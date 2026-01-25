package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.service.emoji.EmojiService;
import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.EmojiResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emoji")
@Tag(name= "Emoji", description = "이모지 API")
public class EmojiController {

    private final EmojiService emojiService;

    @PostMapping("/chat/{chatId}")
    @Operation(
            summary = "채팅 이모지 반응",
            description = """
                    각 채팅에 대해 이모지 반응을 추가한 뒤 집계해서 반환
                    - 기존 이모지 없음 : CREATED
                    - 같은 이모지 재클릭 : DELETED
                    - 다른 이모지 클릭 : UPDATED
                    """)
    public Response<Void> emojiReact(
            @AuthUser Long userId,
            @PathVariable Long chatId,
            @RequestParam EmojiType type
            ){
        emojiService.emojiReact(userId, chatId, type);
        return Response.of(EmojiResultCode.EMOJI_REACT_SUCCESS);
    }
}
