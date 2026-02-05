package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetEmailReq(
        @NotBlank
        @Email
        String email
) {
}
