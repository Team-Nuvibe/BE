package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissuePasswordReq(
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword
) {
}
