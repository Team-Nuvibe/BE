package com.umc.nuvibe.domain.tribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "트라이브 채팅 발신 요청")
public record ChatSendReq (

        @Schema(description = "아카이브 보드 ID")
        Long boardId
){}
