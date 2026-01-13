package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;

import java.util.List;


public interface ImageTagService {
    List<ImageTag> findByCategory(ImageTagCategory category);
    List<ImageTag> findByNameAndTagKoAndSynonyms(String search);
}
