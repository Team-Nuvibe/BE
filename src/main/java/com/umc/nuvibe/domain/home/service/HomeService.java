package com.umc.nuvibe.domain.home.service;

import com.umc.nuvibe.domain.home.dto.CategoryTagResponse;
import com.umc.nuvibe.domain.home.dto.DropMissionResponse;
import com.umc.nuvibe.domain.home.dto.MyBoardResponse;
import com.umc.nuvibe.domain.home.dto.TagDetailResponse;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;

import java.util.List;

public interface HomeService {

    /**
     * 오늘의 드롭미션 조회
     * 랜덤 소분류 태그 + 이미지 1개
     */
    DropMissionResponse getDropMission();

    /**
     * 나의 기록(보드) 목록 조회
     * 최대 5개, 오래된 이미지 업로드 보드 순
     */
    List<MyBoardResponse> getMyBoards(Long userId);

    /**
     * 대분류 카테고리별 소분류 태그 목록 조회
     * 최대 15개 랜덤, 태그 + 대표 이미지
     */
    List<CategoryTagResponse> getCategoryTags(ImageTagCategory category);

    /**
     * 소분류 태그 상세 조회
     * 태그 정보 + 트라이브 채팅 이미지 (최대 5개)
     */
    TagDetailResponse getTagDetail(ImageTag tag);
}
