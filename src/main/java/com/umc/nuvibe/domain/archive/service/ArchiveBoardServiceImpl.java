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
    private final ImageRepository imageRepository;

    // 보드 목록 조회
    @Override
    public List<BoardListResponse> getBoards(Long userId, String keyword) {
    // keyword가 null이거나 공백이면 전체 조회, 있으면 검색
    List<ArchiveBoard> boards = (keyword == null || keyword.trim().isEmpty())
            ? archiveBoardRepository.findByUserId(userId)
            : archiveBoardRepository.findByUserIdAndNameContaining(userId, keyword.trim());
        
        if (boards.isEmpty()) {
            return List.of();
        }
        
        // 썸네일 한 번에 조회 (n+1 방지)
        List<Long> boardIds = boards.stream()
                .map(ArchiveBoard::getId)
                .toList();
        
        Map<Long, String> thumbnailMap = boardImageRepository.findLatestByBoardIds(boardIds)
                .stream()
                .collect(Collectors.toMap(
                        bi -> bi.getBoard().getId(),
                        bi -> bi.getImage().getImageUrl(),
                        (existing, replacement) -> existing  // 중복 시 첫 번째 값 유지
                ));
        
        return boards.stream()
                .map(board -> BoardListResponse.from(board, thumbnailMap.get(board.getId())))
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
}
