package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


// 보드 간 이미지 이동 요청 DTO
@Schema(description = "보드 간 이미지 이동 요청")
public record BoardImageMoveRequest(

    @Schema(description = "이동 대상 보드 ID", example = "2")
    @NotNull(message = "이동할 보드를 선택해주세요.")
    Long targetBoardId,

    @Schema(description = "이동할 보드 이미지 ID 목록", example = "[1, 2, 3]")
    @NotEmpty(message = "이동할 이미지를 선택해주세요.")
    List<Long> boardImageIds
) {}