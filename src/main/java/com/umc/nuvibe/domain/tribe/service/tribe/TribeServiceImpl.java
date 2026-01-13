package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.tribe.code.TribeErrorCode;
import com.umc.nuvibe.domain.tribe.converter.TribeConverter;
import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.tribeRepository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.userTribeRepository.UserTribeRepository;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TribeServiceImpl implements TribeService {

    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final UserRepository userRepository;

    private static final int MAX_RETRIES = 3;

    @Override
    @Transactional
    public TribeRes.JoinRes joinOrCreateTribe(Long userId, TribeReq.JoinReq request) {

        String selectedTag = request.imageTag();
        int retryCount = 0;

        if (userTribeRepository.existsByUserIdAndTribe_TagName(userId, selectedTag)) {
            throw new BusinessException(TribeErrorCode.ALREADY_JOINED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        while (retryCount < MAX_RETRIES) {
            try {
                Tribe targetTribe = tribeRepository.findAvailableRooms(selectedTag).stream()
                        .findFirst()
                        .orElseGet(() -> createNewVersionRoom(selectedTag));


                int updatedRows = tribeRepository.incrementCounts(targetTribe.getId());

                if (updatedRows > 0) {

                    Tribe updatedTribe = tribeRepository.findById(targetTribe.getId())
                            .orElseThrow(() -> new BusinessException(TribeErrorCode.TRIBE_NOT_FOUND));

                    UserTribe updatedUserTribe = TribeConverter.ToEntity.toUserTribe(user, updatedTribe);

                    userTribeRepository.save(updatedUserTribe);

                    // 알림 기능 구현 시 추가
//              if (targetTribe.getCounts() + 1 == 5 && targetTribe.getStatus() == TribeStatus.INACTIVE) {
//                  sendActivationNotification(targetTribe);
//              }
                    return TribeConverter.ToResponse.toJoinRes(updatedTribe, updatedUserTribe);
                }
            } catch (BusinessException e){
                if (e.getErrorCode() == TribeErrorCode.ALREADY_CREATED_VERSION){
                    retryCount++;
                    continue;
                }
                throw e;
            }
            retryCount++;
        }

        throw new BusinessException(TribeErrorCode.TRIBE_JOIN_RETRY_EXCEEDED);
    }

    private Tribe createNewVersionRoom(String tagName) {
        try{
            int nextVersion = tribeRepository.findTopByTagNameOrderByVersionDesc(tagName)
                    .map(Tribe::getVersion)
                    .map(v -> v + 1)
                    .orElse(1);

            Tribe newTribe = TribeConverter.ToEntity.toTribe(tagName, nextVersion);
            return tribeRepository.save(newTribe);
        } catch (DataIntegrityViolationException e){
            throw new BusinessException(TribeErrorCode.ALREADY_CREATED_VERSION);
        }
    }


}
