package com.umc.nuvibe.domain.tribe.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TribeReq {

    public record JoinReq(
            @NotBlank
            String imageTag
    ){}

    public record TribeListReq(

            //즐겨찾기 여부
//            Interger lastFavoriteValue,

            //정렬 기준 시각(챗 입장 시각 또는 최신 메시지 시각)
//            String lastValue,
//
//            Long lastUserTribeId,

            SortType sortType

//            @Min(1) @Max(10)
//            Integer size
    ){
        public TribeListReq {
            if (sortType == null) sortType = SortType.LATEST_JOIN;
        }
    }

    public enum SortType {
        LATEST_JOIN,

        //추후 추가
//        LATEST_MESSAGE
//        FAVORITE
    }

    public record LeaveReq(
            @NotNull
            Long userTribeId
    ){}
}
