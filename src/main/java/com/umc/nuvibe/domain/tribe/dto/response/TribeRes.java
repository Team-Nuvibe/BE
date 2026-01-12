package com.umc.nuvibe.domain.tribe.dto.response;

import com.umc.nuvibe.domain.tribe.vo.TribeStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TribeRes {

    public record JoinRes(
            Long tribeId,
            String tagName,
            Integer version,
            TribeStatus tribeStatus,
            Integer counts,
            LocalDateTime joinedAt
    ){}

    public record TribeListRes(
            List<TribeInfo> tribes

    ){}

    public record TribeInfo(
            Long userTribeId,
            Long tribeId,
            String tagName,
//            String lastMessage,
//            LocalDateTime lastMessageAt,
            LocalDateTime joinedAt,
//          Boolean isFavorite,
            Integer participantCount,
            Integer version,
            TribeStatus status
          ){}
}
