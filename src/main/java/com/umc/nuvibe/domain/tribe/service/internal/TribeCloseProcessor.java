package com.umc.nuvibe.domain.tribe.service.internal;

import com.umc.nuvibe.domain.tribe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TribeCloseProcessor {

    private final ChatRepository chatRepository;
    private final EmojiRepository emojiRepository;
    private final ScrapedImageRepository scrapedImageRepository;
    private final UserTribeRepository userTribeRepository;
    private final TribeRepository tribeRepository;

    // 실패 시 부분 롤백으로 제한하기 위해 REQUIRES_NEW 설정
    // 트라이브 챗 자동 삭제 과정
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processTribeClose(Long tribeId) {

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
    }
}
