package com.umc.nuvibe.domain.archive.converter;

import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;


// 아카이브 Entity -> DTO 변환
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArchiveBoardConverter {

    
    // 보드 목록 응답으로 변환
    public static BoardListResponse toBoardListResponse(ArchiveBoard board, String thumbnailUrl) {
        return new BoardListResponse(
                board.getId(),
                board.getName(),
                thumbnailUrl
        );
    }

    
    // 보드 상세 응답으로 변환
    public static BoardDetailResponse toBoardDetailResponse(ArchiveBoard board, List<BoardImage> boardImages) {
        List<BoardDetailResponse.ImageInfo> images = boardImages.stream()
                .map(bi -> new BoardDetailResponse.ImageInfo(
                        bi.getId(),
                        bi.getImage().getId(),
                        bi.getImage().getImageUrl(),
                        bi.getImage().getImageTag()))
                .toList();
        
        return new BoardDetailResponse(
                board.getId(),
                board.getName(),
                images
        );
    }
}
