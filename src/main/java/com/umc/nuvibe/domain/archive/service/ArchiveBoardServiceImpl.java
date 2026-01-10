package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.code.ArchiveErrorCode;
import com.umc.nuvibe.domain.archive.converter.ArchiveBoardConverter;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.archive.repository.ArchiveBoardRepository;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchiveBoardServiceImpl implements ArchiveBoardService {

    private final ArchiveBoardRepository archiveBoardRepository;
    private final BoardImageRepository boardImageRepository;

    
    // 보드 목록 조회
    // 사용자의 모든 보드를 조회
    // 각 보드의 썸네일은 가장 최근 이미지
    @Override
    public List<BoardListResponse> getBoards(Long userId) {
        List<ArchiveBoard> boards = archiveBoardRepository.findByUserId(userId);
        
        return boards.stream()
                .map(board -> {
                    // 가장 최근 이미지를 썸네일로 사용
                    String thumbnailUrl = boardImageRepository
                            .findTopByBoardIdOrderByCreatedAtDesc(board.getId())
                            .map(bi -> bi.getImage().getImageUrl())
                            .orElse(null);
                    return ArchiveBoardConverter.toBoardListResponse(board, thumbnailUrl);
                })
                .toList();
    }

    
    // 보드 상세 조회
    // tag가 null이면 전체 이미지 조회 (최신순)
    // tag가 있으면 해당 태그 이미지만 조회 (최신순)
    @Override
    public BoardDetailResponse getBoardDetail(Long userId, Long boardId, ImageTag tag) {
        // 보드 조회 + 권한 체크
        ArchiveBoard board = archiveBoardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.BOARD_NOT_FOUND));
        
        // 태그 유무에 따라 전체/필터 조회
        List<BoardImage> boardImages = (tag == null)
                ? boardImageRepository.findByBoardIdOrderByCreatedAtDesc(boardId)
                : boardImageRepository.findByBoardIdAndImageTagOrderByCreatedAtDesc(boardId, tag);
        
        return ArchiveBoardConverter.toBoardDetailResponse(board, boardImages);
    }
}
