package com.umc.nuvibe.domain.tribe.dto.internal;

import java.time.LocalDateTime;

public record ChatSend(

        Long chatId,
        Long senderUserId,
        Long imageId,
        String imageUrl,
        LocalDateTime createdAt
) {
}
