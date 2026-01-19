package com.umc.nuvibe.domain.tribe.dto.response.chat;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "트라이브 채팅 타임라인 조회 응답")
public record ChatTimelineListRes(

        @Schema(description = "채팅 타임라인 목록")
        List<ChatTimelineItemRes> items,

        @Schema(description = "다음 페이지 조회를 위한 커서(chatId)")
        Long nextLastChatId,

        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext
) {
}
