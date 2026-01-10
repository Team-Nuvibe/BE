package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.image.vo.ImageTag;

import java.util.List;

public interface ArchiveBoardService {
    
    // 보드 목록 조회
    List<BoardListResponse> getBoards(Long userId);
    
    // 보드 상세 조회 (태그 필터 옵션)
    BoardDetailResponse getBoardDetail(Long userId, Long boardId, ImageTag tag);
}
