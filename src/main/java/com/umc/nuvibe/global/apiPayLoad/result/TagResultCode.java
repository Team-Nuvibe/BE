package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TagResultCode implements ResultCode {

    TAG_SEARCH_OK(HttpStatus.OK, "TAG001", "태그 검색 성공"),
    TAG_FIND_OK(HttpStatus.OK, "TAG002", "태그 호출 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}




