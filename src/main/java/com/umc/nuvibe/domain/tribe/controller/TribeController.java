package com.umc.nuvibe.domain.tribe.controller;

import com.umc.nuvibe.domain.tribe.dto.request.TribeJoinReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeJoinRes;
import com.umc.nuvibe.global.apiPayLoad.result.TribeResultCode;
import com.umc.nuvibe.domain.tribe.service.tribe.TribeService;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tribe")
@RequiredArgsConstructor
@Tag(name= "Tribe", description = "트라이브 API")
public class TribeController {

    private final TribeService tribeService;


    @PostMapping("/join")
    @Operation(summary = "트라이브 챗 입장 및 생성", description = "기존 트라이브 챗이 존재 시 입장, 부재 시 새로운 트라이브 챗 생성")
    public Response<TribeJoinRes> joinTribe(
            @AuthUser Long userId,
            @RequestBody @Valid TribeJoinReq request) {

        TribeJoinRes response = tribeService.joinOrCreateTribe(userId, request);

        return Response.of(TribeResultCode.TRIBE_JOIN_SUCCESS, response);
    }
}
