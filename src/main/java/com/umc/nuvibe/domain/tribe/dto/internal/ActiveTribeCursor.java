package com.umc.nuvibe.domain.tribe.dto.internal;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Active 트라이브 챗 목록 커서 ")
public record ActiveTribeCursor(

        @Schema(description = "즐겨찾기 여부")
        Boolean fav,

        @Schema(description = "안 읽음 메시지 여부")
        Boolean unread,

        @Schema(description = "마지막 채팅 시각")
        LocalDateTime lastChatAt,

        @Schema(description = "트라이브 ID")
        Long tribeId
) {

    // Repository 쿼리용 int 변환
    public int favInt() { return Boolean.TRUE.equals(fav) ? 1 : 0; }
    public int unreadInt() { return Boolean.TRUE.equals(unread) ? 1 : 0; }

    // 커서 데이터 완전성 검증
    public boolean isComplete() {
        return fav != null && unread != null && lastChatAt != null && tribeId != null;
    }
}
