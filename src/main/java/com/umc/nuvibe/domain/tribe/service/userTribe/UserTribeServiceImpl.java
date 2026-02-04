package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeCursor;
import com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeRow;
import com.umc.nuvibe.domain.tribe.dto.request.ActiveTribeListReq;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.*;
import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.LeaveRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeActivateRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeFavoriteRes;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.ScrapedImageRepository;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTribeServiceImpl implements UserTribeService {

    private final UserTribeRepository userTribeRepository;
    private final ScrapedImageRepository scrapedImageRepository;
    private final TribeRepository tribeRepository;
    private final FcmService fcmService;

    @Override
    @Transactional
    public LeaveRes leaveTribe(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED); // 본인 것만 삭제 가능
        }

        Long tribeId = userTribe.getTribe().getId();

        // 원본 Image 엔티티는 유지하고 참조만 삭제
        scrapedImageRepository.deleteAllByUserIdAndTribeId(userId, tribeId);

        userTribeRepository.delete(userTribe);
        tribeRepository.decrementCounts(tribeId);

        return new LeaveRes(userTribeId, tribeId);
    }

    @Override
    @Transactional
    public UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED);
        }

        if (userTribe.getUserTribeStatus() == UserTribeStatus.ACTIVE) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_ALREADY_ACTIVE);
        }

        if (userTribe.getTribe().getStatus() != TribeStatus.WAITING) {
            throw new BusinessException(TribeErrorCode.ACTIVATION_NOT_READY);
        }

        // 유저 트라이브 활성화 및 활동 시각 최신화
        userTribe.activate();
        userTribe.updateLastActivityAt(LocalDateTime.now());

        // 커밋 이후에 FCM 발송 (noti-02)
        User user = userTribe.getUser();
        String tag = userTribe.getTribe().getImageTag().name();
        Long tribeId = userTribe.getTribe().getId();

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        fcmService.sendNotification(
                                user,
                                NotificationType.NOTI_02,
                                tag,
                                null,
                                tribeId
                        );
                    }
                }
        );

        return UserTribeActivateRes.from(userTribe);
    }

    @Override
    @Transactional
    public UserTribeFavoriteRes toggleFavorite(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED);
        }

        userTribe.toggleFavorite();

        return UserTribeFavoriteRes.from(userTribe);
    }

    @Override
    @Transactional(readOnly = true)
    public ActiveTribeListRes getActiveTribeList(Long userId, ActiveTribeListReq req){

        int size = req.size();

        // 1. 커서 값 검증
        boolean hasCursor = req.hasCursor();
        ActiveTribeCursor cursor = req.cursor();

        // 2. 커서 기반 페이징 (size보다 1개 더 조회)
        Pageable pageable = PageRequest.of(0, size + 1);

        // 3. active 트라이브 챗 목록 조회
        // hasCursor=false 이면 첫 페이지, hasCursor=true 이면 커서 이후 데이터만 조회
        List<ActiveTribeRow> rows = userTribeRepository.findActiveTribes(
                userId, UserTribeStatus.ACTIVE, hasCursor,
                hasCursor ? cursor.favInt()     : 0,
                hasCursor ? cursor.lastActivityAt()  : null,
                hasCursor ? cursor.unreadInt()       : 0,
                hasCursor ? cursor.lastChatId()      : 0L,
                pageable
        );

        // 4. 다음 페이지 존재 여부 판단 및 size까지만 반환
        boolean hasNext = rows.size() > size;
        List<ActiveTribeRow> content = hasNext ? rows.subList(0, size) : rows;

        // 5. 내부 조회용 row에서 응답 Dto로 변환
        List<ActiveTribeItemRes> items = content.stream()
                .map(r -> new ActiveTribeItemRes(
                        r.tribeId(),
                        r.userTribeId(),
                        r.imageTag(),
                        r.counts(),
                        r.isFavorite(),
                        r.lastActivityAt(),
                        r.unreadCount()
                ))
                .toList();

        // 6. 다음 커서 생성
        // 다음 페이지 존재 시에만 마지막 요소 기준으로 커서 생성
        ActiveTribeCursor nextCursor = null;

        if (hasNext && !content.isEmpty()) {
            ActiveTribeRow last = content.get(content.size() - 1);

            nextCursor = new ActiveTribeCursor(
                    last.isFavorite(),         // 즐겨찾기 여부
                    last.lastActivityAt(),         // 마지막 채팅 시각
                    last.unreadCount() > 0,    // unreadCount → boolean 커서
                    last.lastChatId()
            );
        }

        // 7. 커서 정보와 함께 챗 리스트 반환
        return new ActiveTribeListRes(items, hasNext, nextCursor);
    }

    @Override
    @Transactional(readOnly = true)
    public WaitingTribeListRes getWaitingTribeList(Long userId, Long cursorTribeId, Integer size){

        // 1. 페이지 사이즈 및 커서 여부 판단
        // size 미지정 시 기본 20
        int pageSize = (size == null || size < 1) ? 20 : size;

        // tribeId 커서가 있으면 다음 페이지 요청
        boolean hasCursor = cursorTribeId != null;

        // 커서 기반 페이징 (size보다 1개 더 조회)
        Pageable pageable = PageRequest.of(0, pageSize + 1);

        // 2. Waiting 트라이브 목록 조회
        // hasCursor=false이면 첫 페이지, hasCursor=true이면 cursorTribeId 보다 작은 tribeId만 조회
        List<WaitingTribeItemRes> rows = userTribeRepository.findWaitingTribes(
                userId, UserTribeStatus.WAITING, hasCursor,
                hasCursor ? cursorTribeId : 0L,
                pageable
        );

        // 3. 다음 페이지 존재 여부 판단 및 size까지만 반환
        boolean hasNext = rows.size() > pageSize;
        List<WaitingTribeItemRes> content = hasNext ? rows.subList(0, pageSize) : rows;

        // 4. 다음 커서 생성
        // 다음 페이지가 있을 때만 마지막 tribeId 기준으로 커서 생성
        Long nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = content.get(content.size() - 1).tribeId();
        }

        return new WaitingTribeListRes(content, hasNext, nextCursor);
    }

    @Override
    @Transactional
    public TribeReadRes readChat(Long userId, Long tribeId) {

        // 1. 읽음 권한 검증
        boolean isActiveMember = userTribeRepository.existsByUser_IdAndTribe_IdAndUserTribeStatus(
                userId, tribeId, UserTribeStatus.ACTIVE
        );
        if (!isActiveMember) {throw new BusinessException(UserTribeErrorCode.USERTRIBE_FORBIDDEN);}

        // 2. 최신 chatId(없으면 empty) 조회
        Optional<Long> lastChatId = tribeRepository.findLastChatId(tribeId);

        // 3. 채팅 0개면 그대로 성공 반환
        if (lastChatId.isEmpty()) {
            return new TribeReadRes(tribeId, null);
        }

        // 4. 안 읽은 메시지 수 0건으로 초기화
        Long lastId = lastChatId.get();
        userTribeRepository.readChat(
                userId,
                tribeId,
                lastId,
                UserTribeStatus.ACTIVE
        );

        return new TribeReadRes(tribeId, lastId);

    }

}
