package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.code.UserTribeErrorCode;
import com.umc.nuvibe.domain.tribe.converter.TribeConverter;
import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.ScrapedImageRepository.ScrapedImageRepository;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository.UserTribeRepository;
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
    public TribeRes.TribeListRes getTribeList(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        List<UserTribe> userTribes = userTribeRepository
                .findAllByUserIdAndTribeStatusOrderByCreatedAtDesc(userId, TribeStatus.ACTIVE);

        List<TribeRes.TribeInfo> tribeInfoList = userTribes.stream()
                .map(TribeConverter.ToResponse::toTribeInfo)
                .toList();

        return TribeConverter.ToResponse.toTribeListRes(tribeInfoList);
    }

    @Override
    @Transactional
    public TribeRes.LeaveRes leaveTribe(Long userId, TribeReq.LeaveReq req) {

        UserTribe userTribe = userTribeRepository.findById(req.userTribeId())
                .orElseThrow(() -> new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND));

        if (!userTribe.getUser().getId().equals(userId)) {
            throw new BusinessException(UserTribeErrorCode.USER_TRIBE_NOT_OWNER); // 본인 것만 삭제 가능
        }

        Long tribeId = userTribe.getTribe().getId();

        // 원본 Image 엔티티는 유지하고 참조만 삭제
        scrapedImageRepository.deleteAllByUserIdAndTribeId(userId, tribeId);

        tribeRepository.decrementCounts(tribeId);

        userTribeRepository.delete(userTribe);

        return new TribeRes.LeaveRes(req.userTribeId(), tribeId);
    }
}
