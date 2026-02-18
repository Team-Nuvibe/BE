package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.dto.response.RecapActiveResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapBoardResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapDataResponse;
import com.umc.nuvibe.domain.archive.dto.response.RecapTagResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.repository.ArchiveBoardRepository;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.archive.vo.RecapMessage;
import com.umc.nuvibe.domain.archive.vo.RecapPeriod;
import com.umc.nuvibe.domain.archive.vo.TimeRange;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.RecapErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecapServiceImpl implements RecapService {

    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final ArchiveBoardRepository archiveBoardRepository;

    //기간 계산 함수
    private TimeRange calculateStartTime(RecapPeriod period, Long userId){

        if (period == RecapPeriod.WEEK){
            LocalDate startWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
            LocalDateTime start = startWeek.atStartOfDay();
            LocalDateTime end = startWeek.plusWeeks(1).atStartOfDay();
            return new TimeRange(start, end);

        } else {
            LocalDateTime start = userRepository.findById(userId)
                    .map(User::getCreatedAt)
                    .orElseThrow(()->new BusinessException(UserErrorCode.USER_NOT_FOUND));
            LocalDateTime end = LocalDateTime.now();
            return new TimeRange(start, end);
        }


    }

    //사용자가 드랍한 이미지가 있는 지 확인
    private Long validateHasData(Long userId, LocalDateTime startTime, LocalDateTime endTime){
        Long totalDrops = boardImageRepository.countTotalImageByPeriod(userId, startTime, endTime);
        if (totalDrops == 0){
            throw new BusinessException(RecapErrorCode.NOT_ENOUGH_DATA);
        }
        return totalDrops;
    }

    //자주 사용한 태그 조회
    @Override
    public RecapTagResponse getRecapTags(Long userId, RecapPeriod period) {

        TimeRange range = calculateStartTime(period, userId);
        LocalDateTime start =  range.start();
        LocalDateTime end = range.end();

        //총 drop한 이미지 개수
        Long totalDrops = validateHasData(userId, start, end);

        //많이 사용한 상위 5개 태그 리스트
        List<ImageTag> topTags = boardImageRepository.findTopTagsByPeriod(
                userId, start, end, PageRequest.of(0, 5));

        List<RecapTagResponse.TagRankItem> ranks = IntStream.range(0, topTags.size())
                .mapToObj(i -> new RecapTagResponse.TagRankItem(
                        i + 1,
                        topTags.get(i)
                ))
                .toList();

        LocalDate endDate = (period == RecapPeriod.WEEK)
                ? end.toLocalDate().minusDays(1)
                : end.toLocalDate();

     return new RecapTagResponse(
             period,
             start.toLocalDate(),
             endDate,
             totalDrops,
             ranks
     );
    }

    //가장 많이 사용한 보드 조회
    @Override
    public RecapBoardResponse getRecapBoard(Long userId, RecapPeriod period) {
        TimeRange range = calculateStartTime(period, userId);
        LocalDateTime start =  range.start();
        LocalDateTime end = range.end();

        validateHasData(userId, start, end);

        //가장 많이 사용한 보드 조회
        BoardImageRepository.TopBoardProjection topBoardProjection = boardImageRepository.findTopBoardByPeriod(userId, start, end);

        //보드 조회
        ArchiveBoard board = archiveBoardRepository.findById(topBoardProjection.getBoardId())
                .orElseThrow(()->new BusinessException(RecapErrorCode.NOT_ENOUGH_DATA));

        //보드가 사용된 횟수
        Long count = topBoardProjection.getCount();

        //보드 이미지
        List<String> boardImageUrls = boardImageRepository.findTopByBoardIdOrderByCreatedAtDesc(
                board.getId(), PageRequest.of(0, 3))
                .stream()
                .map(bi -> bi.getImage().getThumbnailUrl()) // 썸네일 URL 반환
                .toList();

        LocalDate endDate = (period == RecapPeriod.WEEK)
                ? end.toLocalDate().minusDays(1)
                : end.toLocalDate();

            return new RecapBoardResponse(
                    period,
                    start.toLocalDate(),
                    endDate,
                    count,
                    board.getId(),
                    board.getName(),
                    boardImageUrls
            );
    }

    //사용자 업로드 패턴 조회
    @Override
    public RecapActiveResponse getRecapActive(Long userId, RecapPeriod period) {
        TimeRange range = calculateStartTime(period, userId);
        LocalDateTime start =  range.start();
        LocalDateTime end = range.end();

        validateHasData(userId, start, end);

        //사용한 아카이브 보드 수
        long boardCount = boardImageRepository.countActiveBoardsByPeriod(userId, start, end);

        //사용한 태그 수
        long tagCount = boardImageRepository.countTotalTagByPeriod(userId, start, end);

        //하루 최대 드랍 수
        long maxDropCount = boardImageRepository.findMaxDailyDropCount(userId, start, end);

        //가장 많은 드랍 한 요일
        String topDayName = boardImageRepository.findTopDayOfWeekByPeriod(userId, start, end);
        if (topDayName == null){
            throw new BusinessException(RecapErrorCode.NOT_ENOUGH_DATA);
        }
        String dayMessage = RecapMessage.Day.from(topDayName).getMessage(period);

        //취향 성향(태그 수와 보드 수 비교)
        RecapMessage.Preference preference = RecapMessage.Preference.calculate(tagCount, boardCount);
        String preferenceMessage = preference.getMessage(period);

        //가장 많이 드랍한 시간대
        Integer topHour = boardImageRepository.findTopHourByPeriod(userId, start, end);
        if (topHour == null){
            throw new BusinessException(RecapErrorCode.NOT_ENOUGH_DATA);
        }
        String timeSlotMessage = RecapMessage.TimeSlot.from(topHour).getMessage(period);

        LocalDate endDate = (period == RecapPeriod.WEEK)
                ? end.toLocalDate().minusDays(1)
                : end.toLocalDate();

        return new RecapActiveResponse(
                period,
                start.toLocalDate(),
                endDate,
                dayMessage,
                preferenceMessage,
                timeSlotMessage,
                boardCount,
                tagCount,
                maxDropCount
        );
    }

    //해당 달에 업로드한 날자
    @Override
    public List<LocalDate> getImageDropDates(Long userId, int year, int month){
        if (month > 12 || month < 1){
            throw new BusinessException(RecapErrorCode.INVALID_MONTH);
        }

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);

        return boardImageRepository.findImageDropDatesByMonth(userId, start, end)
                .stream()
                .map(java.sql.Date::toLocalDate)
                .toList();
    }

    //해당 날자의 업로드 이미지와 지난달의 오늘
    @Override
    public RecapDataResponse getImagesByDate(Long userId, LocalDate date){
        List<RecapDataResponse.ImageDetail> todayImages =
                boardImageRepository.findImagesByDate(userId, date)
                        .stream()
                        .map(RecapDataResponse.ImageDetail::from)
                        .toList();


        LocalDate lastMonth = date.minusMonths(1);
        List<RecapDataResponse.ImageDetail> lastMonthImageList =
                boardImageRepository.findImagesByDate(userId, lastMonth)
                        .stream()
                        .map(RecapDataResponse.ImageDetail::from)
                        .toList();

        return new RecapDataResponse(lastMonthImageList, todayImages);
    }
}
