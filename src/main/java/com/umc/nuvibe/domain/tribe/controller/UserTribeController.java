package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.response.LeaveRes;
import com.umc.nuvibe.domain.tribe.dto.response.TribeListRes;
import com.umc.nuvibe.domain.tribe.dto.response.UserTribeActivateRes;
import com.umc.nuvibe.domain.tribe.dto.response.UserTribeFavoriteRes;
import com.umc.nuvibe.global.apiPayLoad.result.UserTribeResultCode;
import com.umc.nuvibe.domain.tribe.service.userTribe.UserTribeService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userTribe")
@Tag(name= "UserTribe", description = "유저 트라이브 API")
public class UserTribeController {

    private final UserTribeService userTribeService;

    //트라이브 챗 목록 조회
    @GetMapping
    @Operation(summary = "활성화된 챗 목록 조회", description = "최신순으로 조회, 조회 기준들 추가 예정")
    public Response<TribeListRes> getTribeList(
            @AuthUser Long userId){
        TribeListRes res = userTribeService.getTribeList(userId);

        return Response.of(UserTribeResultCode.GET_USERTRIBE_SUCCESS, res);
    }

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

    //트라이브 챗 퇴장
    @DeleteMapping("/{userTribeId}")
    @Operation(summary = "챗 퇴장", description = "활성화된 트라이브 챗 퇴장")
    public Response<LeaveRes> leaveTribe(
         @AuthUser Long userId,
         @PathVariable Long userTribeId){

        LeaveRes res = userTribeService.leaveTribe(userId, userTribeId);
        return Response.of(UserTribeResultCode.USERTRIBE_LEAVE_SUCCESS, res);
    }


}
