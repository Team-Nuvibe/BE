package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    void signUp(SignUpReq request);
    TokenRes login(LoginReq request);
    void logout(Long userId);
    void withdraw(Long userId);
    void sendJoinVerificationEmail(String email);
    void verifyJoinEmailAndRedirect(String token, HttpServletResponse response) throws IOException;
}
