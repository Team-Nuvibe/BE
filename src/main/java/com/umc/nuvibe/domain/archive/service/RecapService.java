package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.dto.response.RecapActiveResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapBoardResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapDataResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapTagResponse;
import com.umc.nuvibe.domain.archive.vo.RecapPeriod;

import java.time.LocalDate;
import java.util.List;

public interface RecapService {
    RecapTagResponse getRecapTags(Long userId, RecapPeriod period);
    RecapBoardResponse getRecapBoard(Long userId, RecapPeriod period);
    RecapActiveResponse getRecapActive(Long userId, RecapPeriod period);
    List<LocalDate> getImageDropDates(Long userId, int year, int month);
    RecapDataResponse getImagesByDate(Long userId, LocalDate date);
}
