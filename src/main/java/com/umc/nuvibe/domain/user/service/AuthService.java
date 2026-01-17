package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;

public interface AuthService {
    void signUp(SignUpReq request);
    TokenRes login(LoginReq request);
    void logout(Long userId);
    void withdraw(Long userId);
}
