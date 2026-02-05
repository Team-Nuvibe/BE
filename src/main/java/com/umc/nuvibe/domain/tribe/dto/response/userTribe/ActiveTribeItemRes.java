package com.umc.nuvibe.domain.tribe.dto.response.userTribe;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Active 트라이브 챗 목록 아이템")
public record ActiveTribeItemRes(

        @Schema(description = "트라이브 ID")
        Long tribeId,

        @Schema(description = "유저 트라이브 ID")
        Long userTribeId,

        @Schema(description = "이미지 태그 (Enum)", example = "CAFE")
        ImageTag imageTag,

        @Schema(description = "트라이브 챗 참여자 수")
        Integer counts,

        @Schema(description = "트라이브 챗 고정 여부")
        boolean isFavorite,

        @Schema(description = "마지막 활동 시각")
        LocalDateTime lastActivityAt,

        @Schema(description = "안 읽은 채팅 개수")
        long unreadCount
) {
}
