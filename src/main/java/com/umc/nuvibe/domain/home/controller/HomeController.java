package com.umc.nuvibe.domain.home.controller;

import com.umc.nuvibe.domain.home.dto.CategoryTagResponse;
import com.umc.nuvibe.domain.home.dto.DropMissionResponse;
import com.umc.nuvibe.domain.home.dto.MyBoardResponse;
import com.umc.nuvibe.domain.home.dto.TagDetailResponse;
import com.umc.nuvibe.domain.home.service.HomeService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.HomeResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Tag(name = "Home", description = "홈 화면 API")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/drop-mission")
    @Operation(summary = "오늘의 드롭미션", description = "랜덤 소분류 태그와 이미지 1개를 조회합니다.")
    public Response<DropMissionResponse> getDropMission() {
        return Response.of(HomeResultCode.DROP_MISSION_SUCCESS, homeService.getDropMission());
    }

    @GetMapping("/my-boards")
    @Operation(summary = "나의 기록", description = "사용자의 보드 목록을 조회합니다. (최대 5개)")
    public Response<List<MyBoardResponse>> getMyBoards(@AuthUser Long userId) {
        return Response.of(HomeResultCode.MY_BOARDS_SUCCESS, homeService.getMyBoards(userId));
    }

    @GetMapping("/categories/{category}/tags")
    @Operation(summary = "대분류별 태그 목록", description = "대분류 카테고리의 소분류 태그 목록을 조회합니다. (최대 15개 랜덤)")
    public Response<List<CategoryTagResponse>> getCategoryTags(
            @Parameter(description = "대분류 카테고리") @PathVariable ImageTagCategory category) {
        return Response.of(HomeResultCode.CATEGORY_TAGS_SUCCESS, homeService.getCategoryTags(category));
    }

    @GetMapping("/tags/{tag}")
    @Operation(summary = "태그 상세 조회", description = "소분류 태그의 상세 정보와 트라이브 채팅 이미지를 조회합니다.")
    public Response<TagDetailResponse> getTagDetail(
            @Parameter(description = "소분류 태그") @PathVariable ImageTag tag) {
        return Response.of(HomeResultCode.TAG_DETAIL_SUCCESS, homeService.getTagDetail(tag));
    }
}
