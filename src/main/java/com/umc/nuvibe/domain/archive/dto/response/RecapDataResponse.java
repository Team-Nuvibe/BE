package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageTag;

import java.util.List;

public record RecapDataResponse (
        List<ImageDetail> lastMonthImages,
        List<ImageDetail> todayImages
) {
    public record ImageDetail (
        long imageId,
        ImageTag tag,
        String imageUrl
    ) {
        public static ImageDetail from(Image image){
            return new ImageDetail(
                    image.getId(),
                    image.getImageTag(),
                    image.getImageUrl()
            );
        }
    }
}
