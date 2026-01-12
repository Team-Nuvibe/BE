package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.dto.response.ImageTagRes;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;

import java.util.Arrays;
import java.util.List;

public class ImageTagServiceImple implements ImageTagService {

    @Override
    public List<ImageTagRes> findByCategory (ImageTagCategory category){
        return Arrays.stream(ImageTag.values())
                .filter(imageTag -> imageTag.getImageTagCategory() == category)
                .map(imageTag -> new ImageTagRes(
                        imageTag.name().toLowerCase(),
                        imageTag.getDescription()
                ))
                .toList();
    }
}
