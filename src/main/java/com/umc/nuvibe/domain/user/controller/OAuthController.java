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
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
    // 소셜 로그인 페이지로 리다이렉트
    @GetMapping("/{provider}")
    @Operation(summary = "소셜 로그인 시작", description = "해당 소셜 서비스의 로그인 페이지로 리다이렉트합니다.(google, naver, kakao)")
    public ResponseEntity<Void> redirectToOAuth(
            @PathVariable String provider,
            @RequestParam(required = false) String redirect_uri) {  // 추가

        AuthProvider authProvider;
        try {
            authProvider = AuthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        }

        String state = UUID.randomUUID().toString();

        // redirect_uri 저장 (추가)
        if (redirect_uri != null) {
            oAuthService.saveRedirectUri(state, redirect_uri);
        }

        String authUrl = oAuthService.getOAuthAuthorizationUrl(authProvider, state);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }

    // OAuth Callback 처리
    @GetMapping("/callback/{provider}")
    public ResponseEntity<Void> handleOAuthCallback(
            @PathVariable String provider,
            @RequestParam String code,
            @RequestParam(required = false) String state) {

        // ✅ 에러 발생 시에도 리다이렉트하기 위해 targetUrl을 먼저 결정
        String targetUrl = oAuthService.getRedirectUri(state, frontendUrl);

        try {
            AuthProvider authProvider;
            try {
                authProvider = AuthProvider.valueOf(provider.toUpperCase());
            } catch (IllegalArgumentException e) {
                return buildErrorRedirect(targetUrl,
                        AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER.getCode(),  // AUTH012
                        AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER.getMessage());
            }

            OAuthLoginRes response = oAuthService.processOAuthCallback(authProvider, code, state);

            String fragmentRaw = "accessToken=" + response.accessToken()
                    + "&refreshToken=" + response.refreshToken()
                    + "&isNewUser=" + response.isNewUser()
                    + "&userId=" + response.userId()
                    + "&email=" + response.email()
                    + "&provider=" + response.provider().name();

            String redirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .path("/oauth/callback")
                    .fragment(UriUtils.encodeFragment(fragmentRaw, StandardCharsets.UTF_8))
                    .build(true)
                    .toUriString();

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();

        } catch (BusinessException e) {
            // BusinessException → 프론트엔드로 에러와 함께 리다이렉트
            return buildErrorRedirect(targetUrl, e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            return buildErrorRedirect(targetUrl,
                    AuthErrorCode.OAUTH_UNKNOWN_ERROR.getCode(),
                    AuthErrorCode.OAUTH_UNKNOWN_ERROR.getMessage());
        }
    }


    // 에러 발생 시 프론트엔드로 에러 정보와 함께 리다이렉트
    private ResponseEntity<Void> buildErrorRedirect(String targetUrl, String errorCode, String errorMessage) {
        String fragmentRaw = "error=" + errorCode + "&errorMessage=" + errorMessage;

        String redirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .path("/oauth/callback")
                .fragment(UriUtils.encodeFragment(fragmentRaw, StandardCharsets.UTF_8))
                .build(true)
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/signup/complete")
    @Operation(summary = "소셜 회원가입 완료", description = "신규 소셜 유저가 추가 정보를 입력합니다.")
    public ResponseEntity<?> completeOAuthSignup(
            @AuthenticationPrincipal Long userId,
            @RequestBody OAuthSignupReq request) {

        // userDetails에서 유저 정보 가져와서 업데이트
        oAuthService.completeSignup(userId, request);

        return ResponseEntity.ok(Response.ok(UserResultCode.USER_SIGNUP_OK));
    }
}
