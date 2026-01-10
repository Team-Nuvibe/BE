package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.AuthRequest;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;

public interface AuthService {
    void signUp(AuthRequest.SignUpReq request);
    TokenRes login(AuthRequest.LoginReq request);
    void logout(Long userId);
    void withdraw(Long userId);
}
