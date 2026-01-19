package com.umc.nuvibe.domain.tribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Schema(description = "트라이브 채팅 이미지 그리드 조회 요청 (커서 기반)")
public record ChatGridReq(

        @Schema(description = "마지막 데이터의 생성 시각 (커서)")
        LocalDateTime cursorCreatedAt,

        @Schema(description = "마지막 데이터의 ID (보조 커서)", example = "1")
        Long cursorChatId,

        @Schema(description = "조회 개수 (기본 30, 최대 60)")
        @Min(1) @Max(60)
        Integer size
) {
    private static final int DEFAULT_SIZE = 30;

    public ChatGridReq {
        size = (size == null) ? DEFAULT_SIZE : size;
    }

    public boolean hasCursor() {
        return cursorCreatedAt != null || cursorChatId != null;
    }

    public boolean isCursorComplete() {
        return cursorCreatedAt != null && cursorChatId != null;
    }

}
