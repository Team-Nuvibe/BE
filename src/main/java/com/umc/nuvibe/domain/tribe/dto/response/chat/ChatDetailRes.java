package com.umc.nuvibe.domain.tribe.dto.response.chat;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "채팅 이미지 상세 조회 응답")
public record ChatDetailRes (

        @Schema(description = "채팅 ID")
        Long chatId,

        @Schema(description = "이미지 ID")
        Long imageId,

        @Schema(description = "이미지 URL (S3 경로)")
        String imageUrl,

        @Schema(description = "이미지 태그 (Enum)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "이미지 업로드 시각 (채팅 생성 시각)")
        LocalDateTime createdAt,

        @Schema(description = "현재 사용자가 해당 이미지를 스크랩했는지 여부", example = "true")
        boolean isScraped
){
    public static ChatDetailRes from(Chat chat, boolean isScraped) {
        Image image = chat.getImage();
        return new ChatDetailRes(
                chat.getId(),
                image.getId(),
                image.getImageUrl(), // 상세 조회는 원본 URL 반환
                image.getImageTag(),
                chat.getCreatedAt(),
                isScraped
        );
    }
}
