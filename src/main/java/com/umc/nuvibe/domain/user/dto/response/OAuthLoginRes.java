package com.umc.nuvibe.domain.user.dto.response;

import com.umc.nuvibe.domain.user.vo.AuthProvider;

public record OAuthLoginRes(
        String accessToken,
        String refreshToken,
        boolean isNewUser,
        Long userId,
        String email,
        AuthProvider provider
) { }
