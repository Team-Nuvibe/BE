package com.umc.nuvibe.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record FcmTokenRequest(
    @NotBlank(message = "FCM 토큰은 필수입니다.")
    String token
) {}