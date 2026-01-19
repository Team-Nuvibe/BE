package com.umc.nuvibe.domain.tribe.dto.request;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Schema(description = "스크랩한 이미지 목록 요청")
public record ScrapedImageSliceReq(

        @Schema(description = "필터링할 태그 (null이면 전체 최신순 조회)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "마지막 데이터의 생성 시각 (커서)")
        LocalDateTime cursorCreatedAt,

        @Schema(description = "마지막 데이터의 ID (보조 커서)", example = "1")
        Long cursorId,

        @Schema(description = "조회 개수 (기본 30, 최대 60)", example = "30")
        @Min(1) @Max(60)
        Integer size
) {
    private static final int DEFAULT_SIZE = 30;

    public ScrapedImageSliceReq {
        size = (size == null) ? DEFAULT_SIZE : size;
    }

    public boolean hasCursor() {
        return cursorCreatedAt != null || cursorId != null;
    }

    public boolean isCursorComplete() {
        return cursorCreatedAt != null && cursorId != null;
    }
}
