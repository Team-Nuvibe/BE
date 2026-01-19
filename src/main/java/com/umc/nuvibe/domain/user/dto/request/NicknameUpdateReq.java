package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NicknameUpdateReq(
        @NotBlank
        String nickname
) {
}

