package com.umc.nuvibe.domain.user.controller;

import com.umc.nuvibe.domain.user.dto.request.OAuthSignupReq;
import com.umc.nuvibe.domain.user.dto.response.OAuthLoginRes;
import com.umc.nuvibe.domain.user.service.OAuthService;
import com.umc.nuvibe.domain.user.vo.AuthProvider;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.UserResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "소셜 로그인 API")
public class OAuthController {

    private final OAuthService oAuthService;

    @Value("${frontend.url}")
    private String frontendUrl;

    // 소셜 로그인 페이지로 리다이렉트
    @GetMapping("/{provider}")
    @Operation(summary = "소셜 로그인 시작", description = "해당 소셜 서비스의 로그인 페이지로 리다이렉트합니다.(google, naver, kakao)")
    public ResponseEntity<Void> redirectToOAuth(@PathVariable String provider) {
        AuthProvider authProvider;
        try {
            authProvider = AuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER); //provider 예외
        }

        String state = UUID.randomUUID().toString();
        String authUrl = oAuthService.getOAuthAuthorizationUrl(authProvider, state);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }

    // OAuth Callback 처리
    @GetMapping("/callback/{provider}")
    @Operation(summary = "OAuth Callback", description = "소셜 서비스 인증 후 콜백을 처리합니다.")
    public ResponseEntity<Void> handleOAuthCallback(
            @PathVariable String provider,
            @RequestParam String code,
            @RequestParam(required = false) String state) {

        AuthProvider authProvider;
        try {
            authProvider = AuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        }

        OAuthLoginRes response = oAuthService.processOAuthCallback(authProvider, code, state);  // state 전달

        // Fragment identifier 방식으로 변경 (# 사용)
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/oauth/callback")
                .build()
                .toUriString()
                + "#accessToken=" + response.accessToken()
                + "&refreshToken=" + response.refreshToken()
                + "&isNewUser=" + response.isNewUser()
                + "&userId=" + response.userId();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/signup/complete")
    @Operation(summary = "소셜 회원가입 완료", description = "신규 소셜 유저가 추가 정보를 입력합니다.")
    public ResponseEntity<?> completeOAuthSignup(
            @AuthenticationPrincipal Long userId,
            @RequestBody OAuthSignupReq request) {

        if (userId == null) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);  // 인증 필요
        }

        // userDetails에서 유저 정보 가져와서 업데이트
        oAuthService.completeSignup(userId, request);

        return ResponseEntity.ok(Response.ok(UserResultCode.USER_SIGNUP_OK));
    }
}
