package com.umc.nuvibe.domain.tribe.dto.internal;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;

/**
 * 이모지 집계 결과 수신용 record
 * (chatId, EmojiType) 기준 count
 */
public record EmojiAggRow(
        Long chatId,
        EmojiType emojiType,
        Long count
) {
}
