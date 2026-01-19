package com.umc.nuvibe.domain.archive.dto.response;
import com.umc.nuvibe.domain.archive.vo.RecapPeriod;

import java.time.LocalDate;

public record RecapActiveResponse (
        RecapPeriod period,
        LocalDate startDate,
        LocalDate endDate,
        String dayMessage,
        String preferenceMessage,
        String timeMessage,
        long totalBoardCount,
        long totalTagCount,
        long maxDailyDropCount
){

}
