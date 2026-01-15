package com.umc.nuvibe.domain.archive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 보드에 추가")
public record BoardImageAddRequest(
        @Schema(description = "추가할 이미지 id")
        Long imageId
) {}

