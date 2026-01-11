package com.umc.nuvibe.domain.tribe.converter;

import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TribeConverter {

    public static class ToEntity {
        public static UserTribe toUserTribe(User user, Tribe tribe) {
            return new UserTribe(user, tribe);
        }

        public static Tribe toTribe(String tagName, int version) {
            return new Tribe(
                    tagName,
                    0,             // 초기 인원 0명
                    version,             // 계산된 버전
                    TribeStatus.INACTIVE // 초기 상태 INACTIVE
            );
        }
    }


    public static class ToResponse {
        public static TribeRes.JoinRes toJoinRes(Tribe tribe) {
            return new TribeRes.JoinRes(
                    tribe.getId(),
                    tribe.getTagName(),
                    tribe.getVersion(),
                    tribe.getStatus(),
                    tribe.getCounts() + 1, // 반영될 예상 인원수
                    LocalDateTime.now()
            );
        }
    }
}
