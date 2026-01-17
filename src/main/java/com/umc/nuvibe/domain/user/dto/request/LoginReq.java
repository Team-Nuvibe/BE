package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
