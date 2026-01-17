package com.umc.nuvibe.domain.tribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "이미지 스크랩 요청")
public record ScrapedImageToggleReq(

        @Schema(description = "이미지 ID")
        @NotNull
        Long imageId,

        @Schema(description = "트라이브 ID")
        @NotNull
        Long tribeId

) {
}
