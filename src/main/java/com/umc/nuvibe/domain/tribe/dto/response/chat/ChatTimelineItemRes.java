package com.umc.nuvibe.domain.tribe.dto.response.chat;

import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "채팅 타임라인 아이템, (sender, reactionsSummary는 중첩 객체)")
public record ChatTimelineItemRes(

        @Schema(description = "채팅 ID")
        Long chatId,

        @Schema(description = "이미지 ID")
        Long imageId,

        @Schema(description = "이미지 URL (S3 경로)")
        String imageUrl,

        @Schema(description = "메시지 생성 시각")
        LocalDateTime createdAt,

        @Schema(description = "보낸 사람 정보")
        SenderRes sender,

        @Schema(description = "이모지 반응 요약 (count > 0만 포함)")
        List<EmojiSummaryRes> reactionsSummary,

        @Schema(description = "내가 누른 이모지 타입 (없으면 null)")
        EmojiType myReactionType,

        @Schema(description = "내가 스크랩했는지 여부")
        boolean isScrapped
) {
    public static ChatTimelineItemRes from(
            Chat chat,
            List<EmojiSummaryRes> reactionsSummary,
            EmojiType myReactionType,
            boolean isScrapped
    ) {
        return new ChatTimelineItemRes(
                chat.getId(),
                chat.getImage().getId(),
                chat.getImage().getThumbnailUrl(), // 썸네일 URL 반환
                chat.getCreatedAt(),
                SenderRes.from(chat.getUser()),
                reactionsSummary,
                myReactionType,
                isScrapped
        );
    }
}
