package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.archive.vo.RecapPeriod;
import com.umc.nuvibe.domain.image.vo.ImageTag;

import java.time.LocalDate;
import java.util.List;

public record RecapTagResponse(
        RecapPeriod period,
        LocalDate startDate,
        LocalDate endDate,
        long totalDropsCount,
        List<TagRankItem> ranks

) {
    public record TagRankItem(
            int rank,
            ImageTag tag
    ){
    }
}
