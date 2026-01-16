package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;

import java.time.LocalDateTime;

public record TribeInfo(
        Long userTribeId,
        Long tribeId,
        ImageTag imageTag,
//            String lastMessage,
//            LocalDateTime lastMessageAt,
        LocalDateTime joinedAt,
//          Boolean isFavorite,
        Integer participantCount,
        Integer version,
        TribeStatus status
) {
    public static TribeInfo from(UserTribe userTribe) {

        Tribe tribe = userTribe.getTribe();

        return new TribeInfo(
                userTribe.getId(),
                tribe.getId(),
                tribe.getImageTag(),
                userTribe.getCreatedAt(),
                tribe.getCounts(),
                tribe.getVersion(),
                tribe.getStatus()
        );
    }
}
