package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpReq(
        @NotBlank
        String name,
        @NotBlank
        String nickname,
        @NotBlank
        String email,
        @NotBlank
       String password,
        @NotBlank
        String confirmPassword
) {
}
