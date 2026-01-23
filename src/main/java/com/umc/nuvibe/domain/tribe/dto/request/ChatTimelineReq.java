package com.umc.nuvibe.domain.tribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "트라이브 채팅 타임라인 조회 요청 (커서 기반)")
public record ChatTimelineReq(

        @Schema(description = "이전 페이지의 마지막 chatId (null이면 첫 페이지)")
        Long lastChatId,

        @Schema(description = "조회할 메시지 수 (기본 20, 최대 30)")
        @Min(1) @Max(30)
        Integer size
) {
    private static final int DEFAULT_SIZE = 20;

    public ChatTimelineReq {
        size = (size == null) ? DEFAULT_SIZE : size;
    }
}
