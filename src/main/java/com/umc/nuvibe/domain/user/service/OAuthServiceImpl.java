package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.OAuthSignupReq;
import com.umc.nuvibe.domain.user.dto.response.OAuthLoginRes;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.oauth.OAuth2UserInfo;
import com.umc.nuvibe.domain.user.oauth.OAuth2UserInfoFactory;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.AuthProvider;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.config.OAuth2Properties;
import com.umc.nuvibe.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OAuthServiceImpl implements OAuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2Properties oAuth2Properties;
    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofSeconds(10))
            ))
            .build();

    // state 저장용 (간단한 방식 - 운영에서는 Redis 권장)
    private final Map<String, Long> stateStore = new ConcurrentHashMap<>();
    private static final long STATE_EXPIRY_MS = 5 * 60 * 1000; // 5분

    @Override
    public String getOAuthAuthorizationUrl(AuthProvider provider, String state) {
        // state 저장 (생성 시간과 함께)
        stateStore.put(state, System.currentTimeMillis());

        return switch (provider) {
            case GOOGLE -> buildGoogleAuthUrl(state);
            case NAVER -> buildNaverAuthUrl(state);
            case KAKAO -> buildKakaoAuthUrl(state);
            default -> throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }

    @Override
    public OAuthLoginRes processOAuthCallback(AuthProvider provider, String code, String state) {
        // state 검증
        validateState(state);

        // 1. Authorization Code로 Access Token 발급
        String accessToken = getAccessToken(provider, code);

        // 2. Access Token으로 사용자 정보 조회
        Map<String, Object> attributes = fetchUserAttributes(provider, accessToken);

        // 3. OAuth2UserInfo로 변환
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);

        // 4. 사용자 처리
        return processOAuthUser(userInfo);
    }

    private void validateState(String state) {
        if (state == null || state.isBlank()) {
            throw new BusinessException(AuthErrorCode.INVALID_OAUTH_STATE);
        }

        Long createdTime = stateStore.remove(state);  // 사용 후 삭제

        if (createdTime == null) {
            throw new BusinessException(AuthErrorCode.INVALID_OAUTH_STATE);
        }

        // 만료 체크 (5분)
        if (System.currentTimeMillis() - createdTime > STATE_EXPIRY_MS) {
            throw new BusinessException(AuthErrorCode.OAUTH_STATE_EXPIRED);
        }
    }

    private OAuthLoginRes processOAuthUser(OAuth2UserInfo userInfo) {
        String email = userInfo.getEmail();
        if (email == null || email.isBlank()) {
            throw new BusinessException(AuthErrorCode.OAUTH_EMAIL_NOT_PROVIDED);
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        // 기존 유저가 있고, Provider가 다르면 에러
        if (existingUser.isPresent() && existingUser.get().getProvider() != userInfo.getProvider()) {
            throw new BusinessException(AuthErrorCode.OAUTH_EMAIL_ALREADY_REGISTERED);
        }

        boolean isNewUser = existingUser.isEmpty();
        User user = existingUser.orElseGet(() -> createNewOAuthUser(userInfo));

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user, userInfo.getProvider());
        String refreshToken = jwtTokenProvider.createRefreshToken(user, userInfo.getProvider());

        // RefreshToken 저장
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new OAuthLoginRes(accessToken, refreshToken, isNewUser, user.getId());
    }

    private User createNewOAuthUser(OAuth2UserInfo userInfo) {
        User newUser = User.createSocialUser(
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
        return userRepository.save(newUser);
    }

    private String getAccessToken(AuthProvider provider, String code) {
        String tokenUrl = getTokenUrl(provider);
        MultiValueMap<String, String> params = buildTokenRequestParams(provider, code);

        try {
            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }
            throw new BusinessException(AuthErrorCode.OAUTH_COMMUNICATION_ERROR);
        } catch (WebClientResponseException e) {
            log.error("OAuth token fetch failed: status={}, provider={}", e.getStatusCode(), provider);
            throw new BusinessException(AuthErrorCode.OAUTH_COMMUNICATION_ERROR);
        }
    }

    private Map<String, Object> fetchUserAttributes(AuthProvider provider, String accessToken) {
        String userInfoUrl = getUserInfoUrl(provider);

        try {
            Map<String, Object> attributes = webClient.get()
                    .uri(userInfoUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            if (attributes == null) {
                throw new BusinessException(AuthErrorCode.OAUTH_USER_INFO_NOT_FOUND);
            }
            return attributes;
        } catch (WebClientResponseException e) {
            log.error("OAuth user info fetch failed: {}", e.getMessage());
            throw new BusinessException(AuthErrorCode.OAUTH_USER_INFO_NOT_FOUND);
        } catch (Exception e) {
            log.error("OAuth communication error: {}", e.getMessage());
            throw new BusinessException(AuthErrorCode.OAUTH_COMMUNICATION_ERROR);
        }
    }

    @Override
    public void completeSignup(Long userId, OAuthSignupReq request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.createName(request.name());
        user.updateNickname(request.nickname());

        userRepository.save(user);
    }

    // ========== URL Builders ==========

    private String getUserInfoUrl(AuthProvider provider) {
        return switch (provider) {
            case GOOGLE -> "https://www.googleapis.com/oauth2/v3/userinfo";
            case NAVER -> "https://openapi.naver.com/v1/nid/me";
            case KAKAO -> "https://kapi.kakao.com/v2/user/me";
            default -> throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }

    private String getTokenUrl(AuthProvider provider) {
        return switch (provider) {
            case GOOGLE -> "https://oauth2.googleapis.com/token";
            case NAVER -> "https://nid.naver.com/oauth2.0/token";
            case KAKAO -> "https://kauth.kakao.com/oauth/token";
            default -> throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }

    private MultiValueMap<String, String> buildTokenRequestParams(AuthProvider provider, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);

        switch (provider) {
            case GOOGLE -> {
                params.add("client_id", oAuth2Properties.getGoogle().getClientId());
                params.add("client_secret", oAuth2Properties.getGoogle().getClientSecret());
                params.add("redirect_uri", oAuth2Properties.getGoogle().getRedirectUri());
            }
            case NAVER -> {
                params.add("client_id", oAuth2Properties.getNaver().getClientId());
                params.add("client_secret", oAuth2Properties.getNaver().getClientSecret());
                params.add("redirect_uri", oAuth2Properties.getNaver().getRedirectUri());
            }
            case KAKAO -> {
                params.add("client_id", oAuth2Properties.getKakao().getClientId());
                params.add("client_secret", oAuth2Properties.getKakao().getClientSecret());
                params.add("redirect_uri", oAuth2Properties.getKakao().getRedirectUri());
            }
            default -> throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        }

        return params;
    }

    private String buildGoogleAuthUrl(String state) {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", oAuth2Properties.getGoogle().getClientId())
                .queryParam("redirect_uri", oAuth2Properties.getGoogle().getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("state", state)
                .build()
                .encode()
                .toUriString();
    }

    private String buildNaverAuthUrl(String state) {
        return UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("client_id", oAuth2Properties.getNaver().getClientId())
                .queryParam("redirect_uri", oAuth2Properties.getNaver().getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    private String buildKakaoAuthUrl(String state) {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", oAuth2Properties.getKakao().getClientId())
                .queryParam("redirect_uri", oAuth2Properties.getKakao().getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .build()
                .toUriString();
    }
}
