package com.umc.nuvibe.domain.user.controller;

import com.umc.nuvibe.domain.user.dto.request.*;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import com.umc.nuvibe.domain.user.service.AuthService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.UserResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/verify-code/send")
    @Operation(summary = "회원가입 인증 코드 발송", description = "회원가입을 위한 6자리 인증 코드를 이메일로 발송합니다.")
    public Response<String> sendJoinVerificationCode(@RequestBody @Valid EmailVerificationReq request) {
        authService.sendJoinVerificationCode(request.email());
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_SENT, "인증 코드가 이메일로 발송되었습니다.");
    }

    @PostMapping("/verify-code/confirm")
    @Operation(summary = "회원가입 인증 코드 검증", description = "이메일로 받은 6자리 인증 코드를 검증합니다.")
    public Response<String> verifyJoinCode(@RequestBody @Valid VerifyCodeReq request) {
        authService.verifyJoinCode(request.email(), request.code());
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_OK, "이메일 인증이 완료되었습니다.");
    }


    @PostMapping("/password-reset/send-code")
    @Operation(summary = "비밀번호 초기화용 인증 코드 발송", description = "비밀번호 재설정을 위한 6자리 인증 코드를 이메일로 발송합니다. ")
    public Response<String> sendPasswordResetCode(@RequestBody @Valid PasswordResetEmailReq request) {
        authService.sendPasswordResetCode(request.email());
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_SENT, "비밀번호 재설정 인증 코드가 이메일로 발송되었습니다.");
    }

    @PostMapping("/password-reset/verify-code")
    @Operation(summary = "비밀번호 초기화용 인증 코드 검증", description = "비밀번호 재설정 인증 코드를 검증합니다.")
    public Response<String> verifyPasswordResetCode(@RequestBody @Valid VerifyCodeReq request) {
        authService.verifyPasswordResetCode(request.email(), request.code());
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_OK, "인증 코드가 확인되었습니다. 새로운 비밀번호를 입력해주세요.");
    }

    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 초기화", description = "인증된 이메일의 비밀번호를 새로운 비밀번호로 변경합니다. 기존 세션은 모두 무효화됩니다.")
    public Response<String> resetPassword(@RequestBody @Valid PasswordResetReq request) {
        authService.resetPasswordWithCode(
                request.email(),
                request.code(),
                request.newPassword(),
                request.confirmPassword()
        );
        return Response.ok(UserResultCode.USER_PASSWORD_REISSUE_OK, "비밀번호가 재설정되었습니다. 새로운 비밀번호로 로그인해주세요.");
    }


    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "RefreshToken으로 새로운 AccessToken과 RefreshToken을 발급받습니다. 헤더에 refreshToekn을 담아주세요..")
    public Response<TokenRes> reissueToken(@RequestHeader("Authorization") String authorizationHeader) {
        TokenRes response = authService.reissueToken(authorizationHeader);
        return Response.ok(UserResultCode.USER_TOKEN_REISSUE_OK, response);
    }


    @PostMapping("/check-password")
    @Operation(summary = "사용자의 현재 비밀번호를 확인합니다.", description = "비번 변경 전 현재 비밀번호 확인할 때 이 api 사용하면 됩니다.")
    public Response<String> checkCurrentPassword(@AuthUser Long userId, @RequestBody @Valid CheckPasswordReq request) {
        authService.checkCurrentPassword(userId, request);
        return Response.ok(UserResultCode.USER_CURRENT_PASSWORD_CHECK_OK,"현재 비밀번호와 일치합니다.");
    }
}
