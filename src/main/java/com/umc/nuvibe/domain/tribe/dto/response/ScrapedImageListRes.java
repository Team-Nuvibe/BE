package com.umc.nuvibe.domain.tribe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "전체 스크랩 목록 조회 응답 (무한 스크롤)")
public record ScrapedImageListRes(

        @Schema(description = "정렬된 스크랩 이미지 목록 (flat List)")
        List<ScrapedImageInfoRes> items,

        @Schema(description = "다음 페이지 커서(createdAt). hasNext=false면 null")
        LocalDateTime nextCursorCreatedAt,

        @Schema(description = "다음 페이지 커서(scrapId). hasNext=false면 null")
        Long nextCursorId,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
}
