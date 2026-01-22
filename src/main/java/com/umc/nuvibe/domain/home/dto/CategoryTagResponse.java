package com.umc.nuvibe.domain.home.dto;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 대분류 카테고리 내 소분류 태그 응답 DTO
 * 태그 + 대표 이미지 1개
 */
@Schema(description = "카테고리별 태그 응답")
public record CategoryTagResponse(

                @Schema(description = "소분류 태그", example = "BLUR") ImageTag tag,

                @Schema(description = "대표 이미지 URL") String imageUrl) {
}
