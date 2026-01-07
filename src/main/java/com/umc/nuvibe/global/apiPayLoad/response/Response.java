package com.umc.nuvibe.global.apiPayLoad.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import com.umc.nuvibe.global.apiPayLoad.result.CommonResultCode;
import com.umc.nuvibe.global.apiPayLoad.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status","code","message","data"})
public class Response <T> {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public static Response<Void> ok() {
        return new Response<>(CommonResultCode.OK.getHttpStatus(), CommonResultCode.OK.getCode(), CommonResultCode.OK.getMessage(),null  );
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(CommonResultCode.OK.getHttpStatus(),CommonResultCode.OK.getCode(), CommonResultCode.OK.getMessage(),data);
    }

    public static <T>Response<T> of(ErrorCode code, T data ) {
        return new Response<>(code.getHttpStatus(), code.getCode(), code.getMessage(), data);
    }


    public static <T>Response<T> of(ResultCode code, T data ) {
        return new Response<>(code.getHttpStatus(), code.getCode(), code.getMessage(), data);
    }

    public static <T>Response<T> fail(ErrorCode code, T data ) {
        return new Response<>(code.getHttpStatus(),code.getCode(), code.getMessage(), data);
    }

    public static <T>Response<T> fail(ErrorCode code) {
        return new Response<>(code.getHttpStatus(),code.getCode(), code.getMessage(), null);
    }
}
