package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


// 보드 삭제 요청 DTO (다중 선택 삭제)
@Schema(description = "보드 삭제 요청")
public record BoardDeleteRequest(
    
    @Schema(description = "삭제할 보드 ID 목록", example = "[1, 2, 3]")
    @NotEmpty(message = "삭제할 보드를 선택해주세요.")
    List<Long> boardIds
) {}
