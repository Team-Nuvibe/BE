package com.umc.nuvibe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRes {

    String accessToken;
    String refreshToken;
}
