package com.umc.nuvibe.domain.user.vo;

import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getDiff(UserSetting prev) {
        List<String> changes = new ArrayList<>();

        compareAndAdd(changes, "서비스 알림", prev.getIsServiceAlert(), this.isServiceAlert);
        compareAndAdd(changes, "계정 보안 알림", prev.getIsSecurityAlert(), this.isSecurityAlert);
        compareAndAdd(changes, "추천 알림", prev.getIsRecommendAlert(), this.isRecommendAlert);
        compareAndAdd(changes, "Recap 알림", prev.getIsRecapAlert(), this.isRecapAlert);
        compareAndAdd(changes, "트라이브 생성 알림", prev.getIsTribeCreateAlert(), this.isTribeCreateAlert);
        compareAndAdd(changes, "트라이브 채팅 알림", prev.getIsTribeChatAlert(), this.isTribeChatAlert);
        compareAndAdd(changes, "반응 알림", prev.getIsReactionAlert(), this.isReactionAlert);

        return changes;
    }

    // 값이 다를 때만 리스트에 추가
    private void compareAndAdd(List<String> changes, String fieldName, Boolean oldVal, Boolean newVal) {
        if (oldVal != null && !oldVal.equals(newVal)) {
            changes.add(String.format("%s: %s -> %s", fieldName, oldVal, newVal));
        }
    }
}
