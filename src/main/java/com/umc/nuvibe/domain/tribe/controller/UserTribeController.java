package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.code.UserTribeResultCode;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.service.userTribe.UserTribeService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
