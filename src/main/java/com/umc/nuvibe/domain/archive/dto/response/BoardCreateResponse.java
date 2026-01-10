package com.umc.nuvibe.domain.archive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


// 보드 생성 응답 DTO
@Schema(description = "보드 생성 응답")
public record BoardCreateResponse(
    
    @Schema(description = "생성된 보드 ID", example = "1")
    Long boardId,
    
    @Schema(description = "보드 이름", example = "2026 추구미")
    String name
) {}
