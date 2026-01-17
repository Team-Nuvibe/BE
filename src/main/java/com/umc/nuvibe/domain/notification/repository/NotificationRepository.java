package com.umc.nuvibe.domain.notification.repository;

import com.umc.nuvibe.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 사용자의 알림 목록 조회 (최신순)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 사용자의 특정 알림 조회 (권한 체크 및 단건 처리용)
    Optional<Notification> findByIdAndUserId(Long id, Long userId);

}
