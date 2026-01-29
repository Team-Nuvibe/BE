package com.umc.nuvibe.domain.tribe.dto.request;

import com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeCursor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Active 트라이브 챗 목록 조회 요청")
public record ActiveTribeListReq(

        @Schema(description = "Active 트라이브 챗 목록 커서 ")
        ActiveTribeCursor cursor,

        @Schema(description = "조회할 트라이브 챗 수 (기본 20, 최대 40)")
        @Min(1) @Max(40)
        Integer size
) {
    private static final int DEFAULT_SIZE = 20;

    public ActiveTribeListReq {
        size = (size == null) ? DEFAULT_SIZE : size;
    }

    // 유효한 커서 요청인지 확인
    public boolean hasCursor() {
        return cursor != null && cursor.isComplete();
    }
}
