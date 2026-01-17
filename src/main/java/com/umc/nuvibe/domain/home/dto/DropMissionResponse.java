package com.umc.nuvibe.domain.home.dto;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 오늘의 드롭미션 응답 DTO
 * 소분류 해시태그 + 이미지 1개를 랜덤으로 반환
 */
@Schema(description = "오늘의 드롭미션 응답")
public record DropMissionResponse(

                @Schema(description = "소분류 태그", example = "MINIMAL") ImageTag tag,

                @Schema(description = "이미지 ID") Long imageId,

                @Schema(description = "이미지 URL") String imageUrl) {
}
