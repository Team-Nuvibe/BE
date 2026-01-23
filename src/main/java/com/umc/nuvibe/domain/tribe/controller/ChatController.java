package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.request.ChatGridReq;
import com.umc.nuvibe.domain.tribe.dto.request.ChatSendReq;
import com.umc.nuvibe.domain.tribe.dto.request.ChatTimelineReq;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatDetailRes;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{chatId}/detail")
    @Operation(summary = "채팅 이미지 상세 조회", description = "그리드 목록에서 이미지 상세 조회")
    public Response<ChatDetailRes> getChatDetail(
            @AuthUser Long userId,
            @PathVariable Long chatId
    ){
        ChatDetailRes res = chatService.getChatDetail(userId, chatId);
        return Response.of(ChatResultCode.CHAT_DETAIL_SUCCESS, res);
    }

    @PostMapping(
            value = "/tribe/{tribeId}/send",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "채팅 발신", description = "이미지 업로드 및 선택 보드에 저장 후 채팅 발신")
    public Response<Void> sendChat(
            @AuthUser Long userId,
            @PathVariable Long tribeId,
            @RequestPart("file") MultipartFile file,
            @RequestParam Long boardId
            ){
        chatService.chatSend(userId, tribeId, file, boardId);
        return Response.of(ChatResultCode.CHAT_SEND_SUCCESS);

    }
}
