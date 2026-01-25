package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.CheckPasswordReq;
import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;

public interface AuthService {

    // 기본
    void signUp(SignUpReq request);
    TokenRes login(LoginReq request);
    void logout(Long userId);
    void withdraw(Long userId);

    // 회원가입 인증
    void sendJoinVerificationCode(String email);
    void verifyJoinCode(String email, String code);

    // 비번초기화
    void sendPasswordResetCode(String email);
    void verifyPasswordResetCode(String email, String code);
    void resetPasswordWithCode(String email, String code, String newPassword, String confirmPassword);

    // 별도
    void checkCurrentPassword(Long userId, CheckPasswordReq request);
    TokenRes reissueToken(String authorizationHeader);

}
