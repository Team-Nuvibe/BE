package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "전체 스크랩 목록 조회를 위한 개별 이미지 정보")
public record ScrapedImageInfoRes(

        @Schema(description = "스크랩 이미지 ID")
        Long scrapImageId,

        @Schema(description = "이미지 ID")
        Long imageId,

        @Schema(description = "이미지 URL (S3 경로)")
        String imageUrl,

        @Schema(description = "이미지 태그 (Enum)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "스크랩 생성 일시 (정렬/날짜 섹션 기준)")
        LocalDateTime createdAt
) {
        public static ScrapedImageInfoRes from(ScrapedImage scrap) {
                return new ScrapedImageInfoRes(
                        scrap.getId(),
                        scrap.getImage().getId(),
                        scrap.getImage().getImageUrl(),
                        scrap.getImage().getImageTag(),
                        scrap.getCreatedAt()
                );
        }
}
