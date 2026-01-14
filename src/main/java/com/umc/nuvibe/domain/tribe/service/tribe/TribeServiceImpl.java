package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.tribe.dto.request.TribeJoinReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeJoinRes;
import com.umc.nuvibe.global.apiPayLoad.error.TribeErrorCode;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
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
    public TribeJoinRes joinOrCreateTribe(Long userId, TribeJoinReq request) {
        String selectedTag = request.imageTag();

        if (userTribeRepository.existsByUserIdAndTribe_TagName(userId, selectedTag)) {
            throw new BusinessException(TribeErrorCode.ALREADY_JOINED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        Tribe targetTribe = tribeRepository.findAvailableRoom(selectedTag)
                .orElseGet(() -> createNewVersionRoom(selectedTag));

        int updatedRows = tribeRepository.incrementCounts(targetTribe.getId());

        if (updatedRows == 0) {
            throw new BusinessException(TribeErrorCode.TRIBE_FULL_RETRY);
        }

        UserTribe userTribe = UserTribe.of(user, targetTribe);
        userTribeRepository.save(userTribe);

        return TribeJoinRes.from(targetTribe, userTribe);
        }


    private Tribe createNewVersionRoom(String tagName) {
            int nextVersion =tribeRepository.findTopByTagNameOrderByVersionDesc(tagName)
                    .map(Tribe::getVersion)
                    .map(v -> v + 1)
                    .orElse(1);

            Tribe newTribe = Tribe.create(tagName, nextVersion);
            return tribeRepository.save(newTribe);
    }


}
