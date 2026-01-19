package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.request.ChatGridReq;
import com.umc.nuvibe.domain.tribe.dto.request.ChatTimelineReq;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatGridListRes;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatTimelineListRes;
import com.umc.nuvibe.domain.tribe.service.chat.ChatService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ChatResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name= "Chat", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/tribe/{tribeId}/timeline")
    @Operation(
            summary = "트라이브 챗 내 채팅 타임라인 목록 조회",
            description = """
                    트라이브 채팅방 타임라인을 커서 기반으로 조회 (최신순)
                    각 채팅당 이모지 요약, 발신자 정보, 내 이모지 포함
                    """)
    public Response<ChatTimelineListRes> getChatTimeLine(
            @AuthUser Long userId,
            @PathVariable Long tribeId,
            @ParameterObject @Valid ChatTimelineReq req
    ){
        ChatTimelineListRes res = chatService.getChatTimelineList(userId, tribeId, req);
        return Response.of(ChatResultCode.CHAT_TIMELINE_SUCCESS, res);
    }

    @GetMapping("/tribe/{tribeId}/grid")
    @Operation(summary = "트라이브 챗 내 채팅 이미지 그리드 목록 조회", description = "챗 내 이미지 목록을 커서 기반으로 조회(최신순)")
    public Response<ChatGridListRes> getChatGrid(
            @AuthUser Long userId,
            @PathVariable Long tribeId,
            @ParameterObject @Valid ChatGridReq req
    ){
        ChatGridListRes res = chatService.getChatGridList(userId, tribeId, req);
        return Response.of(ChatResultCode.CHAT_GRID_SUCCESS, res);
    }
}
