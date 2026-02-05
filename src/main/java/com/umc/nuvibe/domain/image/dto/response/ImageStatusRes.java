package com.umc.nuvibe.domain.image.dto.response;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 상태 응답")
public record ImageStatusRes(
        Long imageId,
        ImageStatus status
) {
    public static ImageStatusRes from(Image image) {
        return new ImageStatusRes(image.getId(), image.getStatus());
    }
}
