package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OAuthSignupReq(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname
) {}
