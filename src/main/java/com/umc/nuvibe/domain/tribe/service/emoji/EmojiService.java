package com.umc.nuvibe.domain.tribe.service.emoji;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;

public interface EmojiService {

    // 이모지 등록
    void emojiReact(Long userId, Long chatId, EmojiType type);
}
