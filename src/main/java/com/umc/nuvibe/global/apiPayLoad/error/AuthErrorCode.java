package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"AUTH001", "만료된 jwt 토큰입니다."),
    JWT_INVALID_TOKEN(HttpStatus.BAD_REQUEST,"AUTH002","유효하지 않은 jwt 토큰입니다."),
    JWT_GENERATED_FAILED(HttpStatus.BAD_REQUEST,"AUTH003", "jwt 토큰 생성 실패했습니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"AUTH004", "jwt 토큰ㅇ르 찾을 수 없습니다."),
    AUTHORIZATION_HEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH005", "autorization header를 찾을 수 없습니다"),
    INVALID_AUTHORIZATION_FORMAT(HttpStatus.BAD_REQUEST, "AUTH006", "유효하지 않은 헤더 포맷입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"AUTH007","비밀번호 형식이 일치하지 않습니다."),
    CONFIRM_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST,"AUTH008","비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVAILD_EMAIL_FORMAT(HttpStatus.BAD_REQUEST,"AUTH009","이메일 형식이 일치하지 않습니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"AUTH010","이미 존재하는 이메일입니다."),
    PASSWORD_UNMATCH_ERROR(HttpStatus.BAD_REQUEST,"AUTH011", "비밀번호가 일치하지 않습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH012", "지원하지 않는 소셜 로그인 제공자입니다."),
    OAUTH_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH013", "소셜 로그인 서버와 통신 중 오류가 발생했습니다."),
    OAUTH_USER_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH014", "소셜 로그인 사용자 정보를 가져올 수 없습니다."),
    OAUTH_EMAIL_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "AUTH015", "소셜 계정에서 이메일 정보를 제공받지 못했습니다."),
    OAUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH016", "인증이 필요합니다."),
    OAUTH_EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT, "AUTH017", "이미 다른 방식으로 가입된 이메일입니다."),
    INVALID_OAUTH_STATE(HttpStatus.BAD_REQUEST, "AUTH018", "유효하지 않은 OAuth state입니다."),
    OAUTH_STATE_EXPIRED(HttpStatus.BAD_REQUEST, "AUTH019", "OAuth 인증이 만료되었습니다. 다시 시도해주세요.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
