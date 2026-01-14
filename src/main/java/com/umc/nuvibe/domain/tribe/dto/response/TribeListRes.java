package com.umc.nuvibe.domain.tribe.dto.response;

import java.util.List;

public record TribeListRes(
        List<TribeInfo> tribes

) {
    public static TribeListRes of(List<TribeInfo> tribeInfoList) {
        return new TribeListRes(tribeInfoList);
    }
}
