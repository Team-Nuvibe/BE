package com.umc.nuvibe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record TokenRes (
        String accessToken,
        String refreshToken
){
}
