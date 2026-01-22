package com.umc.nuvibe.domain.notification.service;

import com.umc.nuvibe.domain.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    /**
     * 알림 목록 조회
     */
    List<NotificationResponse> getNotifications(Long userId);

    /**
     * 알림 읽음 처리
     */
    void markAsRead(Long userId, Long notificationId);

    /**
     * 알림 삭제
     */
    void deleteNotification(Long userId, Long notificationId);
}
