package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.archive.vo.RecapPeriod;

import java.time.LocalDate;
import java.util.List;

public record RecapBoardResponse(
        RecapPeriod period,
        LocalDate startDate,
        LocalDate endDate,
        long totalDropsCount,
        long boardId,
        String boardName,
        List<String> boardImages
) {

}
