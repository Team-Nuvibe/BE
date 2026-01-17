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
        LocalDateTime lastCreatedAt,

        @Schema(description = "마지막 데이터의 ID (보조 커서)", example = "1")
        Long lastId,

        @Schema(description = "조회 개수 (3열 그리드 권장: 30)", example = "30")
        @Min(0) @Max(90)
        Integer size
) {
    public enum SortType { LATEST, TAG }

    public ScrapedImageSliceReq {
        if (size == null) size = 30;

        if ((lastCreatedAt == null) != (lastId == null)) {
            throw new IllegalArgumentException("cursorCreatedAt과 cursorId는 함께 제공되거나 모두 null이어야 합니다.");
        }
    }
}
