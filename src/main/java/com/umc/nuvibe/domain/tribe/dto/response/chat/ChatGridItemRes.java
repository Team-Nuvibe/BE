package com.umc.nuvibe.domain.tribe.dto.response.chat;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "채팅방 이미지 그리드 전체 조회를 위한 개별 이미지 정보")
public record ChatGridItemRes(

        @Schema(description = "채팅 ID")
        Long chatId,

        @Schema(description = "이미지 ID")
        Long imageId,

        @Schema(description = "이미지 URL (S3 경로)")
        String imageUrl,

        @Schema(description = "이미지 업로드 시각 (정렬/날짜 섹션 기준)")
        LocalDateTime createdAt
) {
    public static ChatGridItemRes from(Chat chat) {
        Image image = chat.getImage();
        return new ChatGridItemRes(
                chat.getId(),
                image.getId(),
                image.getImageUrl(),
                chat.getCreatedAt()
        );
    }
}
