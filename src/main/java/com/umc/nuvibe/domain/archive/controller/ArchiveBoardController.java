package com.umc.nuvibe.domain.archive.controller;

import com.umc.nuvibe.domain.archive.dto.request.BoardCreateRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardImageDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardNameUpdateRequest;
import com.umc.nuvibe.domain.archive.dto.request.*;
import com.umc.nuvibe.domain.archive.dto.response.BoardCreateResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardSummaryResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardImageResponse;
import com.umc.nuvibe.domain.archive.service.ArchiveBoardService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ArchiveResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 아카이브 보드 API 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archive")
@Tag(name = "Archive", description = "아카이브 API")
public class ArchiveBoardController {

    private final ArchiveBoardService archiveBoardService;

    
    // 보드 목록 조회 및 검색
    @GetMapping
    @Operation(summary = "아카이브 목록 조회 및 검색", description = "보드 내 이미지 목록을 조회합니다. 검색어를 이용해 보드 검색이 가능합니다")
    public Response<List<BoardListResponse>> getBoards(
        @AuthUser Long userId,
        @Parameter(description = "보드명 검색어") @RequestParam(required = false) String keyword
    ) {
        return Response.of(
            ArchiveResultCode.BOARD_LIST_SUCCESS, 
            archiveBoardService.getBoards(userId, keyword)
        );
    }

    
    // 보드 상세 조회
    @GetMapping("/{boardId}")
    @Operation(summary = "아카이브 상세 조회", description = "보드 내 이미지 목록을 조회합니다.")
    public Response<BoardDetailResponse> getBoardDetail(
            @AuthUser Long userId,
            @Parameter(description = "보드 ID") @PathVariable Long boardId,
            @Parameter(description = "태그 필터") @RequestParam(required = false) ImageTag tag
    ) {
        return Response.of(ArchiveResultCode.BOARD_DETAIL_SUCCESS, archiveBoardService.getBoardDetail(userId, boardId, tag));
    }

    
    // 보드 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // http 201 반환
    @Operation(summary = "아카이브 보드 생성", description = "새로운 아카이브 보드를 생성합니다.")
    public Response<BoardCreateResponse> createBoard(
            @AuthUser Long userId,
            @Valid @RequestBody BoardCreateRequest request
    ) {
        return Response.of(ArchiveResultCode.BOARD_CREATE_SUCCESS, archiveBoardService.createBoard(userId, request));
    }

    
    // 보드 삭제 (다중)
    @PostMapping("/delete")
    @Operation(summary = "아카이브 보드 삭제", description = "선택한 보드들을 삭제합니다. 보드 내 이미지도 함께 삭제됩니다.")
    public Response<Void> deleteBoards(
            @AuthUser Long userId,// @AuthUser 적용
            @Valid @RequestBody BoardDeleteRequest request
    ) {
        archiveBoardService.deleteBoards(userId, request);
        return Response.of(ArchiveResultCode.BOARD_DELETE_SUCCESS);
    }

    
    // 보드명 수정 (보드 내부에서)
    @PatchMapping("/{boardId}/name")
    @Operation(summary = "아카이브 보드명 수정", description = "보드 이름을 수정합니다.")
    public Response<Void> updateBoardName(
            @AuthUser Long userId, //@AuthUser 적용
            @Parameter(description = "보드 ID") @PathVariable Long boardId,
            @Valid @RequestBody BoardNameUpdateRequest request
    ) {
        archiveBoardService.updateBoardName(userId, boardId, request);
        return Response.of(ArchiveResultCode.BOARD_NAME_UPDATE_SUCCESS);
    }

    
    // 보드 내 이미지 삭제(다중)
    @DeleteMapping("/{boardId}/delete")
    @Operation(summary = "아카이브 보드 내 이미지 삭제", description = "보드 내 선택한 이미지들을 삭제합니다.")
    public Response<Void> deleteBoardImages(
            @AuthUser Long userId, // @AuthUser 적용
            @Parameter(description = "보드 ID") @PathVariable Long boardId,
            @Valid @RequestBody BoardImageDeleteRequest request
    ) {
        archiveBoardService.deleteBoardImages(userId, boardId, request);
        return Response.of(ArchiveResultCode.BOARD_IMAGE_DELETE_SUCCESS);
    }

    // 사용자가 올린 모든 이미지 조회 (페이징, 최신순)
    @GetMapping("/images")
    @Operation(summary = "보드에 올린 전체 이미지 조회",
            description = "사용자가 올린 모든 이미지를 최신순으로 조회합니다.(아카이브 첫 화면 상단 이미지 나열)")
    public Response<Page<BoardImageResponse>> getUserImages(
        @AuthUser Long userId,
        @Parameter(description = "페이지 번호 (0부터 시작)")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기")
        @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return Response.of(
            ArchiveResultCode.BOARD_IMAGES_SUCCESS,
            archiveBoardService.getBoardImages(userId, pageable)
        );
    }
    // Vibe 톤 입구
    @GetMapping("/vibe")
    @Operation(summary = "아카이브 Vibe 톤 입구",
            description = "사용자 닉네임과 가장 많이 사용한 태그 Top 4를 조회합니다.")
    public Response<BoardSummaryResponse> getSummary(
        @AuthUser Long userId
    ) {
        return Response.of(
            ArchiveResultCode.BOARD_VIBE_SUCCESS,
            archiveBoardService.getSummary(userId)
        );
    }


    //보드에 이미지 추가
    @PostMapping("/{boardId}/images")
    @Operation(summary = "아카이브 보드에 이미지 추가", description = "이미지를 지정한 아카이브 보드에 추가합니다.")
    public Response<Void> addBoardImage(
            @AuthUser Long userId,
            @Parameter(description = "보드 ID") @PathVariable Long boardId,
            @Valid @RequestBody BoardImageAddRequest request
    ){
        archiveBoardService.addBoardImage(userId, boardId, request);
        return Response.of(ArchiveResultCode.BOARD_IMAGE_ADD_SUCCESS);
    }
}