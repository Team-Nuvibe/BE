package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;

import java.time.LocalDateTime;

public record TribeJoinRes(
        Long tribeId,
        Long userTribeId,
        String tagName,
        Integer version,
        TribeStatus tribeStatus,
        Integer counts,
        LocalDateTime joinedAt
) {
    public static TribeJoinRes from(Tribe tribe, UserTribe userTribe) {
        return new TribeJoinRes(
                tribe.getId(),
                userTribe.getId(),
                tribe.getTagName(),
                tribe.getVersion(),
                tribe.getStatus(),
                tribe.getCounts(),
                userTribe.getCreatedAt()
        );
    }
}
