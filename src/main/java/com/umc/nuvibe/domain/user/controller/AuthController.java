package com.umc.nuvibe.domain.user.controller;

import com.umc.nuvibe.domain.user.dto.request.CheckPasswordReq;
import com.umc.nuvibe.domain.user.dto.request.EmailVerificationReq;
import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import com.umc.nuvibe.domain.user.service.AuthService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.UserResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name="Auth", description = "로그인 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "새로운 회원 등록")
    public Response<String> signUp(@RequestBody @Valid SignUpReq request) {
        authService.signUp(request);
        return Response.ok(UserResultCode.USER_SIGNUP_OK, "회원가입이 완료되었습니다. ");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인시 엑세스 토큰과 리프레쉬 토큰을 발급합니다.")
    public Response<TokenRes> login(@RequestBody @Valid LoginReq request) {
        TokenRes response= authService.login(request);
        return Response.ok(UserResultCode.USER_LOGIN_OK, response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "db의 리프레쉬 토큰을 무효화합니다.")
    public Response<String> logout(@AuthUser Long userId) {
        authService.logout(userId);
        return Response.ok(UserResultCode.USER_LOGOUT_OK, "로그아웃이 완료되었습니다. ");
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원탈퇴", description = "회원을 삭제합니다.")
    public Response<String> withdraw (@AuthUser Long userId) {
        authService.withdraw(userId);
        return Response.ok(UserResultCode.USER_WITHDRAW_OK, "회원탈퇴가 완료되었습니다. ");
    }

    @PostMapping("/verify-email")
    @Operation(summary = "이메일 인증 발송", description = "회원가입을 위한 인증 이메일을 발송합니다.")
    public Response<String> sendJoinVerificationEmail(@RequestBody @Valid EmailVerificationReq request) {
        authService.sendJoinVerificationEmail(request.email());
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_SENT, "이메일 인증이 발송되었습니다.");
    }

    @GetMapping("/verify")
    @Operation(summary = "이메일 인증", description = "이메일 인증 링크를 처리합니다. 인증 완료 후 해당 페이지로 리다이렉트합니다.")
    public Response<String> verifyEmail(
            @RequestParam String token,
            HttpServletResponse response) throws IOException {
        authService.verifyJoinEmailAndRedirect(token, response);
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_OK,"이메일 인증 처리 완료, 이전 페이지로 리다이렉트합니다.");
    }

    @GetMapping("check-password")
    @Operation(summary = "사용자의 현재 비밀번호를 확인합니다.", description = "비번 변경 전 현재 비밀번호 확인할 때 이 api 사용하면 됩니다.")
    public Response<String> checkCurrentPassword(@AuthUser Long userId, @RequestBody @Valid CheckPasswordReq request) {
        authService.checkCurrentPassword(userId, request);
        return Response.ok(UserResultCode.USER_CURRENT_PASSWORD_CHECK_OK,"현재 비밀번호와 일치합니다.");
    }
}

