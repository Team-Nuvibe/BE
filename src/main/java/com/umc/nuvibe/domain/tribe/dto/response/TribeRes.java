package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.tribe.vo.TribeStatus;

import java.time.LocalDateTime;

public class TribeRes {

    public record JoinRes(
            Long tribeId,
            String tagName,
            Integer version,
            TribeStatus tribeStatus,
            Integer counts,
            LocalDateTime joinedAt
    ){}
}
