package com.umc.nuvibe.domain.home.dto;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 소분류 태그 상세 응답 DTO
 */
@Schema(description = "태그 상세 응답")
public record TagDetailResponse(

                @Schema(description = "소분류 태그", example = "GRAIN") ImageTag tag,

                @Schema(description = "태그 설명") String description,

                @Schema(description = "대분류 카테고리", example = "MOOD") ImageTagCategory category,

                @Schema(description = "트라이브 채팅 이미지 URL 목록 (최대 5개)") List<String> tribeImageUrls,

                @Schema(description = "트라이브 채팅 이미지 존재 여부") boolean hasImages,

                @Schema(description = "트라이브 ID (없으면 null)") Long tribeId) {
}
