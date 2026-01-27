package com.umc.nuvibe.domain.tribe.dto.internal;

import com.umc.nuvibe.domain.image.vo.ImageTag;

import java.time.LocalDateTime;

// 1차 조회용 Active 트라이브 기본 정보
public record ActiveTribeRow(

        Long tribeId,
        ImageTag imageTag,
        Integer counts,
        boolean isFavorite,
        LocalDateTime lastActivityAt,
        int unreadCount,
        Long lastChatId

) {
}
