package com.umc.nuvibe.domain.user.dto.request;

public record UserSettingReq(
        Boolean isServiceAlert,
        Boolean isSecurityAlert,
        Boolean isRecommendAlert,
        Boolean isRecapAlert,
        Boolean isTribeCreateAlert,
        Boolean isTribeChatAlert,
        Boolean isReactionAlert
) {
}
