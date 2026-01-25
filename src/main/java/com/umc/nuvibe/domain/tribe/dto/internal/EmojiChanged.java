package com.umc.nuvibe.domain.tribe.dto.internal;

import java.time.LocalDateTime;
import java.util.Map;

public record EmojiChanged(

        Long tribeId,
        Long chatId,
        Long userId,
        String action,
        String actorEmojiType,
        Map<String, Long> summaryCounts,
        LocalDateTime publishedAt
) {
}
