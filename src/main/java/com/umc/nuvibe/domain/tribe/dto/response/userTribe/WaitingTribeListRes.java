package com.umc.nuvibe.domain.tribe.dto.response.userTribe;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Waiting 트라이브 챗 목록 응답")
public record WaitingTribeListRes(

        @Schema(description = "Waiting 트라이브 챗 목록")
        List<WaitingTribeItemRes> items,

        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext,

        @Schema(description = "다음 페이지 커서. hasNext=false면 null")
        Long nextCursor
) {
}
