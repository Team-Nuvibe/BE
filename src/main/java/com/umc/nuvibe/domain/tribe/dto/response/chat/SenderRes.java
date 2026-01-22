package com.umc.nuvibe.domain.tribe.dto.response.chat;

import com.umc.nuvibe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 이미지 발신자 정보")
public record SenderRes(

        @Schema(description = "유저 ID")
        Long userId,

        @Schema(description = "닉네임", example = "nuvibe")
        String nickname,

        @Schema(description = "프로필 이미지 URL")
        String profileImage
) {
    public static SenderRes from(User user) {
        return new SenderRes(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
