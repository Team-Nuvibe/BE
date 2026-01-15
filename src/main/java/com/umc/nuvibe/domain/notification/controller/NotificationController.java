package com.umc.nuvibe.domain.notification.controller;

import com.umc.nuvibe.domain.notification.dto.NotificationResponse;
import com.umc.nuvibe.domain.notification.service.NotificationService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.NotificationResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다.")
    public Response<List<NotificationResponse>> getNotifications(@AuthUser Long userId) {
        return Response.of(NotificationResultCode.NOTIFICATION_LIST_SUCCESS,
                notificationService.getNotifications(userId));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    public Response<Void> markAsRead(
            @AuthUser Long userId,
            @Parameter(description = "알림 ID") @PathVariable Long notificationId) {
        notificationService.markAsRead(userId, notificationId);
        return Response.of(NotificationResultCode.NOTIFICATION_READ_SUCCESS, null);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    public Response<Void> deleteNotification(
            @AuthUser Long userId,
            @Parameter(description = "알림 ID") @PathVariable Long notificationId) {
        notificationService.deleteNotification(userId, notificationId);
        return Response.of(NotificationResultCode.NOTIFICATION_DELETE_SUCCESS, null);
    }
}
