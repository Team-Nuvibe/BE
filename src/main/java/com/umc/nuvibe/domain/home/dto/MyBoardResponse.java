package com.umc.nuvibe.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 나의 기록(보드) 응답 DTO
 * 최대 5개, 오래된 이미지 업로드 보드 순
 */
@Schema(description = "나의 보드 응답")
public record MyBoardResponse(

        @Schema(description = "보드 ID") Long boardId,

        @Schema(description = "보드 이름") String name,

        @Schema(description = "썸네일 URL (가장 최신 이미지)") String thumbnailUrl) {
}
