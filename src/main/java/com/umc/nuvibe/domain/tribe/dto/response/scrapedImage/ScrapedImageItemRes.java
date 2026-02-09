package com.umc.nuvibe.domain.tribe.dto.response.scrapedImage;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "전체 스크랩 목록 조회를 위한 개별 이미지 정보")
public record ScrapedImageItemRes(

        @Schema(description = "스크랩 이미지 ID")
        Long scrapImageId,

        @Schema(description = "이미지 ID")
        Long imageId,

        @Schema(description = "이미지 URL (S3 경로)")
        String imageUrl,

        @Schema(description = "이미지 태그 (Enum)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "스크랩 생성 일시 (정렬/날짜 섹션 기준)")
        LocalDateTime createdAt,

        @Schema(description = "채팅 ID")
        Long chatId,

        @Schema(description = "채팅 작성자 ID")
        Long chatSenderId,

        @Schema(description = "채팅 작성자 닉네임")
        String chatSenderNickname
) {
}
