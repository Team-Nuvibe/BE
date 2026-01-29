package com.umc.nuvibe.domain.user.dto.response;

public record OAuthLoginRes(
        String accessToken,
        String refreshToken,
        boolean isNewUser,
        Long userId
) {
}
