package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetReq(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String code,

        @NotBlank
        String newPassword,

        @NotBlank
        String confirmPassword
) {
}
