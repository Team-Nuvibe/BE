package com.umc.nuvibe.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.umc.nuvibe.domain.notification.entity.Fcm;
import com.umc.nuvibe.domain.notification.repository.FcmRepository;
import com.umc.nuvibe.domain.notification.repository.NotificationRepository;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.UserSetting;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmRepository fcmRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // FCM 토큰 등록/갱신
    @Transactional
    public void registerToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        fcmRepository.findByUserAndToken(user, token)
                .ifPresentOrElse(
                        fcm -> fcm.updateToken(token),
                        () -> fcmRepository.save(Fcm.builder()
                                .user(user)
                                .token(token)
                                .build())
                );
    }

    // 단일 사용자에게 알림 발송
    @Async
    @Transactional
    public void sendNotification(User user, NotificationType type, String tag, String nickname, Long relatedId) {
        // 1. DB에 알림 저장 (알림함용) - 항상 저장
        com.umc.nuvibe.domain.notification.entity.Notification notification =
                com.umc.nuvibe.domain.notification.entity.Notification.builder()
                        .user(user)
                        .type(type)
                        .category(type.getCategory())
                        .mainMessage(type.formatMainMessage(tag))
                        .actionMessage(type.getActionMessage())
                        .relatedId(relatedId)
                        .build();
        notificationRepository.save(notification);

        // 2. 알림 설정 체크 - OFF면 푸시 발송 안 함
        if (!isNotificationEnabled(user, type)) {
            return;
        }

        // 3. FCM 토큰 조회 후 푸시 발송
        String pushMessage = type.formatPushMessage(tag, nickname);
        List<Fcm> tokens = fcmRepository.findByUserAndIsActiveTrue(user);
        for (Fcm fcm : tokens) {
            sendPushMessage(fcm.getToken(), pushMessage);
        }
    }

    // 여러 사용자에게 알림 발송
    @Async
    @Transactional
    public void sendNotificationToUsers(List<User> users, NotificationType type, String tag, Long relatedId) {
        for (User user : users) {
            sendNotification(user, type, tag, null, relatedId);
        }
    }

    private boolean isNotificationEnabled(User user, NotificationType type) {
        UserSetting setting = user.getSetting();
        if (setting == null) return true;

        return switch (type) {
            case NOTI_01, NOTI_02 -> setting.getIsTribeCreateAlert();
            case NOTI_03, NOTI_05, NOTI_06 -> setting.getIsTribeChatAlert();
            case NOTI_04 -> setting.getIsReactionAlert();
            case NOTI_07, NOTI_08 -> setting.getIsRecommendAlert();
            case NOTI_09, NOTI_10 -> setting.getIsRecapAlert();
        };
    }

    private void sendPushMessage(String token, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle("Nuvibe")
                            .setBody(body)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
            log.info("FCM 발송 성공: {}", body);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 발송 실패: {}", e.getMessage());
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                fcmRepository.findByToken(token).ifPresent(Fcm::deactivate);
            }
        }
    }
}
