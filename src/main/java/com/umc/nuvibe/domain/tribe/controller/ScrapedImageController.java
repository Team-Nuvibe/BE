package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageSliceReq;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageToggleRes;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageListRes;
import com.umc.nuvibe.domain.tribe.service.scrapedImage.ScrapedImageService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ScrapedImageResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrapedImage")
@RequiredArgsConstructor
@Tag(name= "ScrapedImage", description = "스크랩 이미지 API")
public class ScrapedImageController {

    private final ScrapedImageService scrapedImageService;

    @PostMapping("/chat/{chatId}")
    @Operation(summary = "이미지 스크랩 토글", description = "기존 스크랩 존재 시 삭제, 부재 시 새로운 스크랩 생성")
    public Response<ScrapedImageToggleRes> scrapedImageToggle(
            @AuthUser Long userId,
            @PathVariable Long chatId
    ){
        ScrapedImageToggleRes res =
                scrapedImageService.toggleScrapedImage(userId, chatId);

        /*
         * - scrapedImageId != null : 스크랩 생성
         * - scrapedImageId == null : 스크랩 취소
         */
        ScrapedImageResultCode resultCode =
                res.scrapedImageId() != null
                        ? ScrapedImageResultCode.SCRAPEDIMAGE_CREATED
                        : ScrapedImageResultCode.SCRAPEDIMAGE_DELETED;

        return Response.of(resultCode, res);
    }

    @GetMapping
    @Operation(summary = "스크랩 이미지 전체 목록 조회", description = "최신순 전체 조회 또는 태그별 조회 제공 (무한 스크롤 기능)")
    public Response<ScrapedImageListRes> getTotalScrapedImage(
            @AuthUser Long userId,
            @ParameterObject @ModelAttribute @Valid ScrapedImageSliceReq req
            ){
        ScrapedImageListRes res = scrapedImageService.getTotalScrapedImage(userId, req);

        return Response.of(ScrapedImageResultCode.SCRAPEDIMAGE_TOTAL_LIST_SUCCESS, res);
    }

    @GetMapping("/tribe/{tribeId}")
    @Operation(summary = "해당 트라이브 챗 내 스크랩 이미지 목록 조회", description = "최신순으로 조회 (무한 스크롤 기능)")
    public Response<ScrapedImageListRes> getTribeScrapedImage(
            @AuthUser Long userId,
            @PathVariable Long tribeId,
            @ParameterObject @ModelAttribute @Valid ScrapedImageSliceReq req
    ){
        ScrapedImageListRes res = scrapedImageService.getTribeScrapedImage(userId, tribeId, req);
        return Response.of(ScrapedImageResultCode.SCRAPEDIMAGE_TRIBE_LIST_SUCCESS, res);
    }
}
