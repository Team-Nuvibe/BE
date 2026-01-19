package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이모지 반응 요약 정보")
public record EmojiSummaryRes(

        @Schema(description = "이모지 타입", example = "LIKE")
        EmojiType type,

        @Schema(description = "해당 이모지 수", example = "10")
        Long count
) {
}
