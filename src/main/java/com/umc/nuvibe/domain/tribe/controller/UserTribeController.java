package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.request.ActiveTribeListReq;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.*;
import com.umc.nuvibe.global.apiPayLoad.result.UserTribeResultCode;
import com.umc.nuvibe.domain.tribe.service.userTribe.UserTribeService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userTribe")
@Tag(name= "UserTribe", description = "유저 트라이브 API")
public class UserTribeController {

    private final UserTribeService userTribeService;

    //트라이브 챗 활성화
    @PatchMapping("/{userTribeId}/activate")
    @Operation(summary = "유저 트라이브 챗 활성화", description = "인원 수가 5명 이상인 트라이브 챗에 대해 유저 별로 활성화")
    public Response<UserTribeActivateRes> activateUserTribe(
            @AuthUser Long userId,
            @PathVariable Long userTribeId
    ){
        UserTribeActivateRes res = userTribeService.activateUserTribe(userId, userTribeId);
        return Response.of(UserTribeResultCode.USERTRIBE_ACTIVATE_SUCCESS, res);
    }

    //트라이브 챗 즐겨찾기
    @PatchMapping("/{userTribeId}/favorite")
    @Operation(summary = "트라이브 챗 즐겨찾기", description = "원하는 트라이브 챗을 즐겨찾기로 등록")
    public Response<UserTribeFavoriteRes> favoriteUserTribe(
            @AuthUser Long userId,
            @PathVariable Long userTribeId
    ){
        UserTribeFavoriteRes res = userTribeService.toggleFavorite(userId, userTribeId);
        return Response.of(UserTribeResultCode.USERTRIBE_FAVORITE_SUCCESS, res);
    }

    // 활성화된 트라이브 챗 목록 조회
    @GetMapping("/active")
    @Operation(summary = "활성화된 트라이브 목록 조회", description = "트라이브 챗 목록을 고정, 마지막 활동 시각(동률 시 안 읽은 메시지 여부 순), 마지막 chatId 순으로 정렬")
    public Response<ActiveTribeListRes> getActiveTribeList(
            @AuthUser Long userId,
            @ParameterObject ActiveTribeListReq req
    ){
        ActiveTribeListRes res = userTribeService.getActiveTribeList(userId, req);
        return Response.of(UserTribeResultCode.GET_USERTRIBE_SUCCESS, res);
    }

    // 대기 중 트라이브 챗 목록 조회
    @GetMapping("/waiting")
    @Operation(summary = "대기 중인 트라이브 목록 조회", description = "대기 중 트라이브 챗 목록 tribeId 순으로 정렬")
    public Response<WaitingTribeListRes> getWaitingTribeList(
            @AuthUser Long userId,
            @RequestParam(required = false) Long cursorTribeId,
            @RequestParam(required = false) Integer size
    ){
        WaitingTribeListRes res = userTribeService.getWaitingTribeList(userId, cursorTribeId, size);
        return Response.of(UserTribeResultCode.GET_USERTRIBE_SUCCESS, res);
    }

    // 트라이브 챗 읽음 처리
    @PatchMapping("/tribe/{tribeId}/read")
    @Operation(summary = "트라이브 챗 읽음 처리", description = "안 읽은 메시지 수를 0개로 초기화")
    public Response<TribeReadRes> readChat(
            @AuthUser Long userId,
            @PathVariable Long tribeId
    ){
        TribeReadRes res = userTribeService.readChat(userId, tribeId);
        return Response.of(UserTribeResultCode.USERTRIBE_READ_SUCCESS, res);
    }


    //트라이브 챗 퇴장
    @DeleteMapping("/{userTribeId}")
    @Operation(summary = "챗 퇴장", description = "활성화된 트라이브 챗 퇴장")
    public Response<LeaveRes> leaveTribe(
         @AuthUser Long userId,
         @PathVariable Long userTribeId){

        LeaveRes res = userTribeService.leaveTribe(userId, userTribeId);
        return Response.of(UserTribeResultCode.USERTRIBE_LEAVE_SUCCESS, res);
    }

    @PatchMapping("/{userTribeId}/mute")
    @Operation(summary = "트라이브 챗 무음 설정", description = "특정 트라이브 챗의 푸시 알림 무음 토글")
    public Response<UserTribeMuteRes> muteUserTribe(
            @AuthUser Long userId,
            @PathVariable Long userTribeId
    ){
        UserTribeMuteRes res = userTribeService.toggleMute(userId, userTribeId);
        return Response.ok(UserTribeResultCode.USERTRIBE_MUTE_SUCCESS, res);
    }


}
