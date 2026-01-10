package com.umc.nuvibe.domain.archive.service;

import com.umc.nuvibe.domain.archive.code.ArchiveErrorCode;
import com.umc.nuvibe.domain.archive.converter.ArchiveBoardConverter;
import com.umc.nuvibe.domain.archive.dto.request.BoardCreateRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardImageDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardNameUpdateRequest;
import com.umc.nuvibe.domain.archive.dto.response.BoardCreateResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.archive.repository.ArchiveBoardRepository;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    
    // 보드 목록 조회
    @Override
    public List<BoardListResponse> getBoards(Long userId) {
        List<ArchiveBoard> boards = archiveBoardRepository.findByUserId(userId);
        
        return boards.stream()
                .map(board -> {
                    String thumbnailUrl = boardImageRepository
                            .findTopByBoardIdOrderByCreatedAtDesc(board.getId())
                            .map(bi -> bi.getImage().getImageUrl())
                            .orElse(null);
                    return ArchiveBoardConverter.toBoardListResponse(board, thumbnailUrl);
                })
                .toList();
    }

    
    // 보드 상세 조회
    @Override
    public BoardDetailResponse getBoardDetail(Long userId, Long boardId, ImageTag tag) {
        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);
        
        List<BoardImage> boardImages = (tag == null)
                ? boardImageRepository.findByBoardIdOrderByCreatedAtDesc(boardId)
                : boardImageRepository.findByBoardIdAndImageTagOrderByCreatedAtDesc(boardId, tag);
        
        return ArchiveBoardConverter.toBoardDetailResponse(board, boardImages);
    }

    
    // 보드 생성
    @Override
    @Transactional
    public BoardCreateResponse createBoard(Long userId, BoardCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.USER_NOT_FOUND));
        
        // 보드명 중복 체크
        if (archiveBoardRepository.existsByUserIdAndName(userId, request.name())) {
            throw new BusinessException(ArchiveErrorCode.DUPLICATE_BOARD_NAME);
        }
        
        ArchiveBoard board = ArchiveBoardConverter.toArchiveBoard(user, request.name());
        archiveBoardRepository.save(board);
        
        return ArchiveBoardConverter.toBoardCreateResponse(board);
    }

    
    // 보드 삭제 (다중)
    // 보드 내 이미지도 함께 삭제
    @Override
    @Transactional
    public void deleteBoards(Long userId, BoardDeleteRequest request) {
        // 보드 내 이미지 먼저 삭제
        boardImageRepository.deleteByBoardIdIn(request.boardIds());
        
        // 보드 삭제
        archiveBoardRepository.deleteByIdInAndUserId(request.boardIds(), userId);
    }

    
    // 보드명 수정
    @Override
    @Transactional
    public void updateBoardName(Long userId, Long boardId, BoardNameUpdateRequest request) {
        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);
        
        // 보드명 중복 체크 (자기 자신 제외)
        if (!board.getName().equals(request.name()) 
                && archiveBoardRepository.existsByUserIdAndName(userId, request.name())) {
            throw new BusinessException(ArchiveErrorCode.DUPLICATE_BOARD_NAME);
        }
        
        board.updateName(request.name());
    }

    
    // 보드 내 이미지 삭제 (다중)
    @Override
    @Transactional
    public void deleteBoardImages(Long userId, Long boardId, BoardImageDeleteRequest request) {
        // 보드 권한 체크
        findBoardByIdAndUserId(boardId, userId);
        
        // 이미지 삭제
        boardImageRepository.deleteByIdInAndUserId(request.boardImageIds(), userId);
    }

    
    // 보드 조회 + 권한 체크
    private ArchiveBoard findBoardByIdAndUserId(Long boardId, Long userId) {
        return archiveBoardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.BOARD_NOT_FOUND));
    }
}
