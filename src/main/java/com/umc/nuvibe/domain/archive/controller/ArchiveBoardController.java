package com.umc.nuvibe.domain.archive.controller;

import com.umc.nuvibe.domain.archive.code.ArchiveSuccessCode;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.service.ArchiveBoardService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archive/boards")
@Tag(name = "Archive", description = "아카이브 API")
public class ArchiveBoardController {

    private final ArchiveBoardService archiveBoardService;

    
    // 보드 목록 조회
    @GetMapping
    @Operation(summary = "아카이브 목록 조회", description = "사용자의 아카이브 보드 목록을 조회합니다.")
    public Response<List<BoardListResponse>> getBoards(
            @Parameter(description = "사용자 ID") @RequestHeader("X-User-Id") Long userId
    ) {
        return Response.of(ArchiveSuccessCode.BOARD_LIST_SUCCESS, archiveBoardService.getBoards(userId));
    }

    
    // 보드 상세 조회
    @GetMapping("/{boardId}")
    @Operation(summary = "아카이브 상세 조회", description = "보드 내 이미지 목록을 조회합니다.")
    public Response<BoardDetailResponse> getBoardDetail(
            @Parameter(description = "사용자 ID") @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "보드 ID") @PathVariable Long boardId,
            @Parameter(description = "태그 필터") @RequestParam(required = false) ImageTag tag
    ) {
        return Response.of(ArchiveSuccessCode.BOARD_DETAIL_SUCCESS, archiveBoardService.getBoardDetail(userId, boardId, tag));
    }
}