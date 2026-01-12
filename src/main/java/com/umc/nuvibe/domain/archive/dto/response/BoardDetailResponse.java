package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


//보드 상세 조회 응답 DTO
@Schema(description = "보드 상세 응답")
public record BoardDetailResponse(
    
    @Schema(description = "보드 ID", example = "1")
    Long boardId,
    
    @Schema(description = "보드 이름", example = "Model")
    String name,
    
    @Schema(description = "이미지 목록")
    List<ImageInfo> images
) {
    
    //보드 내 이미지 정보
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
    ) {}
}
