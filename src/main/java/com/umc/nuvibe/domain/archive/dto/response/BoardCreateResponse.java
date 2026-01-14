package com.umc.nuvibe.domain.archive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;


// 보드 생성 응답 DTO
@Schema(description = "보드 생성 응답")
public record BoardCreateResponse(
        Long boardId,
        String name
) {
    public static BoardCreateResponse from(ArchiveBoard board) {
        return new BoardCreateResponse(
                board.getId(),
                board.getName()
        );
    }
}
