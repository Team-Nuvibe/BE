package com.umc.nuvibe.domain.tribe.service.emoji;

import com.umc.nuvibe.domain.tribe.dto.internal.EmojiChanged;
import com.umc.nuvibe.domain.tribe.dto.internal.EmojiCountRow;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.entity.Emoji;
import com.umc.nuvibe.domain.tribe.repository.ChatRepository;
import com.umc.nuvibe.domain.tribe.repository.EmojiRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.error.ChatErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class EmojiServiceImpl implements EmojiService {

    private final EmojiRepository emojiRepository;
    private final ChatRepository chatRepository;
    private final UserTribeRepository userTribeRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final Clock clock;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void emojiReact(Long userId, Long chatId, EmojiType type) {

        // 1. chatId로 tribeId 조회
        Long tribeId = chatRepository.findTribeIdByChatId(chatId)
                .orElseThrow(() -> new BusinessException(ChatErrorCode.CHAT_NOT_FOUND));

        // 2. 발신 권한 검증
        boolean canReact = userTribeRepository.existsByUser_IdAndTribe_IdAndUserTribeStatus(
                userId, tribeId, UserTribeStatus.ACTIVE
        );
        if (!canReact) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_FORBIDDEN);
        }

        // 3. 기존 emoji 조회
        Optional<Emoji> existingEmoji = emojiRepository.findByChatIdAndUserId(chatId, userId);

        // 이모지 등록 상태
        String action;
        // 등록하는 이모지 (삭제일 시 null)
        String actorEmojiType;

        if (existingEmoji.isEmpty()) {

            // 4. 기존 등록된 이미지가 없을 시에는 새로 저장
            Chat chatRef = em.getReference(Chat.class, chatId);
            User userRef = em.getReference(User.class, userId);

            emojiRepository.save(Emoji.of(type, chatRef, userRef));

            action = "CREATED";
            actorEmojiType = type.name();

        } else {
            // 4-1. 등록된 이미지가 있을 시 동일하면 삭제, 다르면 변경
            Emoji existing = existingEmoji.get();

            if (existing.getType() == type) {
                // 같은 타입일 시 삭제
                emojiRepository.delete(existing);

                action = "DELETED";
                actorEmojiType = null;

            } else {
                // 다른 타입일 시 변경
                existing.changeType(type);

                action = "UPDATED";
                actorEmojiType = type.name();
            }
        }

        // 5. 커밋 이후에만 WS 발행
        registerEmojiPublish(tribeId, chatId, userId, action, actorEmojiType);
    }

    // 커밋 이후 이모지 집계를 각 트라이브로 발송
    private void registerEmojiPublish(Long tribeId, Long chatId, Long userId, String action, String actorEmojiType) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {

                // 1. 개별 채팅 이모지 집계
                List<EmojiCountRow> rows = emojiRepository.countGroupByType(chatId);

                // 2. 모든 타입 0으로 채운 뒤 map으로 반환
                Map<String, Long> summaryCounts = buildSummaryCounts(rows);

                // 3. 발신할 record 생성
                EmojiChanged emojiReact = new EmojiChanged(
                        tribeId,
                        chatId,
                        userId,
                        action,
                        actorEmojiType,
                        summaryCounts,
                        now(clock) // clock 쓰면 now(clock)
                );

                // 4. 각 트라이브로 발송
                messagingTemplate.convertAndSend(
                        "/topic/tribe." + tribeId,
                        emojiReact
                );
            }
        }
        );
    }

    // 각 채팅에 대해 이모지 타입별 개수를 집계 및 요약
    private Map<String, Long> buildSummaryCounts(List<EmojiCountRow> rows) {

        // 모든 타입 0 채움
        Map<String, Long> map = new java.util.LinkedHashMap<>();
        for (EmojiType t : EmojiType.values()) {
            map.put(t.name(), 0L);
        }
        // 집계 반영
        for (EmojiCountRow r : rows) {
            map.put(r.emojiType().name(), r.count());
        }
        return map;
    }
}
