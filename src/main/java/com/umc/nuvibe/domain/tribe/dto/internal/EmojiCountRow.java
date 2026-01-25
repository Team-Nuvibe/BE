package com.umc.nuvibe.domain.tribe.dto.internal;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;

// 채팅 이모지 타입별 집계 결과
public record EmojiCountRow(

        EmojiType emojiType,
        Long count
) {
}
