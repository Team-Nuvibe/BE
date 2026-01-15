package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.response.*;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.ScrapedImageRepository;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTribeServiceImpl implements UserTribeService {

    private final UserTribeRepository userTribeRepository;
    private final UserRepository userRepository;
    private final ScrapedImageRepository scrapedImageRepository;
    private final TribeRepository tribeRepository;

    @Override
    @Transactional(readOnly = true)
    public TribeListRes getTribeList(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        List<UserTribe> userTribes = userTribeRepository
                .findAllByUserIdAndTribe_StatusOrderByCreatedAtDesc(userId, TribeStatus.WAITING);

        List<TribeInfo> tribeInfoList = userTribes.stream()
                .map(TribeInfo::from)
                .toList();

        return TribeListRes.of(tribeInfoList);
    }

    @Override
    @Transactional
    public LeaveRes leaveTribe(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED); // 본인 것만 삭제 가능
        }

        Long tribeId = userTribe.getTribe().getId();

        // 원본 Image 엔티티는 유지하고 참조만 삭제
        scrapedImageRepository.deleteAllByUserIdAndTribeId(userId, tribeId);

        userTribeRepository.delete(userTribe);
        tribeRepository.decrementCounts(tribeId);

        return new LeaveRes(userTribeId, tribeId);
    }

    @Override
    @Transactional
    public UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED);
        }

        if (userTribe.getUserTribeStatus() == UserTribeStatus.ACTIVE) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_ALREADY_ACTIVE);
        }

        if (userTribe.getTribe().getStatus() != TribeStatus.WAITING) {
            throw new BusinessException(TribeErrorCode.ACTIVATION_NOT_READY);
        }

        userTribe.activate();

        return UserTribeActivateRes.from(userTribe);
    }

    @Override
    @Transactional
    public UserTribeFavoriteRes toggleFavorite(Long userId, Long userTribeId) {

        UserTribe userTribe = userTribeRepository.findById(userTribeId)
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_JOINED);
        }

        userTribe.toggleFavorite();

        return UserTribeFavoriteRes.from(userTribe);
    }
}
