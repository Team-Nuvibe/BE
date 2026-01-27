package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationResultCode implements ResultCode {

    NOTIFICATION_LIST_SUCCESS(HttpStatus.OK, "NOTI001", "알림 목록 조회 성공"),
    NOTIFICATION_READ_SUCCESS(HttpStatus.OK, "NOTI002", "알림 읽음 처리 성공"),
    NOTIFICATION_DELETE_SUCCESS(HttpStatus.OK, "NOTI003", "알림 삭제 성공"),
    FCM_TOKEN_REGISTERED(HttpStatus.OK, "NOTI004", "FCM 토큰이 등록되었습니다.");  // [추가]

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
