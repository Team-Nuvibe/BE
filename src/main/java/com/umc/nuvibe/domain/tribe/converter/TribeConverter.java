package com.umc.nuvibe.domain.tribe.converter;

import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        public static TribeRes.JoinRes toJoinRes(Tribe tribe, UserTribe userTribe) {
            return new TribeRes.JoinRes(
                    tribe.getId(),
                    tribe.getTagName(),
                    tribe.getVersion(),
                    tribe.getStatus(),
                    tribe.getCounts(),
                    userTribe.getCreatedAt()
            );
        }

        public static TribeRes.TribeInfo toTribeInfo(UserTribe userTribe){

            Tribe tribe = userTribe.getTribe();

            return new TribeRes.TribeInfo(
                    userTribe.getId(),
                    tribe.getId(),
                    tribe.getTagName(),
                    userTribe.getCreatedAt(),
                    tribe.getCounts(),
                    tribe.getVersion(),
                    tribe.getStatus()
            );
        }
        public static TribeRes.TribeListRes toTribeListRes(List<TribeRes.TribeInfo> tribeInfoList){
            return new TribeRes.TribeListRes(tribeInfoList);
        }
    }
}
