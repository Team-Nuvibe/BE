package com.umc.nuvibe.domain.tribe.dto.response.chat;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "채팅방 이미지 그리드 전체 목록 조회 응답")
public record ChatGridListRes(

        @Schema(description = "정렬된 이미지 그리드 항목 목록 (flat list)")
        List<ChatGridItemRes> items,

        @Schema(description = "다음 페이지 커서(createdAt). hasNext=false면 null")
        LocalDateTime nextCursorCreatedAt,

        @Schema(description = "다음 페이지 커서(chatId). hasNext=false면 null")
        Long nextCursorChatId,

        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext
) {
}
