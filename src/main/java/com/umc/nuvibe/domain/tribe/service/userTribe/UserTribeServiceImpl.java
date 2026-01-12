package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.converter.TribeConverter;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
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
}
