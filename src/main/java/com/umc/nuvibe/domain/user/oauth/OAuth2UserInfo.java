package com.umc.nuvibe.domain.user.oauth;

import com.umc.nuvibe.domain.user.vo.AuthProvider;

public interface OAuth2UserInfo {
    AuthProvider getProvider();
    String getProviderId();
    String getEmail();
}
