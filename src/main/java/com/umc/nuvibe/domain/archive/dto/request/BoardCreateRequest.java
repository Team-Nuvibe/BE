package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// 보드 생성 요청 DTO
@Schema(description = "보드 생성 요청")
public record BoardCreateRequest(
    
    @Schema(description = "보드 이름", example = "2026 추구미")
    @NotBlank(message = "보드 이름은 필수입니다.")
    @Size(max = 20, message = "보드 이름은 20자를 초과할 수 없습니다.")// 길이 제한 추가
    String name
) {}
