package com.umc.nuvibe.domain.tribe.service.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TribeCloseScheduler {

    private final TribeCloseRunner tribeCloseRunner;
    private final Clock clock;

    // 매일 12시간마다 배치 실행
    @Scheduled(cron = "0 0 0/12 * * *", zone = "Asia/Seoul")
    public void run() {
        tribeCloseRunner.run(LocalDateTime.now(clock));
    }
}
