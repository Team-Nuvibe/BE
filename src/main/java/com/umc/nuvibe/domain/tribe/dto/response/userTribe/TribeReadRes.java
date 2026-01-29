package com.umc.nuvibe.domain.tribe.dto.response.userTribe;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "트라이브 읽음 처리 결과")
public record TribeReadRes(

        @Schema(description = "트라이브 ID")
        Long tribeId,

        @Schema(description = "최종 읽음 처리된 chatId (메시지가 없는 트라이브 챗이면 null)")
        Long lastReadChatId

) {
}
