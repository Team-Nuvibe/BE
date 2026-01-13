package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SignUpReq {
        @NotBlank
        private String name;
        @NotBlank
        private String nickname;
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String confirmPassword;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginReq {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }


}
