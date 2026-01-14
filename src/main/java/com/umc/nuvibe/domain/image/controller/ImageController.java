package com.umc.nuvibe.domain.image.controller;

import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.image.service.ImageServiceImpl;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import com.umc.nuvibe.global.apiPayLoad.result.ImageResultCode;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //multipart 데이터를 받기 위해 사용
    public Response<ImageRes> uploadImage(@RequestPart("file") MultipartFile file, @RequestParam ImageTag tag) {
        ImageRes response=imageService.uploadAndSave(file, tag);
        return Response.ok(ImageResultCode.IMAGE_UPLOAD_OK,response);
    }


}
