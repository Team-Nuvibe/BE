package com.umc.nuvibe.domain.tribe.dto.response.userTribe;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;

import java.time.LocalDateTime;

public record UserTribeActivateRes(
        Long userTribeId,
        Long tribeId,
        ImageTag imageTag,
        Boolean isFavorite,
        TribeStatus tribeStatus,
        UserTribeStatus userTribeStatus,
        LocalDateTime createdAt
) {
    public static UserTribeActivateRes from(UserTribe userTribe) {
        return new UserTribeActivateRes(
                userTribe.getId(),
                userTribe.getTribe().getId(),
                userTribe.getTribe().getImageTag(),
                userTribe.isFavorite(),
                userTribe.getTribe().getStatus(),
                userTribe.getUserTribeStatus(),
                userTribe.getCreatedAt()
        );
    }
}
