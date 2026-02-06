package com.umc.nuvibe.global.scheduler;

import com.umc.nuvibe.domain.user.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTokenCleanupScheduler {

    private final EmailVerificationTokenRepository tokenRepository;

    // 만료된 이메일 인증 토큰 정리
    // 5분마다 실행, 생성된 지 5분 이상 된 토큰 삭제
    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        int deletedCount = tokenRepository.deleteAllCreatedBefore(cutoff);

        if (deletedCount > 0) {
            log.info("만료된 이메일 인증 토큰 {}건 삭제 완료", deletedCount);
        }
    }
}
