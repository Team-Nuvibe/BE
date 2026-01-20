package com.umc.nuvibe.domain.tribe.dto.internal;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;

/**
 * 특정 유저가 각 채팅에 대해 누른 이모지 타입
 * chatId 당 최대 1건
 */
public record MyEmojiRow(
        Long chatId,
        EmojiType emojiType
) {
}
