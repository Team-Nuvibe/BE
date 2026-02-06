package com.umc.nuvibe.domain.notification.dto;

import com.umc.nuvibe.domain.notification.entity.Notification;  // [추가] Notification import
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


//알림 목록 응답 DTO
public record NotificationResponse(
        @Schema(description = "알림 ID") Long notificationId,
        @Schema(description = "알림 타입") String type,
        @Schema(description = "알림 종류") String category,
        @Schema(description = "메인 메시지") String mainMessage,
        @Schema(description = "행동 유도 메시지") String actionMessage,
        @Schema(description = "연관 ID") Long relatedId,
        @Schema(description = "트라이브 ID") Long tribeId,
        @Schema(description = "읽음 여부") boolean isRead,
        @Schema(description = "생성 시간") LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType().getClientType(),
                notification.getCategory(),
                notification.getMainMessage(),
                notification.getActionMessage(),
                notification.getRelatedId(),
                notification.getTribeId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
