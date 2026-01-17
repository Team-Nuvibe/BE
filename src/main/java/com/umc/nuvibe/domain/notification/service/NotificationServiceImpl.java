package com.umc.nuvibe.domain.notification.service;

import com.umc.nuvibe.domain.notification.dto.NotificationResponse;
import com.umc.nuvibe.domain.notification.entity.Notification;
import com.umc.nuvibe.domain.notification.repository.NotificationRepository;
import com.umc.nuvibe.global.apiPayLoad.error.NotificationErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getContent(),
                        n.isRead(),
                        n.getCreatedAt()))
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        // 단건 조회 후 Dirty Checking을 통한 업데이트
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        notification.read(); // 상태 변경 (트랜잭션 커밋 시 반영)
    }

    @Override
    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        // 단건 조회 후 JPA 표준 삭제
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }
}
