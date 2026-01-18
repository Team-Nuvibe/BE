package com.umc.nuvibe.domain.tribe.service.scrapedImage;

import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageSliceReq;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageToggleRes;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageListRes;

public interface ScrapedImageService {

    /**
     * 이미지 스크랩 토글
     * 스크랩이 없을 시 스크랩 이미지 생성, 이미 존재할 시 삭제
     */
    ScrapedImageToggleRes toggleScrapedImage(Long userId, Long chatId);

    /**
     * 스크랩한 전체 이미지 목록 조회
     * 태그 없을 시 전체 최신 순 조회
     * 태그 존재 시 태그별 조회
     */
    ScrapedImageListRes getTotalScrapedImage(Long userId, ScrapedImageSliceReq req);

    /**
     * 해당 트라이브 챗 내에서 스크랩한 이미지 목록 조회
     * 기본 최신 순 조회
     */
    ScrapedImageListRes getTribeScrapedImage(Long userId, Long tribeId, ScrapedImageSliceReq req);
}
