package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


// 보드명 수정 요청 DTO
@Schema(description = "보드명 수정 요청")
public record BoardNameUpdateRequest(
    
    @Schema(description = "새 보드 이름", example = "model")
    @NotBlank(message = "보드 이름은 필수입니다.")
    String name
) {}