package com.umc.nuvibe.domain.archive.controller;

import com.umc.nuvibe.domain.archive.dto.response.RecapActiveResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapBoardResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapDataResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapTagResponse;
import com.umc.nuvibe.domain.archive.service.RecapService;
import com.umc.nuvibe.domain.archive.vo.RecapPeriod;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.RecapResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recap")
@Tag(name = "Recap", description = "리캡 API")
public class RecapController {

    private final RecapService recapService;

    @GetMapping("/tags")
    @Operation(summary = "태그 순위 조회", description = "사용자가 가장 많이 사용한 태그 5개를 조회합니다.")
    public Response<RecapTagResponse> getRecapTags(
            @AuthUser Long userId,
            @RequestParam RecapPeriod period
    ){
        RecapTagResponse response = recapService.getRecapTags(userId, period);
        return Response.ok(RecapResultCode.RECAP_TAG_SUCCESS, response);

    }

    @GetMapping("/board")
    @Operation(summary = "가장 많이 사용한 보드 조회", description = "사용자가 가장 많이 사용한 보드를 조회합니다.")
    public Response<RecapBoardResponse> getRecapBoard(
            @AuthUser Long userId,
            @RequestParam RecapPeriod period
    ){
        RecapBoardResponse response = recapService.getRecapBoard(userId, period);
        return Response.ok(RecapResultCode.RECAP_ARCHIVE_SUCCESS, response);
    }

    @GetMapping("/active")
    @Operation(summary = "사용자 이용 패턴 조회", description = "사용자의 이용 패턴을 조회합니다.")
    public Response<RecapActiveResponse> getRecapActive(
            @AuthUser Long userId,
            @RequestParam RecapPeriod period
    ){
        RecapActiveResponse response = recapService.getRecapActive(userId, period);
        return Response.ok(RecapResultCode.RECAP_STATUS_SUCCESS, response);
    }

    @GetMapping("/calendar/dates")
    @Operation(summary = "월별 업로드 날짜 조회", description = "사용자의 월별 업로드 날짜를 조회합니다.")
    public Response<List<LocalDate>> getImageDropDates(
            @AuthUser Long userId,
            @RequestParam int year,
            @RequestParam int month
    ){
        List<LocalDate> response = recapService.getImageDropDates(userId, year, month);

        return Response.ok(RecapResultCode.RECAP_CALENDAR_SUCCESS, response);
    }

    @GetMapping("/calendar/images")
    @Operation(summary = "날짜 별 업로드한 이미지 조회", description = "날짜 선택시 지난 달 업로드한 이미지들과 해당 날짜에 업로드한 이미지를 조회합니다.")
    public Response<RecapDataResponse> getRecapImages(
            @AuthUser Long userId,
            @Parameter(description = "조회할 날짜 (형식:yyyy-mm-dd)") @RequestParam LocalDate date
    ){
        RecapDataResponse response = recapService.getImagesByDate(userId, date);
        return Response.ok(RecapResultCode.RECAP_IMAGES_SUCCESS, response);
    }




}
