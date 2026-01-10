package com.umc.nuvibe.domain.user.controller;

import com.umc.nuvibe.domain.user.dto.request.AuthRequest;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import com.umc.nuvibe.domain.user.service.AuthService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ResultCode;
import com.umc.nuvibe.global.apiPayLoad.result.UserResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
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
    public Response<String> signUp(@RequestBody @Valid AuthRequest.SignUpReq request) {
        authService.signUp(request);
        return Response.ok(UserResultCode.USER_SIGNUP_OK, "회원가입이 완료되었습니다");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인시 엑세스 토큰과 리프레쉬 토큰을 발급합니다.")
    public Response<TokenRes> login(@RequestBody @Valid AuthRequest.LoginReq request) {
        TokenRes response= authService.login(request);
        return Response.ok(UserResultCode.USER_LOGIN_OK, response);
    }

    @PatchMapping("/logout")
    @Operation(summary = "로그아웃", description = "db의 리프레쉬 토큰을 무효화합니다")
    public Response<String> logout(@AuthUser Long userId) {
        authService.logout(userId);
        return Response.ok(UserResultCode.USER_LOGOUT_OK, "로그아웃이 완료되었습니다");
    }

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "새로운 회원 등록")
    public Response<String> withdraw (@AuthUser Long userId) {
        authService.withdraw(userId);
        return Response.ok(UserResultCode.USER_WITHDRAW_OK, "회원탈퇴가 완료되었습니다");
    }
}
