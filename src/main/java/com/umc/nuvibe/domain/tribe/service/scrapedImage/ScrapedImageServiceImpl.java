package com.umc.nuvibe.domain.tribe.service.scrapedImage;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageToggleReq;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageToggleRes;
import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.repository.ScrapedImageRepository;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapedImageServiceImpl implements ScrapedImageService {

    private final ScrapedImageRepository scrapedImageRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;

    @Override
    @Transactional
    public ScrapedImageToggleRes toggleScrapedImage(Long userId, ScrapedImageToggleReq req) {

        // 1. 트라이브 검증
        Tribe tribe = tribeRepository.findById(req.tribeId())
                .orElseThrow(() -> new BusinessException(TribeErrorCode.TRIBE_NOT_FOUND));

        // 2. 이미지 검증
        Image image = imageRepository.findById(req.imageId())
                .orElseThrow(() -> new BusinessException(ImageErrorCode.IMAGE_NOT_FOUND));

        // 3. 유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 4. 유저-트라이브 참여 검증
        if (!userTribeRepository.existsByUser_IdAndTribe_Id(userId, req.tribeId())) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND);
        }

        //스크랩된 이미지가 이미 있을 시 삭제 후 null 반환, 없을 시 저장 후 스크랩 이미지 ID 반환
        return scrapedImageRepository.findByUser_IdAndTribe_IdAndImage_Id(userId, req.tribeId(), req.imageId())
                .map(existing -> {
                    scrapedImageRepository.delete(existing);
                    return new ScrapedImageToggleRes(null);
                })
                .orElseGet(() -> {
                    ScrapedImage saved = scrapedImageRepository.save(ScrapedImage.of(user, tribe, image));
                    return new ScrapedImageToggleRes(saved.getId());
                });

    }

}
