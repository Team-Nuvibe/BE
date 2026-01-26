package com.umc.nuvibe.domain.notification.service;

import com.umc.nuvibe.domain.notification.entity.Fcm;
import com.umc.nuvibe.domain.notification.repository.FcmRepository;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmRepository fcmRepository;
    private final UserRepository userRepository;
    private final FcmAsyncService fcmAsyncService;

    @Transactional
    public void registerToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        if (!fcmRepository.existsByUserAndToken(user, token)) {
            fcmRepository.save(Fcm.builder()
                    .user(user)
                    .token(token)
                    .build());
        }
    }

    // 단일 사용자 알림 - 위임
    public void sendNotification(User user, NotificationType type, String tag, String nickname, Long relatedId) {
        fcmAsyncService.sendNotification(user, type, tag, nickname, relatedId);
    }

    // 여러 사용자 알림 - 각각 비동기로 호출
    public void sendNotificationToUsers(List<User> users, NotificationType type, String tag, Long relatedId) {
        for (User user : users) {
            fcmAsyncService.sendNotification(user, type, tag, null, relatedId);
        }
    }
}
