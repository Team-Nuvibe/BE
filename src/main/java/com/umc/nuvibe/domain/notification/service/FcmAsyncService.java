package com.umc.nuvibe.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.umc.nuvibe.domain.notification.entity.Fcm;
import com.umc.nuvibe.domain.notification.repository.FcmRepository;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.UserSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAsyncService {

    private final FcmRepository fcmRepository;
    private final FcmDbService fcmDbService;
    private final FcmTokenService fcmTokenService;
    private final UserRepository userRepository;

    @Async
    public void sendNotification(User user, NotificationType type, String tag, String nickname, Long relatedId, Long tribeId) {
        // 1. DB에 알림 저장 (별도 트랜잭션)
        fcmDbService.saveNotification(user, type, tag, relatedId, tribeId);

        // 2. 푸시 메시지가 없으면 FCM 발송 스킵
        if (type.getPushMessage() == null) {
            return;
        }

        // 3. 알림 설정 체크
        User managedUser = userRepository.findById(user.getId()).orElse(null);
        if (managedUser == null || !isNotificationEnabled(managedUser, type)) {
            return;
        }

        // 4. FCM 푸시 발송
        String pushMessage = type.formatPushMessage(tag, nickname);
        List<Fcm> tokens = fcmRepository.findByUserAndIsActiveTrue(managedUser);
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