package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.code.UserTribeResultCode;
import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.service.userTribe.UserTribeService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/userTribe")
public class UserTribeController {

    private final UserTribeService userTribeService;

    @GetMapping("/my")
    @Operation(summary = "활성화된 챗 목록 조회", description = "최신순으로 조회, 조회 기준들 추가 예정")
    public Response<TribeRes.TribeListRes> getTribeList(
            @AuthUser Long userId){
        TribeRes.TribeListRes res = userTribeService.getTribeList(userId);

        return Response.of(UserTribeResultCode.GET_TRIBE_SUCCESS, res);
    }

    @DeleteMapping("/leave")
    @Operation(summary = "챗 퇴장", description = "활성화된 트라이브 챗 퇴장")
    public Response<TribeRes.LeaveRes> leaveTribe(
         @AuthUser Long userId,
         @RequestBody @Valid TribeReq.LeaveReq req){

        TribeRes.LeaveRes res = userTribeService.leaveTribe(userId, req);
        return Response.of(UserTribeResultCode.USERTRIBE_LEAVE_SUCCESS, res);
    }
}
