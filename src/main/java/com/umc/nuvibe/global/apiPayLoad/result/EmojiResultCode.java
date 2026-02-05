package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmojiResultCode implements ResultCode {

    EMOJI_REACT_SUCCESS(HttpStatus.OK, "EMOJI001", "이모지 반응 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
