package com.umc.nuvibe.domain.tribe.dto.response.tribe;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Waiting 트라이브 챗 목록 아이템")
public record WaitingTribeItemRes(

        @Schema(description = "트라이브 ID")
        Long tribeId,

        @Schema(description = "이미지 태그 (Enum)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "트라이브 챗 참여자 수")
        Integer counts
) {
}
