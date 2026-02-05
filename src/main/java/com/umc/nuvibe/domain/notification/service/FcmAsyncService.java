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
import com.umc.nuvibe.domain.user.vo.UserSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAsyncService {

    private final FcmRepository fcmRepository;
    private final NotificationRepository notificationRepository;
    private final FcmTokenService fcmTokenService;

    @Async
    @Transactional
    public void sendNotification(User user, NotificationType type, String tag, String nickname, Long relatedId, Long tribeId) {
        // 1. DB에 알림 저장
        com.umc.nuvibe.domain.notification.entity.Notification notification =
                com.umc.nuvibe.domain.notification.entity.Notification.builder()
                        .user(user)
                        .type(type)
                        .category(type.getCategory())
                        .mainMessage(type.formatMainMessage(tag))
                        .actionMessage(type.getActionMessage())
                        .relatedId(relatedId)
                        .tribeId(tribeId)
                        .build();
        notificationRepository.save(notification);

        // 2. 푸시 메시지가 없으면 FCM 발송 스킵 (추가)
        if (type.getPushMessage() == null) {
            return;
        }

        // 3. 알림 설정 체크
        if (!isNotificationEnabled(user, type)) {
            return;
        }

        // 4. FCM 푸시 발송
        String pushMessage = type.formatPushMessage(tag, nickname);
        List<Fcm> tokens = fcmRepository.findByUserAndIsActiveTrue(user);
        for (Fcm fcm : tokens) {
            sendPushMessage(fcm.getToken(), pushMessage);
        }
    }

    private boolean isNotificationEnabled(User user, NotificationType type) {
        UserSetting setting = user.getSetting();
        if (setting == null) return true;

        return switch (type) {
            case NOTI_01, NOTI_02 -> Boolean.TRUE.equals(setting.getIsTribeCreateAlert());
            case NOTI_03, NOTI_05, NOTI_06 -> Boolean.TRUE.equals(setting.getIsTribeChatAlert());
            case NOTI_04 -> Boolean.TRUE.equals(setting.getIsReactionAlert());
            case NOTI_07, NOTI_08 -> Boolean.TRUE.equals(setting.getIsRecommendAlert());
            case NOTI_09, NOTI_10 -> Boolean.TRUE.equals(setting.getIsRecapAlert());
            case NOTI_11, NOTI_12 -> true;  // 푸시 없으니 항상 true
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
                fcmTokenService.deactivateToken(token);  // 변경
            }
        }
    }
}