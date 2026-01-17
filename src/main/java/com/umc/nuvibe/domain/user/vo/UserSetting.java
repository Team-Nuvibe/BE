package com.umc.nuvibe.domain.user.vo;

import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSetting implements Serializable {

    // 서비스 알림
    private Boolean isServiceAlert;

    //계정 보안 알림
    private Boolean isSecurityAlert;

    //추천 알림
    private Boolean isRecommendAlert;

    //recap 알림
    private Boolean isRecapAlert;

    //트라이브 챗 생성 알림
    private Boolean isTribeCreateAlert;

    //트라이브 챗 채팅 알림
    private Boolean isTribeChatAlert;

    //이미지 반응 알림
    private Boolean isReactionAlert;

    // 회원가입 시 기본값 설정 (모두 켜기)
    public static UserSetting createDefault() {
        return UserSetting.builder()
                .isServiceAlert(false)
                .isSecurityAlert(false)
                .isRecommendAlert(false)
                .isRecapAlert(false)
                .isTribeCreateAlert(false)
                .isTribeChatAlert(false)
                .isReactionAlert(false)
                .build();
    }

    public UserSetting update(UserSettingReq req) {
        return UserSetting.builder()
                .isServiceAlert(req.isServiceAlert() != null ? req.isServiceAlert() : this.isServiceAlert)
                .isSecurityAlert(req.isSecurityAlert() != null ? req.isSecurityAlert() : this.isSecurityAlert)
                .isRecommendAlert(req.isRecommendAlert() != null ? req.isRecommendAlert() : this.isRecommendAlert)
                .isRecapAlert(req.isRecapAlert() != null ? req.isRecapAlert() : this.isRecapAlert)
                .isTribeCreateAlert(req.isTribeCreateAlert() != null ? req.isTribeCreateAlert() : this.isTribeCreateAlert)
                .isTribeChatAlert(req.isTribeChatAlert() != null ? req.isTribeChatAlert() : this.isTribeChatAlert)
                .isReactionAlert(req.isReactionAlert() != null ? req.isReactionAlert() : this.isReactionAlert)
                .build();
    }
}
