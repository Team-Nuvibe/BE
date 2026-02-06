package com.umc.nuvibe.domain.tribe.service.internal;

import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.repository.*;
import com.umc.nuvibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TribeCloseProcessor {

    private final ChatRepository chatRepository;
    private final EmojiRepository emojiRepository;
    private final ScrapedImageRepository scrapedImageRepository;
    private final UserTribeRepository userTribeRepository;
    private final TribeRepository tribeRepository;
    private final FcmService fcmService;

    // 실패 시 부분 롤백으로 제한하기 위해 REQUIRES_NEW 설정
    // 트라이브 챗 자동 삭제 과정
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processTribeClose(Long tribeId) {

        // 삭제 전에 알림에 필요한 데이터 미리 조회
        Tribe tribe = tribeRepository.findById(tribeId).orElse(null);
        String tagName = tribe != null ? tribe.getImageTag().name() : null;
        List<User> participants = tribe != null ? userTribeRepository.findAllUsersByTribeId(tribeId) : List.of();

        // 삭제 순서
        // 1. Emoji
        List<Long> chatIds = chatRepository.findIdsByTribeId(tribeId);
        if (!chatIds.isEmpty()) {
            emojiRepository.deleteByChatIds(chatIds);
        }

        // 2. ScrapedImage
        scrapedImageRepository.deleteByTribeId(tribeId);

        // 3. Chat
        chatRepository.deleteByTribeId(tribeId);

        // 4. UserTribe
        userTribeRepository.deleteByTribeId(tribeId);

        // 5. Tribe
        tribeRepository.deleteById(tribeId);

        // 트랜잭션 커밋 후 알림 발송(NOTI-06)
        if (tribe != null && !participants.isEmpty()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            fcmService.sendNotificationToUsers(
                                    participants,
                                    NotificationType.NOTI_06,
                                    tagName,
                                    tribeId,
                                    null
                            );
                        }
                    }
            );
        }
    }
}
