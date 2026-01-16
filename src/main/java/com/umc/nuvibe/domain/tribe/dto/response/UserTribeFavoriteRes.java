package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;

public record UserTribeFavoriteRes(
        Long userTribeId,
        Long tribeId,
        ImageTag imageTag,
        Boolean isFavorite,
        UserTribeStatus userTribeStatus
) {
    public static UserTribeFavoriteRes from(UserTribe userTribe) {
        return new UserTribeFavoriteRes(
                userTribe.getId(),
                userTribe.getTribe().getId(),
                userTribe.getTribe().getImageTag(),
                userTribe.isFavorite(),
                userTribe.getUserTribeStatus()
        );
    }
}
