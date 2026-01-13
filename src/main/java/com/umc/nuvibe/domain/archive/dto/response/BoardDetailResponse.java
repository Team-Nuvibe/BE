package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Schema(description = "보드 상세 응답")
public record BoardDetailResponse(
    Long boardId,
    String name,
    List<ImageInfo> images
) {
    
    
    public static BoardDetailResponse from(ArchiveBoard board, List<BoardImage> boardImages) {
        List<ImageInfo> images = boardImages.stream()
                .map(ImageInfo::from)
                .toList();
        
        return new BoardDetailResponse(
                board.getId(),
                board.getName(),
                images
        );
    }
    
    @Schema(description = "이미지 정보")
    public record ImageInfo(
        
        @Schema(description = "보드 이미지 ID", example = "1")
        Long boardImageId,
        
        @Schema(description = "이미지 ID", example = "10")
        Long imageId,
        
        @Schema(description = "이미지 URL")
        String imageUrl,
        
        @Schema(description = "이미지 태그", example = "WARM")
        ImageTag imageTag
    ) {
        
        public static ImageInfo from(BoardImage bi) {
            return new ImageInfo(
                    bi.getId(),
                    bi.getImage().getId(),
                    bi.getImage().getImageUrl(),
                    bi.getImage().getImageTag()
            );
        }
    }
}
