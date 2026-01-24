package com.umc.nuvibe.domain.tribe.dto.internal;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;

// 채팅 이모지 단건 조회 수신
public record EmojiCountRow(

        EmojiType emojiType,
        Long count
) {
}
