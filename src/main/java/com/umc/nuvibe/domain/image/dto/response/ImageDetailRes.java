package com.umc.nuvibe.domain.image.dto.response;

import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "이미지 정보 응답")
public record ImageDetailResponse(
        String userName,
        String boardName,
        ImageTag imageTag,
        String imageUrl,
        LocalDateTime createdAt
) {
    public static ImageDetailResponse from(Image image, User user, ArchiveBoard board) {
        return new ImageDetailResponse(
                user.getName(),
                board.getName(),
                image.getImageTag(),
                image.getImageUrl(),
                image.getCreatedAt()
        );
    }
}

