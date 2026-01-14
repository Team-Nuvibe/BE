package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.s3.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    public ImageRes uploadAndSave(MultipartFile file, ImageTag tag) {

        String imageURL= s3Service.upload(file);

        Image newImage = Image.builder()
                .imageUrl(imageURL)
                .imageTag(tag)
                .build();

        imageRepository.save(newImage);

        return new ImageRes(imageURL, tag);
    }

}
