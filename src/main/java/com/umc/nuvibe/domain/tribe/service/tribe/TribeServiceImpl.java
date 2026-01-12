package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.tribe.code.TribeErrorCode;
import com.umc.nuvibe.domain.tribe.converter.TribeConverter;
import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository.UserTribeRepository;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TribeServiceImpl implements TribeService {

    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TribeRes.JoinRes joinOrCreateTribe(Long userId, TribeReq.JoinReq request) {

        String selectedTag = request.imageTag();

        Tribe targetTribe = tribeRepository.findAvailableRooms(selectedTag).stream()
                .findFirst()
                .orElseGet(() -> createNewVersionRoom(selectedTag));

        if (userTribeRepository.existsByUserIdAndTribeId(userId, targetTribe.getId())) {
            throw new BusinessException(TribeErrorCode.ALREADY_JOINED);
        }

        int updatedRows = tribeRepository.incrementCounts(targetTribe.getId());
        if (updatedRows == 0) {
            return joinOrCreateTribe(userId, request);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        userTribeRepository.save(TribeConverter.ToEntity.toUserTribe(user, targetTribe));

        // 알림 기능 구현 시 추가
//        if (targetTribe.getCounts() + 1 == 5 && targetTribe.getStatus() == TribeStatus.INACTIVE) {
//            sendActivationNotification(targetTribe);
//        }

        return TribeConverter.ToResponse.toJoinRes(targetTribe);
    }

    private Tribe createNewVersionRoom(String tagName) {
        int nextVersion = tribeRepository.findTopByTagNameOrderByVersionDesc(tagName)
                .map(Tribe::getVersion)
                .map(v -> v + 1)
                .orElse(1);

        Tribe newTribe = TribeConverter.ToEntity.toTribe(tagName, nextVersion);
        return tribeRepository.save(newTribe);
    }


}
