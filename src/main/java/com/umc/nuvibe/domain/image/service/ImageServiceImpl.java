package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.s3.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public ImageRes uploadAndSave(MultipartFile file, ImageTag tag) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }

        if (tag == null) {
            throw new BusinessException(ImageErrorCode.IMAGETAG_IS_NULL);
        }

        String imageURL = s3Service.upload(file);

        if (imageURL == null) {
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAIL);
        }

        Image newImage = Image.builder()
                .imageUrl(imageURL)
                .imageTag(tag)
                .build();

        imageRepository.save(newImage);

        return new ImageRes(imageURL, tag);

    }
}
