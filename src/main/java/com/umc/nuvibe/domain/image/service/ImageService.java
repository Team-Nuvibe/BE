package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.dto.request.PreSignedUrlReq;
import com.umc.nuvibe.domain.image.dto.response.ImageDetailRes;
import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.dto.response.ImageStatusRes;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    ImageRes preSaveAndGetUrl(PreSignedUrlReq request, ImageTag tag);

    String uploadImage(MultipartFile file);
    ImageDetailRes getImageDetail(Long userId, Long imageId);
    ImageStatusRes getImageStatus(Long userId, Long imageId);


}
