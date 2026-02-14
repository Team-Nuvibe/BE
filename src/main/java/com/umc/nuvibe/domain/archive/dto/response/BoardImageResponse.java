package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record BoardImageResponse(
    Long imageId,
    String imageUrl,
    ImageTag tag,
    String uploadedAt
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yy.MM.dd");

    public static BoardImageResponse from(BoardImage boardImage) {
        return new BoardImageResponse(
            boardImage.getImage().getId(),
            boardImage.getImage().getThumbnailUrl(), // 썸네일 URL 반환
            boardImage.getImage().getImageTag(),
            formatUploadTime(boardImage.getCreatedAt())
        );
    }
    
    private static String formatUploadTime(LocalDateTime uploadedAt) {
        Duration duration = Duration.between(uploadedAt, LocalDateTime.now());
        
        long minutes = duration.toMinutes();
        if (minutes < 60) return minutes + "m";
        
        long hours = duration.toHours();
        if (hours < 24) return String.format("%02dh", hours);

        return uploadedAt.format(DATE_FORMATTER);
    }
}
