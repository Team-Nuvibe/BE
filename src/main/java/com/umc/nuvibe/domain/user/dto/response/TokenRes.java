package com.umc.nuvibe.domain.user.dto.response;

public record TokenRes (
        String accessToken,
        String refreshToken
){
}
