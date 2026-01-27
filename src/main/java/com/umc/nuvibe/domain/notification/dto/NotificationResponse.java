package com.umc.nuvibe.domain.notification.dto;

import com.umc.nuvibe.domain.notification.entity.Notification;  // [추가] Notification import
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


//알림 목록 응답 DTO
@Schema(description = "알림 응답")
public record NotificationResponse(

        @Schema(description = "알림 ID") Long notificationId,

        // [삭제] String content 삭제됨

        // [추가] UI 알림함용 - 알림 종류 (채팅, 알림, 미션)
        @Schema(description = "알림 종류") String category,

        // [추가] UI 알림함용 - 메인 메시지
        @Schema(description = "메인 메시지") String mainMessage,

        // [추가] UI 알림함용 - 행동 유도 메시지
        @Schema(description = "행동 유도 메시지") String actionMessage,

        // [추가] 연관 ID (트라이브ID, 이미지ID 등)
        @Schema(description = "연관 ID") Long relatedId,

        @Schema(description = "읽음 여부") boolean isRead,

        @Schema(description = "생성 시간") LocalDateTime createdAt
) {
    // [추가] Entity -> DTO 변환 메서드
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getCategory(),
                notification.getMainMessage(),
                notification.getActionMessage(),
                notification.getRelatedId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
