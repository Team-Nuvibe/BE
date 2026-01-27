package com.umc.nuvibe.domain.archive.service;

import java.util.Map;
import java.util.stream.Collectors;
import com.umc.nuvibe.domain.archive.dto.request.BoardCreateRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardImageDeleteRequest;
import com.umc.nuvibe.domain.archive.dto.request.BoardNameUpdateRequest;
import com.umc.nuvibe.domain.archive.dto.request.*;
import com.umc.nuvibe.domain.archive.dto.response.BoardCreateResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardDetailResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardListResponse;
import com.umc.nuvibe.domain.archive.dto.response.BoardSummaryResponse;
import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.archive.repository.ArchiveBoardRepository;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.ArchiveErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.umc.nuvibe.domain.archive.dto.response.BoardImageResponse;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchiveBoardServiceImpl implements ArchiveBoardService {

    private final ArchiveBoardRepository archiveBoardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    // 보드 목록 조회
    @Override
    public List<BoardListResponse> getBoards(Long userId, String keyword) {
        List<ArchiveBoard> boards = (keyword == null || keyword.trim().isEmpty())
                ? archiveBoardRepository.findByUserId(userId)
                : archiveBoardRepository.findByUserIdAndNameContainingIgnoreCase(userId, keyword.trim());

        if (boards.isEmpty()) {
            return List.of();
        }

        List<Long> boardIds = boards.stream()
                .map(ArchiveBoard::getId)
                .toList();

        // 썸네일 조회 (기존)
        Map<Long, String> thumbnailMap = boardImageRepository.findLatestByBoardIds(boardIds)
                .stream()
                .collect(Collectors.toMap(
                        bi -> bi.getBoard().getId(),
                        bi -> bi.getImage().getImageUrl(),
                        (existing, replacement) -> existing
                ));

        // 태그 개수 조회 (추가)
        Map<Long, Long> tagCountMap = boardImageRepository.countDistinctTagsByBoardIds(boardIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],      // boardId
                        row -> (Long) row[1]       // tagCount
                ));

        return boards.stream()
                .map(board -> BoardListResponse.from(
                        board,
                        thumbnailMap.get(board.getId()),
                        tagCountMap.getOrDefault(board.getId(), 0L).intValue()  // 태그 없으면 0
                ))
                .toList();
    }

    
    // 보드 상세 조회
    @Override
    public BoardDetailResponse getBoardDetail(Long userId, Long boardId, ImageTag tag) {
        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);
        
        List<BoardImage> boardImages = (tag == null)
                ? boardImageRepository.findByBoardIdOrderByCreatedAtDesc(boardId)
                : boardImageRepository.findByBoardIdAndImageTagOrderByCreatedAtDesc(boardId, tag);
        
        return BoardDetailResponse.from(board, boardImages);
    }

    
    // 보드 생성
    @Override
    @Transactional
    public BoardCreateResponse createBoard(Long userId, BoardCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.USER_NOT_FOUND));
        
        String normalizedName = request.name().trim();
        
        // 보드명 중복 체크
        if (archiveBoardRepository.existsByUserIdAndName(userId, normalizedName)) {
            throw new BusinessException(ArchiveErrorCode.DUPLICATE_BOARD_NAME);
        }
        
        // Entity 직접 생성
        ArchiveBoard board = ArchiveBoard.builder()
                .user(user)
                .name(normalizedName)
                .build();
        
        archiveBoardRepository.save(board);
        
        return BoardCreateResponse.from(board);
    }

    
    
    // 보드 삭제 (다중)
    @Override
    @Transactional
    public void deleteBoards(Long userId, BoardDeleteRequest request) {
        // 소유권이 확인된 보드 ID만 조회
        List<Long> ownedBoardIds = archiveBoardRepository
                .findAllByIdInAndUserId(request.boardIds(), userId)
                .stream()
                .map(ArchiveBoard::getId)
                .toList();
        
        if (ownedBoardIds.isEmpty()) {
            return;
        }
        
        // 보드 내 이미지 먼저 삭제 (벌크)
        boardImageRepository.deleteByBoardIdInAndUserId(ownedBoardIds, userId);
        
        // 보드 삭제 (벌크)
        archiveBoardRepository.deleteByIdInAndUserId(ownedBoardIds, userId);
    }

    
    // 보드명 수정
    @Override
    @Transactional
    public void updateBoardName(Long userId, Long boardId, BoardNameUpdateRequest request) {
        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);
        
        String normalizedName = request.name().trim();
        
        // 보드명 중복 체크 (자기 자신 제외)
        if (!board.getName().equals(normalizedName) 
                && archiveBoardRepository.existsByUserIdAndName(userId, normalizedName)) {
            throw new BusinessException(ArchiveErrorCode.DUPLICATE_BOARD_NAME);
        }
        
        board.updateName(normalizedName);
    }

    
    // 보드 내 이미지 삭제 (다중)
    @Override
    @Transactional
    public void deleteBoardImages(Long userId, Long boardId, BoardImageDeleteRequest request) {
        // 보드 권한 체크
        findBoardByIdAndUserId(boardId, userId);
        
        // 이미지 삭제 + 삭제 건수 검증
        int deletedCount = boardImageRepository.deleteByIdInAndBoardId(request.boardImageIds(), boardId);
        
        if (deletedCount == 0) {
            throw new BusinessException(ArchiveErrorCode.BOARD_IMAGE_NOT_FOUND);
        }
    }

    // 보드 조회 + 권한 체크
    private ArchiveBoard findBoardByIdAndUserId(Long boardId, Long userId) {
        return archiveBoardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.BOARD_NOT_FOUND));
    }

    // 사용자가 올린 모든 이미지 조회 (페이징, 최신순)
    @Override
    public Page<BoardImageResponse> getBoardImages(Long userId, Pageable pageable) {
        // 사용자 존재 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.USER_NOT_FOUND));

        Page<BoardImage> boardImages = boardImageRepository
            .findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

        return boardImages.map(BoardImageResponse::from);
    }

    // vibe 톤 입구
    @Override
    @Transactional(readOnly = true) // 조회 성능 최적화 (선택 사항)
    public BoardSummaryResponse getSummary(Long userId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ArchiveErrorCode.USER_NOT_FOUND));

        // 2. Top 4 태그 조회 (Pageable 사용)
        // PageRequest.of(페이지번호, 사이즈) -> 0페이지, 4개 = LIMIT 4 효과
        Pageable limit4 = PageRequest.of(0, 4);

        List<ImageTag> topTags = boardImageRepository.findTopTagsByUserId(userId, limit4);

        return BoardSummaryResponse.of(user.getNickname(), topTags);
    }

    //보드 이미지 추가
    @Override
    @Transactional
    public void addBoardImage(Long userId, Long boardId, BoardImageAddRequest request) {
        //유저가 소유한 보드인지 확인
        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);

        //이미지 id가 존재하는 지 확인
        Image image = imageRepository.findById(request.imageId())
                        .orElseThrow(() -> new BusinessException(ImageErrorCode.IMAGE_NOT_FOUND));

        //이미지가 이미 보드에 저장되어 있는 지 확인
        if (boardImageRepository.existsByImageId(request.imageId())){
            throw new BusinessException(ArchiveErrorCode.BOARD_IMAGE_ALREADY_EXISTS);
        }

        //이미지를 보드에 저장
        boardImageRepository.save(BoardImage.builder().
                board(board).
                image(image).
                build());
    }

    // 채팅 전송용 보드 이미지 추가
    @Override
    @Transactional
    public void addBoardImage(Long userId, Long boardId, Long imageId) {

        ArchiveBoard board = findBoardByIdAndUserId(boardId, userId);

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageErrorCode.IMAGE_NOT_FOUND));

        if (boardImageRepository.existsByImageId(imageId)){
            throw new BusinessException(ArchiveErrorCode.BOARD_IMAGE_ALREADY_EXISTS);
        }

        boardImageRepository.save(BoardImage.builder().
                board(board).
                image(image).
                build());
    }






}
