package com.umc.nuvibe.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SignUpReq {
        @NotNull
        private String name;
        @NotNull
        private String nickname;
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String confirmPassword;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginReq {
        @NotNull
        private String email;
        @NotNull
        private String password;
    }


}
