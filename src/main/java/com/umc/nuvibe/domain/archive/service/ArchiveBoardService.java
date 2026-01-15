package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.dto.request.*;
import com.umc.nuvibe.domain.archive.dto.response.BoardCreateResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardImageResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArchiveBoardService {
    
    // 보드 목록 조회
    List<BoardListResponse> getBoards(Long userId, String keyword);
    
    // 보드 상세 조회 (태그 필터 옵션)
    BoardDetailResponse getBoardDetail(Long userId, Long boardId, ImageTag tag);

    // 보드 생성
    BoardCreateResponse createBoard(Long userId, BoardCreateRequest request);

    // 보드 삭제(다중 삭제)
    void deleteBoards(Long userId, BoardDeleteRequest request);

    // 보드명 수정
    void updateBoardName(Long userId, Long boardId, BoardNameUpdateRequest request);

    // 보드 내 이미지 삭제(다중)
    void deleteBoardImages(Long userId, Long boardId, BoardImageDeleteRequest request);

    // 사용자가 올린 모든 이미지 조회 (페이징, 최신순)
    Page<BoardImageResponse> getBoardImages(Long userId, Pageable pageable);

    // 보드 내 이미지 추가
    void addBoardImage(Long userId, Long boardId, BoardImageAddRequest request);

}
