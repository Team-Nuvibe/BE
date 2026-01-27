package com.umc.nuvibe.domain.archive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;


//보드 목록 조회 응답 DTO
@Schema(description = "보드 목록 응답")
public record BoardListResponse(
        Long boardId,
        String name,
        String thumbnailUrl,
        Integer tagCount
) {
    public static BoardListResponse from(ArchiveBoard board, String thumbnailUrl, Integer tagCount) {
        return new BoardListResponse(
                board.getId(),
                board.getName(),
                thumbnailUrl,
                tagCount
        );
    }
}