package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.OAuthSignupReq;
import com.umc.nuvibe.domain.user.dto.response.OAuthLoginRes;
import com.umc.nuvibe.domain.user.vo.AuthProvider;

public interface OAuthService {
    String getOAuthAuthorizationUrl(AuthProvider provider, String state);
    OAuthLoginRes processOAuthCallback(AuthProvider provider, String code, String state);  // state 추가
    void completeSignup(Long userId, OAuthSignupReq request);
}