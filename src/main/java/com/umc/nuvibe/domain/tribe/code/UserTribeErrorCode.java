package com.umc.nuvibe.domain.tribe.code;

import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserTribeErrorCode implements ErrorCode {

    USERTRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "USERTRIBE4041", "해당 트라이브를 찾을 수 없습니다."),
    USERTRIBE_NOT_JOINED(HttpStatus.BAD_REQUEST, "USERTRIBE4001", "해당 트라이브 챗에 입장해있지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
