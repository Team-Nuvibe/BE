package com.umc.nuvibe.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 알림 목록 응답 DTO
 */
@Schema(description = "알림 응답")
public record NotificationResponse(

        @Schema(description = "알림 ID") Long notificationId,

        @Schema(description = "알림 내용") String content,

        @Schema(description = "읽음 여부") boolean isRead,

        @Schema(description = "생성 시간") LocalDateTime createdAt) {
}
