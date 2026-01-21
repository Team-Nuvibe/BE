package com.umc.nuvibe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        // 서버 기본 타임존 대신 명시적으로 고정
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
