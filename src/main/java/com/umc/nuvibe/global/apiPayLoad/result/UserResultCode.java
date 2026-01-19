package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserResultCode implements ResultCode {

    USER_SIGNUP_OK(HttpStatus.CREATED,"USER001", "유저 회원가입 성공"),
    USER_LOGIN_OK(HttpStatus.CREATED,"USER002", "유저 로그인 성공"),
    USER_LOGOUT_OK(HttpStatus.CREATED,"USER003", "유저 로그아웃 성공"),
    USER_WITHDRAW_OK(HttpStatus.CREATED,"USER004", "유저 회원탈퇴 성공"),
    USER_EMAIL_VERIFICATION_SENT(HttpStatus.OK,"USER005", "이메일 인증 발송 성공"),
    USER_PROFILE_IMAGE_GET_OK(HttpStatus.OK,"USER006", "프로필 이미지 조회 성공"),
    USER_PROFILE_IMAGE_UPDATE_OK(HttpStatus.OK,"USER007", "프로필 이미지 수정 성공"),
    USER_NICKNAME_UPDATE_OK(HttpStatus.OK,"USER008", "닉네임 수정 성공"),
    USER_EMAIL_UPDATE_REQUEST_OK(HttpStatus.OK,"USER009", "이메일 변경 요청 성공"),

    USER_CURRENT_PASSWORD_CHECK_OK(HttpStatus.OK, "USER010","현재 비밀번호 확인 성공"),
    USER_PASSWORD_REISSUE_OK(HttpStatus.OK,"USER011", "비밀번호 재설정 성공"),
    USER_SETTING_UPDATE_OK(HttpStatus.OK,"USER012", "유저 설정 변경 성공"),
    USER_EMAIL_VERIFICATION_OK(HttpStatus.OK,"USER013", "이메일 인증 완료 성공"),
    USER_EMAIL_UPDATE_OK(HttpStatus.OK,"USER014", "이메일 변경 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
