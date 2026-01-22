package com.umc.nuvibe.domain.tribe.service.scrapedImage;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageSliceReq;
import com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageItemRes;
import com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageToggleRes;
import com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageListRes;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.repository.ChatRepository;
import com.umc.nuvibe.domain.tribe.repository.ScrapedImageRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.*;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapedImageServiceImpl implements ScrapedImageService {

    private final ScrapedImageRepository scrapedImageRepository;
    private final UserRepository userRepository;
    private final UserTribeRepository userTribeRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public ScrapedImageToggleRes toggleScrapedImage(Long userId, Long chatId) {

        Chat chat = chatRepository.findByIdWithImageAndTribe(chatId)
                .orElseThrow(() -> new BusinessException(ChatErrorCode.CHAT_NOT_FOUND));

        // 1. 관련 엔티티 조회
        Tribe tribe = chat.getTribe();
        Image image = chat.getImage();
        User user = getUserOrThrow(userId);

        // 2. 해당 트라이브 챗에 대한 유저의 접근 권한 체크
        validateUserInTribe(userId, tribe.getId());

        //스크랩된 이미지가 이미 있을 시 삭제 후 null 반환, 없을 시 저장 후 스크랩 이미지 ID 반환
        return scrapedImageRepository.findByUser_IdAndTribe_IdAndImage_Id(userId, tribe.getId(), image.getId())
                .map(existing -> {
                    scrapedImageRepository.delete(existing);
                    return new ScrapedImageToggleRes(null);
                })
                .orElseGet(() -> {
                    ScrapedImage saved = scrapedImageRepository.save(ScrapedImage.of(user, tribe, image));
                    return new ScrapedImageToggleRes(saved.getId());
                });
    }

    @Override
    @Transactional(readOnly = true)
    public ScrapedImageListRes getTotalScrapedImage(Long userId, ScrapedImageSliceReq req){
        return getScrapedImageInternal(userId, null, req);
    }

    @Override
    @Transactional(readOnly = true)
    public ScrapedImageListRes getTribeScrapedImage(Long userId, Long tribeId, ScrapedImageSliceReq req){
        validateUserInTribe(userId, tribeId);
        return getScrapedImageInternal(userId, tribeId, req);
    }

    private ScrapedImageListRes getScrapedImageInternal(Long userId, Long tribeId, ScrapedImageSliceReq req){

        // 커서 유효성 체크
        if (req.hasCursor() && !req.isCursorComplete()) {
            throw new BusinessException(ScrapedImageErrorCode.SCRAPEDIMAGE_CURSOR_ERROR);
        }

        // 1. hasNext 판단을 위해 요청 size보다 1개 더 조회
        int limit = req.size();
        Pageable pageable = PageRequest.of(0, limit + 1);

        // 2. 첫 페이지인지 여부에 따라 메서드 호출
        List<ScrapedImage> scraps = (req.cursorCreatedAt() == null)
                ? scrapedImageRepository.findMyScrapsFirstPage(userId, tribeId, req.imageTag(), pageable)
                : scrapedImageRepository.findMyScrapsNextPage(userId, tribeId, req.imageTag(), req.cursorCreatedAt(), req.cursorId(), pageable);

        // 3. 다음 페이지 여부 판단 및 다음 페이지 존재 시 1개 더 조회한 데이터 삭제 후 반환
        boolean hasNext = scraps.size() > limit;
        List<ScrapedImage> resultItems = hasNext ? scraps.subList(0, limit) : scraps;

        // 4. dto로 변환
        List<ScrapedImageItemRes> items = resultItems.stream()
                .map(ScrapedImageItemRes::from)
                .toList();

        LocalDateTime nextCursorCreatedAt = null;
        Long nextCursorId = null;

        // 5. 다음 페이지 존재 시 마지막 데이터 정보로 커서 설정
        if (hasNext && !items.isEmpty()) {
            ScrapedImageItemRes lastItem = items.get(items.size() - 1);
            nextCursorCreatedAt = lastItem.createdAt();
            nextCursorId = lastItem.scrapImageId();
        }

        return new ScrapedImageListRes(items, nextCursorCreatedAt, nextCursorId, hasNext);
    }


    // 유저 존재 검증
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    // 해당 트라이브 챗에 대한 유저의 접근 권한 체크
    private void validateUserInTribe(Long userId, Long tribeId) {
        if (!userTribeRepository.existsByUser_IdAndTribe_Id(userId, tribeId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND);
        }
    }

}
