package com.umc.nuvibe.domain.user.controller;

import com.umc.nuvibe.domain.user.dto.request.*;
import com.umc.nuvibe.domain.user.dto.response.UserProfileImageRes;
import com.umc.nuvibe.domain.user.dto.response.UserSettingUpdateRes;
import com.umc.nuvibe.domain.user.service.UserService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.UserResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile-image")
    @Operation(summary = "프로필 이미지 조회", description = "유저의 프로필 이미지를 조회합니다.")
    public Response<UserProfileImageRes> getUserProfileImage(@AuthUser Long userId) {
        UserProfileImageRes response = userService.getUserProfileImage(userId);
        return Response.ok(UserResultCode.USER_PROFILE_IMAGE_GET_OK, response);
    }

    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 수정", description = "유저의 프로필 이미지를 수정합니다.")
    public Response<String> updateProfileImage(
            @AuthUser Long userId,
            @RequestPart("file") MultipartFile file) {
        userService.updateProfileImage(userId, file);
        return Response.ok(UserResultCode.USER_PROFILE_IMAGE_UPDATE_OK, "프로필 이미지가 수정되었습니다.");
    }

    @PatchMapping("/nickname")
    @Operation(summary = "닉네임 수정", description = "유저의 닉네임을 수정합니다.")
    public Response<String> updateUserNickname(
            @AuthUser Long userId,
            @RequestBody @Valid NicknameUpdateReq request) {
        userService.updateUserNickname(userId, request.nickname());
        return Response.ok(UserResultCode.USER_NICKNAME_UPDATE_OK, "닉네임이 수정되었습니다.");
    }

    @PostMapping("/email/request")
    @Operation(summary = "이메일 변경 요청", description = "이메일 변경을 위한 인증 이메일을 발송합니다.")
    public Response<String> requestEmailUpdate(
            @AuthUser Long userId,
            @RequestBody @Valid EmailVerificationReq request) {
        userService.requestEmailUpdate(userId, request.email());
        return Response.ok(UserResultCode.USER_EMAIL_UPDATE_REQUEST_OK, "이메일 변경 요청이 발송되었습니다.");
    }

    @GetMapping("/email/verify")
    @Operation(summary = "이메일 변경 인증", description = "이메일 변경용 인증 링크를 처리하고 설정 페이지로 리다이렉트합니다.")
    public Response<String> verifyAndUpdateEmail(
            @AuthUser Long userId,
            @RequestParam String token,
            HttpServletResponse response) throws IOException {
        userService.verifyAndUpdateEmailWithRedirect(userId, token, response);
        return Response.ok(UserResultCode.USER_EMAIL_VERIFICATION_OK,"이메일 변경 인증 완료 후 리다이렉트 합니다.");
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 재설정", description = "유저의 비밀번호를 재설정합니다. 사용자의 현재 비번 확인 후 이 api 호출하시면 됩니다.")
    public Response<String> reissuePassword(
            @AuthUser Long userId,
            @RequestBody @Valid ReissuePasswordReq request) {

        userService.reissuePassword(userId, request);
        return Response.ok(UserResultCode.USER_PASSWORD_REISSUE_OK, "비밀번호가 재설정되었습니다.");
    }

    @PatchMapping("/settings")
    @Operation(summary = "유저 설정 변경", description = "유저의 알림 설정을 변경합니다.")
    public Response<UserSettingUpdateRes> updateSetting(
            @AuthUser Long userId,
            @RequestBody @Valid UserSettingReq request) {
        UserSettingUpdateRes response= userService.updateSetting(userId, request);
        return Response.ok(UserResultCode.USER_SETTING_UPDATE_OK, response);
    }
}

