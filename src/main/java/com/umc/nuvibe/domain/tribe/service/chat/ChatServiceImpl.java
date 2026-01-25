package com.umc.nuvibe.domain.tribe.service.chat;

import com.umc.nuvibe.domain.archive.service.ArchiveBoardService;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.dto.internal.*;
import com.umc.nuvibe.domain.tribe.dto.request.ChatGridReq;
import com.umc.nuvibe.domain.tribe.dto.request.ChatTimelineReq;
import com.umc.nuvibe.domain.tribe.dto.response.chat.*;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.repository.*;
import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.ChatErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final EmojiRepository emojiRepository;
    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final ScrapedImageRepository scrapedImageRepository;
    private final UserRepository userRepository;

    private final ImageService imageService;
    private final ArchiveBoardService archiveBoardService;

    private final SimpMessagingTemplate messagingTemplate;

    private final FcmService fcmService;

    @Override
    @Transactional(readOnly = true)
    public ChatTimelineListRes getChatTimelineList(Long userId, Long tribeId, ChatTimelineReq req) {

        // 1. 유저-트라이브 접근 권한 체크
        validateUserInTribe(userId, tribeId);

        // 1-1. 커서 유효성 체크
        if (req.lastChatId() != null) {
            validateCursorChatInTribe(req.lastChatId(), tribeId);
        }

        // 2. size는 dto에서 기본값 처리 (null → 20)
        int limit = req.size();
        Pageable pageable = PageRequest.of(0, limit + 1);

        // 3. lastChatId 여부에 따라 첫 페이지 / 다음 페이지 분기
        List<Chat> chats = (req.lastChatId() == null)
                ? chatRepository.findChatTimelineFirstPage(tribeId, pageable)
                : chatRepository.findChatTimelineNextPage(tribeId, req.lastChatId(), pageable);

        // 4. hasNext 판단
        boolean hasNext = chats.size() > limit;
        List<Chat> pageItems = hasNext ? chats.subList(0, limit) : chats;

        // 5. 채팅들에서 chatIds 추출
        List<Long> chatIds = pageItems.stream()
                .map(Chat::getId)
                .toList();

        // 6. 채팅이 아직 존재하지 않을 시 반환
        if (chatIds.isEmpty()) {
            return new ChatTimelineListRes(List.of(), null, false);
        }

        // 7. 각 채팅당 이모지 집계 및 요약
        Map<Long, List<EmojiSummaryRes>> emojiSummaryMap =
                buildEmojiSummaryMap(chatIds);

        // 8. 내가 누른 이모지 매핑
        Map<Long, EmojiType> myEmojiMap =
                buildMyEmojiMap(userId, chatIds);

        // 9. dto로 매핑
        List<ChatTimelineItemRes> items = pageItems.stream()
                .map(chat -> ChatTimelineItemRes.from(
                        chat,
                        emojiSummaryMap.getOrDefault(chat.getId(), List.of()),
                        myEmojiMap.get(chat.getId())
                ))
                .toList();

        // 10. 다음 페이지 존재 시 마지막 데이터 정보로 커서 설정
        Long nextLastChatId = null;
        if (hasNext && !items.isEmpty()) {
            nextLastChatId = items.get(items.size() - 1).chatId();
        }

        return new ChatTimelineListRes(items, nextLastChatId, hasNext);

    }

    @Override
    @Transactional(readOnly = true)
    public ChatGridListRes getChatGridList(Long userId, Long tribeId, ChatGridReq req) {

        // 1. 유저-트라이브 유효성 검증
        validateUserInTribe(userId, tribeId);

        // 1-1. 커서 유효성 체크
        if (req.hasCursor() && !req.isCursorComplete()) {
            throw new BusinessException(ChatErrorCode.CHAT_CURSOR_INVALID);
        }

        // 1-2. 커서 소속 트라이브 검증
        if (req.hasCursor()) {
            validateCursorChatInTribe(req.cursorChatId(), tribeId);
        }

        // 2. size는 dto에서 기본값 처리 (null -> 30)
        int limit = req.size();
        Pageable pageable = PageRequest.of(0, limit + 1);

        // 3. 커서 여부에 따라 첫 페이지 / 다음 페이지 분기
        List<Chat> chats = (!req.hasCursor())
                ? chatRepository.findChatGridFirstPage(tribeId, pageable)
                : chatRepository.findChatGridNextPage(tribeId, req.cursorCreatedAt(), req.cursorChatId(), pageable);

        // 4. hasNext 판단
        boolean hasNext = chats.size() > limit;
        List<Chat> pageItems = hasNext ? chats.subList(0, limit) : chats;

        // 5. 채팅이 아직 없을 시 반환
        if (pageItems.isEmpty()) {
            return new ChatGridListRes(List.of(), null, null, false);
        }

        // 6. dto로 매핑
        List<ChatGridItemRes> items = pageItems.stream()
                .map(ChatGridItemRes::from)
                .toList();

        // 7. 다음 커서 계산 (hasNext=true일 때만)
        LocalDateTime nextCursorCreatedAt = null;
        Long nextCursorChatId = null;

        if (hasNext && !items.isEmpty()) {
            ChatGridItemRes last = items.get(items.size() - 1);
            nextCursorCreatedAt = last.createdAt();
            nextCursorChatId = last.chatId();
        }

        return new ChatGridListRes(items, nextCursorCreatedAt, nextCursorChatId, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDetailRes getChatDetail(Long userId, Long chatId){

        Chat chat = chatRepository.findDetailByChatId(chatId)
                .orElseThrow(() -> new BusinessException(ChatErrorCode.CHAT_NOT_FOUND));

        validateUserInTribe(userId, chat.getTribe().getId());

        boolean isScraped = scrapedImageRepository.existsByUser_IdAndImage_Id(
                userId,
                chat.getImage().getId()
        );

        return ChatDetailRes.from(chat, isScraped);
    }

    @Override
    @Transactional
    public void chatSend(Long userId, Long tribeId, MultipartFile file, Long boardId){

        // 1. 트라이브 존재 검증
        Tribe tribe = tribeRepository.findById(tribeId)
                .orElseThrow(() -> new BusinessException(TribeErrorCode.TRIBE_NOT_FOUND));

        // 2. 발신 권한 검증
        boolean canSend = userTribeRepository.existsByUser_IdAndTribe_IdAndUserTribeStatus(
                userId, tribeId, UserTribeStatus.ACTIVE
        );
        if (!canSend) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_FORBIDDEN);
        }

        // 3. 태그 검증 (해당 트라이브 태그 사용)
        ImageTag tag = tribe.getImageTag();
        if (tag == null) {
            throw new BusinessException(ImageErrorCode.IMAGETAG_IS_NULL);
        }

        // 4. 이미지 업로드 + 이미지 엔티티 저장
        Image image = imageService.uploadAndSaveEntity(file, tag);

        // 5. 채팅 저장 (유저는 참조)
        User userRef = userRepository.getReferenceById(userId);
        Chat chat = Chat.of(userRef, tribe, image);
        chatRepository.save(chat);

        // NOTI-03: 트라이브 참여자들에게 새 바이브 알림 (본인 제외)
        List<User> participants = userTribeRepository.findUsersByTribeIdExcept(tribeId, userId);
        fcmService.sendNotificationToUsers(
                participants,
                NotificationType.NOTI_03,
                tribe.getImageTag().name(),
                tribeId
        );

        // 6. 보드에 이미지 저장
        archiveBoardService.addBoardImage(userId, boardId, image.getId());

        // 7. 발신할 record 생성
        ChatSend chatSend = new ChatSend(
                chat.getId(),
                userId,
                image.getId(),
                image.getImageUrl(),
                chat.getCreatedAt()
        );

        registerChatPublish(tribeId, chatSend);

    }

    // 커밋 이후 채팅을 소속 트라이브로 발송
    private void registerChatPublish(Long tribeId, ChatSend chatSend) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                messagingTemplate.convertAndSend(
                        "/topic/tribe." + tribeId,
                        chatSend
                );
            }
        }
        );
    }


    /**
     * 각 채팅에 대해 이모지 타입별 개수를 집계 및 요약한 결과
     * EmojiSummaryRes 목록 형태로 변환
     */
    private Map<Long, List<EmojiSummaryRes>> buildEmojiSummaryMap(List<Long> chatIds) {
        List<EmojiAggRow> rows =
                emojiRepository.countByChatIdGroupByType(chatIds);

        return rows.stream()
                .collect(Collectors.groupingBy(
                        EmojiAggRow::chatId,
                        Collectors.mapping(
                                r -> new EmojiSummaryRes(r.emojiType(), r.count()),
                                Collectors.toList()
                        )
                ));
    }

    /**
     * 특정 유저가 각 chat에 대해 선택한 이모지를 EmojiType 형태로 매핑
     * 이모지는 종류 불문없이 chatId당 최대 1건
     */
    private Map<Long, EmojiType> buildMyEmojiMap(Long userId, List<Long> chatIds) {
        List<MyEmojiRow> rows =
                emojiRepository.findMyEmoji(userId, chatIds);

        return rows.stream()
                .collect(Collectors.toMap(
                        MyEmojiRow::chatId,
                        MyEmojiRow::emojiType
                ));
    }

    // lastChatId 존재 시 해당 커서가 동일한 트라이브 챗 내의 커서인지 확인
    private void validateCursorChatInTribe(Long lastChatId, Long tribeId) {
        if (!chatRepository.existsByIdAndTribe_Id(lastChatId, tribeId)) {
            throw new BusinessException(ChatErrorCode.CHAT_CURSOR_INVALID);
        }
    }

    // 유저가 해당 트라이브 내에 입장해있는지 확인
    private void validateUserInTribe(Long userId, Long tribeId) {
        if (!userTribeRepository.existsByUser_IdAndTribe_Id(userId, tribeId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND);
        }
    }
}
