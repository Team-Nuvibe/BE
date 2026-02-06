package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.dto.request.TribeJoinReq;
import com.umc.nuvibe.domain.tribe.dto.response.tribe.TribeJoinRes;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TribeServiceImpl implements TribeService {

    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;


    @Override
    @Transactional
    public TribeJoinRes joinOrCreateTribe(Long userId, TribeJoinReq request) {
        ImageTag selectedTag = request.imageTag();

        // 1. 이미 해당 태그의 채팅방에 입장해있는지 검증
        if (userTribeRepository.existsByUser_IdAndTribe_ImageTag(userId, selectedTag)) {
            throw new BusinessException(TribeErrorCode.ALREADY_JOINED);
        }

        // 2. 유저는 참조로 조회
        User userRef = userRepository.getReferenceById(userId);

        // 3. 인원 수가 100명 이하인 채팅방에서 가장 오래된 버전의 트라이브 조회, 없을 시 새로운 버전 트라이브 생성
        Tribe tribe = tribeRepository.findAvailableRoom(selectedTag)
                .orElseGet(() -> createNewVersionRoom(selectedTag));

        // 4. 트라이브 인원 수 검증 및 인원수 1 증가
        if (tribe.isFull()) {
            throw new BusinessException(TribeErrorCode.TRIBE_FULL_RETRY);
        }
        tribe.incrementCounts();

        // 5. 유저 트라이브 생성 및 저장 (알림 대상 조회 전에 먼저 저장)
        UserTribe userTribe = UserTribe.of(userRef, tribe);
        userTribeRepository.save(userTribe);

        Long tribeId = tribe.getId();
        String tagName = tribe.getImageTag().name();

        if (tribe.getCounts() == 5) {
            // 6-A. 5명 도달 → 상태 전환 + NOTI-01 전원 발송
            tribe.changeStatus();

            List<User> allUsers = userTribeRepository.findWaitingUsersByTribeId(tribeId);

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            // NOTI-01: 전원에게 "새로운 트라이브 챗이 열렸어요"
                            fcmService.sendNotificationToUsers(
                                    allUsers,
                                    NotificationType.NOTI_01,
                                    tagName,
                                    null,       // relatedId
                                    tribeId     // tribeId
                            );
                        }
                    }
            );
        } else {
            // 6-B. 5명 미만 → 기존 대기자들에게 NOTI-02 발송 (방금 참여한 유저 제외)
            List<User> existingWaiters = userTribeRepository.findWaitingUsersByTribeIdExcept(tribeId, userId);

            if (!existingWaiters.isEmpty()) {
                TransactionSynchronizationManager.registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                // NOTI-02: 기존 대기자에게 "기다리던 트라이브 챗이 열렸어요"
                                fcmService.sendNotificationToUsers(
                                        existingWaiters,
                                        NotificationType.NOTI_02,
                                        tagName,
                                        null,       // relatedId
                                        tribeId     // tribeId
                                );
                            }
                        }
                );
            }
        }

        return TribeJoinRes.from(tribe, userTribe);
        }

    // 새로운 버전의 트라이브 챗 생성
    private Tribe createNewVersionRoom(ImageTag imageTag) {
            int nextVersion = tribeRepository.findTopByImageTagOrderByVersionDesc(imageTag)
                    .map(Tribe::getVersion)
                    .map(v -> v + 1)
                    .orElse(1);

            Tribe newTribe = Tribe.create(imageTag, nextVersion);
            return tribeRepository.save(newTribe);
    }
}
