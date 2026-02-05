package com.umc.nuvibe.domain.image.controller;

import com.umc.nuvibe.domain.image.dto.request.PreSignedUrlReq;
import com.umc.nuvibe.domain.image.dto.response.ImageDetailRes;
import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.dto.response.ImageStatusRes;
import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ImageResultCode;
import com.umc.nuvibe.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/presigned-url")
    @Operation(summary = "Presigned URL 발급", description = "이미지 업로드를 위한 Presigned URL을 발급합니다. " +
            "url 받으시면 put/url로 s3에 사용자가 직접 이미지 업로드하게 해주시면 될 것 같아요." +
            "url 발급 이후로는 s3 업로드까지 프론트에서 맡아서 해주셔야 할 것 같습니다.." +
            "POST 말고 PUT으로 이미지 쏴주시면 됩니다!!")
    public Response<ImageRes> getPresignedUrl(@RequestBody PreSignedUrlReq request, @RequestParam ImageTag tag) {
        ImageRes response = imageService.preSaveAndGetUrl(request, tag);
        return Response.ok(ImageResultCode.IMAGE_UPLOAD_OK, response);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "이미지 상세 정보 조회", description = "업로드한 이미지의 상세정보를 불러옵니다.")
    public Response<ImageDetailRes> getImageDetail(
            @AuthUser Long userId,
            @Parameter(description = "조회할 이미지 ID") @PathVariable Long imageId
    ){
        ImageDetailRes response = imageService.getImageDetail(userId,imageId);
        return Response.ok(ImageResultCode.IMAGE_DETAIL_OK, response);
    }

    @GetMapping("/{imageId}/status")
    @Operation(summary = "이미지 상태 조회", description = "채팅 전송을 위해 이미지 상태를 확인합니다.")
    public Response<ImageStatusRes> getImageStatus(
            @AuthUser Long userId,
            @PathVariable Long imageId
    ){
        ImageStatusRes res = imageService.getImageStatus(userId,imageId);
        return Response.of(ImageResultCode.IMAGE_STATUS_OK, res);
    }


}
