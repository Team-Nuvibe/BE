package com.umc.nuvibe.domain.image.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;

public record ImageRes(
        String imageURL,
        ImageTag imageTag
) {
}
