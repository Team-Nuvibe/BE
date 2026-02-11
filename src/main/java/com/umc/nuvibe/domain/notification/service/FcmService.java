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
import java.util.Optional;

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

        // 1. 같은 토큰의 다른 유저 행 비활성화
        List<Fcm> otherUserTokens = fcmRepository.findByTokenAndIsActiveTrueAndUserNot(token, user);
        otherUserTokens.forEach(Fcm::deactivate);

        // 2. 같은 유저 + 같은 토큰 행 조회
        Optional<Fcm> existing = fcmRepository.findByUserAndToken(user, token);

        if (existing.isPresent()) {
            // 비활성이면 재활성화
            existing.get().activate();
        } else {
            // 없으면 새로 저장
            fcmRepository.save(Fcm.builder()
                    .user(user)
                    .token(token)
                    .build());
        }
    }

    // 단일 사용자 알림 - 위임
    public void sendNotification(User user, NotificationType type, String tag, Long relatedId, Long tribeId) {
        fcmAsyncService.sendNotification(user, type, tag, relatedId, tribeId);
    }

    // 여러 사용자 알림 - 각각 비동기로 호출
    public void sendNotificationToUsers(List<User> users, NotificationType type, String tag, Long relatedId, Long tribeId) {
        for (User user : users) {
            fcmAsyncService.sendNotification(user, type, tag, relatedId, tribeId);
        }
    }
}
