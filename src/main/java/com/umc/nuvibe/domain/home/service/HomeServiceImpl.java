package com.umc.nuvibe.domain.home.service;

import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.repository.ArchiveBoardRepository;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.home.dto.CategoryTagResponse;
import com.umc.nuvibe.domain.home.dto.DropMissionResponse;
import com.umc.nuvibe.domain.home.dto.MyBoardResponse;
import com.umc.nuvibe.domain.home.dto.TagDetailResponse;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.repository.ChatRepository;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {

    private final ArchiveBoardRepository archiveBoardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ImageRepository imageRepository;
    private final ChatRepository chatRepository;
    private final TribeRepository tribeRepository;

    private static final int MAX_BOARDS = 5;
    private static final int MAX_CATEGORY_TAGS = 15;
    private static final int TRIBE_IMAGE_COUNT = 5;

    @Override
    public DropMissionResponse getDropMission() {
        // 1. 모든 태그 중 랜덤 선택
        ImageTag[] allTags = ImageTag.values();
        List<ImageTag> shuffledTags = new ArrayList<>(Arrays.asList(allTags));
        Collections.shuffle(shuffledTags);

        // 2. 이미지가 있는 태그 찾기
        for (ImageTag tag : shuffledTags) {
            Optional<Image> imageOpt = imageRepository.findTopByImageTagOrderByIdDesc(tag);
            if (imageOpt.isPresent()) {
                Image image = imageOpt.get();
                return new DropMissionResponse(
                        tag,
                        image.getId(),
                        image.getImageUrl());
            }
        }

        // 이미지가 없는 경우 랜덤 태그만 반환
        ImageTag randomTag = allTags[new Random().nextInt(allTags.length)];
        return new DropMissionResponse(randomTag, null, null);
    }

    @Override
    public List<MyBoardResponse> getMyBoards(Long userId) {
        // 1. 사용자의 보드 조회
        List<ArchiveBoard> boards = archiveBoardRepository.findByUserId(userId);

        if (boards.isEmpty()) {
            return List.of();
        }

        // 2. 보드 ID 목록
        List<Long> boardIds = boards.stream().map(ArchiveBoard::getId).toList();

        // 3. 각 보드의 최신 썸네일 조회
        Map<Long, String> thumbnailMap = boardImageRepository.findLatestByBoardIds(boardIds)
                .stream()
                .collect(Collectors.toMap(
                        bi -> bi.getBoard().getId(),
                        bi -> bi.getImage().getImageUrl(),
                        (existing, replacement) -> existing));

        // 4. 각 보드의 가장 오래된 이미지 업로드 시간 조회
        Map<Long, java.time.LocalDateTime> oldestUploadMap = boardImageRepository
                .findOldestCreatedAtByBoardIds(boardIds).stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> (java.time.LocalDateTime) arr[1]));

        // 5. 가장 오래된 이미지 업로드 시간 기준 정렬 (오래된 순, 이미지 없는 보드는 맨 뒤)
        return boards.stream()
                .sorted(Comparator.comparing(
                        (ArchiveBoard b) -> oldestUploadMap.get(b.getId()),
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(MAX_BOARDS)
                .map(board -> new MyBoardResponse(
                        board.getId(),
                        board.getName(),
                        thumbnailMap.get(board.getId())))
                .toList();
    }

    @Override
    public List<CategoryTagResponse> getCategoryTags(ImageTagCategory category) {
        // 1. 해당 카테고리의 태그 목록 조회
        List<ImageTag> tagsInCategory = Arrays.stream(ImageTag.values())
                .filter(tag -> tag.getImageTagCategory() == category)
                .toList();

        if (tagsInCategory.isEmpty()) {
            return List.of();
        }

        // 2. 셔플하여 최대 15개 선택
        List<ImageTag> shuffledTags = new ArrayList<>(tagsInCategory);
        Collections.shuffle(shuffledTags);
        List<ImageTag> selectedTags = shuffledTags.stream()
                .limit(MAX_CATEGORY_TAGS)
                .toList();

        // 3. 각 태그별 대표 이미지 조회
        return selectedTags.stream()
                .map(tag -> {
                    Optional<Image> imageOpt = imageRepository.findTopByImageTagOrderByIdDesc(tag);
                    return new CategoryTagResponse(
                            tag,
                            imageOpt.map(Image::getImageUrl).orElse(null));
                })
                .toList();
    }

    @Override
    public TagDetailResponse getTagDetail(ImageTag tag) {
        // 1. 해당 태그의 트라이브 채팅 이미지 조회
        List<Chat> chats = chatRepository.findLatestChatsWithImageByTagName(
                tag.name(), TRIBE_IMAGE_COUNT);

        List<String> tribeImageUrls = chats.stream()
                .map(chat -> chat.getImage().getImageUrl())
                .toList();

        // 2. 트라이브 ID 조회
        Long tribeId = tribeRepository.findLatestTribeIdByTagName(tag.name());

        return new TagDetailResponse(
                tag,
                tag.getDescription(),
                tag.getImageTagCategory(),
                tribeImageUrls,
                !tribeImageUrls.isEmpty(),
                tribeId);
    }
}
