package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


// 보드 생성 요청 DTO
@Schema(description = "보드 생성 요청")
public record BoardCreateRequest(
    
    @Schema(description = "보드 이름", example = "2026 추구미")
    @NotBlank(message = "보드 이름은 필수입니다.")
    String name
) {}
