package com.umc.nuvibe.domain.notification.scheduler;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.dto.internal.CloseTargetView;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final FcmService fcmService;
    private final Clock clock;
    private final UserRepository userRepository;

    private static final int MIN_ACTIVE_COUNT = 5;


    // NOTI-05: 트라이브 종료 예고 (D-1)
    // 매일 0시, 12시에 실행
    // 6일 경과 + 활성 인원 5명 미만인 트라이브 대상
    @Scheduled(cron = "0 0 0/12 * * *", zone = "Asia/Seoul")
    public void sendTribeCloseWarning() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime cutoff = now.minusHours(144L);
        LocalDateTime excludeCutoff = now.minusHours(168L);

        int page = 0;
        Slice<CloseTargetView> warningTargets;

        do {
            warningTargets = tribeRepository.findCloseWarningTargets(
                    cutoff,
                    excludeCutoff,
                    UserTribeStatus.ACTIVE,
                    MIN_ACTIVE_COUNT,
                    PageRequest.of(page, 100)
            );

            for (CloseTargetView target : warningTargets) {
                try {
                    List<User> participants = userTribeRepository.findUsersByTribeId(target.getTribeId());
                    String tag = target.getImageTag() != null ? target.getImageTag().name() : "";

                    fcmService.sendNotificationToUsers(
                            participants,
                            NotificationType.NOTI_05,
                            tag,
                            target.getTribeId()
                    );
                } catch (Exception e) {
                    log.error("NOTI-05 발송 실패. tribeId={}", target.getTribeId(), e);
                }
            }

            page++;
        } while (warningTargets.hasNext());
    }


    // NOTI-07: 드랍 미션 리마인드
    // 매일 저녁 8시에 실행당일
    // 이미지 업로드 안 한 사용자 대상
    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Seoul")
    public void sendDropReminder() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();

        int page = 0;
        Slice<User> usersWithoutDrop;

        do {
            usersWithoutDrop = userRepository.findUsersWithoutDropToday(
                    todayStart,
                    PageRequest.of(page, 100)
            );

            for (User user : usersWithoutDrop) {
                try {
                    fcmService.sendNotification(
                            user,
                            NotificationType.NOTI_07,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    log.error("NOTI-07 발송 실패. userId={}", user.getId(), e);
                }
            }

            page++;
        } while (usersWithoutDrop.hasNext());
    }


    // NOTI-08: 태그 추천 알림
    // 매주 화, 금 오후 2시에 실행
    // 최근 3일간 드랍 없는 사용자 대상
    @Scheduled(cron = "0 0 14 * * TUE,FRI", zone = "Asia/Seoul")
    public void sendTagRecommendation() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime threeDaysAgo = now.minusDays(3);

        ImageTag[] allTags = ImageTag.values();
        Random random = new Random();

        int page = 0;
        Slice<User> inactiveUsers;

        do {
            inactiveUsers = userRepository.findUsersWithoutDropSince(
                    threeDaysAgo,
                    PageRequest.of(page, 100)
            );

            for (User user : inactiveUsers) {
                try {
                    ImageTag randomTag = allTags[random.nextInt(allTags.length)];
                    fcmService.sendNotification(user, NotificationType.NOTI_08, randomTag.name(), null, null);
                } catch (Exception e) {
                    log.error("NOTI-08 발송 실패. userId={}", user.getId(), e);
                }
            }
            page++;
        } while (inactiveUsers.hasNext());
    }


    // NOTI-09: 주간 리캡 알림
    // 매주 일요일 오전 10시에 실행
    // 지난 주 드랍 기록이 있는 사용자 대상
    @Scheduled(cron = "0 0 10 * * SUN", zone = "Asia/Seoul")
    public void sendWeeklyRecap() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime weekStart = now.minusWeeks(1).toLocalDate().atStartOfDay();
        LocalDateTime weekEnd = now.toLocalDate().atStartOfDay();

        int page = 0;
        Slice<User> usersWithWeeklyDrop;

        do {
            usersWithWeeklyDrop = userRepository.findUsersWithDropBetween(
                    weekStart,
                    weekEnd,
                    PageRequest.of(page, 100)
            );

            for (User user : usersWithWeeklyDrop) {
                try {
                    fcmService.sendNotification(user, NotificationType.NOTI_09, null, null, null);
                } catch (Exception e) {
                    log.error("NOTI-09 발송 실패. userId={}", user.getId(), e);
                }
            }
            page++;
        } while (usersWithWeeklyDrop.hasNext());
    }


    // NOTI-10: 전체 리캡 알림
    // 매월 1일 오전 10시에 실행
    // 지난 달 드랍 기록이 있는 사용자 대상
    @Scheduled(cron = "0 0 10 1 * *", zone = "Asia/Seoul")
    public void sendMonthlyRecap() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime monthStart = now.minusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime monthEnd = now.withDayOfMonth(1).toLocalDate().atStartOfDay();

        int page = 0;
        Slice<User> usersWithMonthlyDrop;

        do {
            usersWithMonthlyDrop = userRepository.findUsersWithDropBetween(
                    monthStart,
                    monthEnd,
                    PageRequest.of(page, 100)
            );

            for (User user : usersWithMonthlyDrop) {
                try {
                    fcmService.sendNotification(user, NotificationType.NOTI_10, null, null, null);
                } catch (Exception e) {
                    log.error("NOTI-10 발송 실패. userId={}", user.getId(), e);
                }
            }
            page++;
        } while (usersWithMonthlyDrop.hasNext());
    }

}