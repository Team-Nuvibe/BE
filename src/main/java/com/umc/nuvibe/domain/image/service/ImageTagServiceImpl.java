package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ImageTagServiceImpl implements ImageTagService {

    @Override
    public List<ImageTag> findByCategory (ImageTagCategory category){
        return Arrays.stream(ImageTag.values())
                .filter(imageTag -> imageTag.getImageTagCategory() == category)
                .toList();
    }

    @Override
    public List<ImageTag> findByNameAndTagKoAndSynonyms(String search){
        if (search == null || search.isEmpty()){
            return List.of();
        }

        return Arrays.stream(ImageTag.values())
                .filter(imageTag
                        ->imageTag.name().toLowerCase().contains(search.toLowerCase()) ||
                        imageTag.getTagKo().contains(search)||
                        imageTag.getSynonyms().stream()
                                .anyMatch(synonym -> synonym.contains(search))
                )
                .toList();
    }
}
