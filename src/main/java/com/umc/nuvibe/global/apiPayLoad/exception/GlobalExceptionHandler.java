package com.umc.nuvibe.global.apiPayLoad.exception;

import com.umc.nuvibe.global.apiPayLoad.error.CommonErrorCode;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // ===================== 사용자 정의 예외 ======================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<Object>> handleBusinessException(BusinessException ex) {
        if (ex.getData() != null) {
            return ResponseEntity
                    .status(ex.getHttpStatus())
                    .body(Response.fail(ex.getErrorCode(), ex.getData()));
        }
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(Response.fail(ex.getErrorCode()));
    }

    @ExceptionHandler(GlobalExeception.class)
    public ResponseEntity<Response<Void>> handleGlobalException(GlobalExeception ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(Response.fail(ex.getErrorCode()));
    }

    // ===================== Validation / 변환 오류 ======================

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.fail(CommonErrorCode.BAD_REQUEST, errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("Message not readable: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.fail(CommonErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            DataIntegrityViolationException.class,
            IllegalArgumentException.class,
            IncorrectResultSizeDataAccessException.class,
            InvalidDataAccessApiUsageException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<Object> handleBadRequestExceptions(Exception ex) {
        log.warn("Bad request exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.fail(CommonErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    // ===================== 시스템 오류 (500) ======================

    @ExceptionHandler({
            BeanCreationException.class,
            ClassCastException.class,
            HttpMessageConversionException.class,
            JpaSystemException.class,
            NullPointerException.class,
            UnsatisfiedDependencyException.class
    })
    public ResponseEntity<Object> handleServerExceptions(Exception ex) {
        log.error("Internal server error: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.fail(CommonErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    // ===================== 예기치 못한 예외 ======================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleUnknownException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

}
