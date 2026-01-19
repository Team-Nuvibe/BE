package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.dto.response.ImageDetailRes;
import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    ImageRes uploadAndSave(MultipartFile file, ImageTag tag);

    String uploadImage(MultipartFile file);
    ImageDetailRes getImageDetail(Long userId, Long imageId);


}
