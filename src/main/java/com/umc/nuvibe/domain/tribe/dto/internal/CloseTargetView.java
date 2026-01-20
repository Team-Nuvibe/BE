package com.umc.nuvibe.domain.tribe.dto.internal;

import java.time.LocalDateTime;

// 자동 종료 대상 조회 전용 projection
public interface CloseTargetView {

    Long getTribeId();
    LocalDateTime getCreatedAt();
}
