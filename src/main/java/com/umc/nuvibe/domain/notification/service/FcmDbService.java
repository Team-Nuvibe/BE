package com.umc.nuvibe.domain.notification.service;

import com.umc.nuvibe.domain.notification.entity.Notification;
import com.umc.nuvibe.domain.notification.repository.NotificationRepository;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmDbService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveNotification(User user, NotificationType type, String tag, Long relatedId, Long tribeId) {
        User managedUser = userRepository.getReferenceById(user.getId());
        Notification notification = Notification.builder()
                .user(managedUser)
                .type(type)
                .category(type.getCategory())
                .mainMessage(type.formatMainMessage(tag))
                .actionMessage(type.formatActionMessage(tag))
                .relatedId(relatedId)
                .tribeId(tribeId)
                .build();
        notificationRepository.save(notification);
    }
}