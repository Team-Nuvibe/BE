package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.notification.service.FcmService;
import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.tribe.dto.request.TribeJoinReq;
import com.umc.nuvibe.domain.tribe.dto.response.tribe.TribeJoinRes;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TribeServiceImpl implements TribeService {

    private final TribeRepository tribeRepository;
    private final UserTribeRepository userTribeRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;


    @Override
    @Transactional
    public TribeJoinRes joinOrCreateTribe(Long userId, TribeJoinReq request) {
        ImageTag selectedTag = request.imageTag();

        if (userTribeRepository.existsByUser_IdAndTribe_ImageTag(userId, selectedTag)) {
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

        Tribe tribe = tribeRepository.findById(targetTribe.getId())
                .orElseThrow(() -> new BusinessException(TribeErrorCode.TRIBE_NOT_FOUND));

        if (tribe.getCounts() >= 5) {
            tribe.changeStatus();

            // NOTI-01: 동일 태그로 매칭된 사용자들에게 알림 발송
            List<User> matchedUsers = userTribeRepository.findUsersByTribeId(tribe.getId());
            fcmService.sendNotificationToUsers(
                    matchedUsers,
                    NotificationType.NOTI_01,
                    tribe.getImageTag().name(),  // tag
                    tribe.getId()                 // relatedId
            );
        }

        UserTribe userTribe = UserTribe.of(user, tribe);
        userTribeRepository.save(userTribe);

        return TribeJoinRes.from(tribe, userTribe);
        }


    private Tribe createNewVersionRoom(ImageTag imageTag) {
            int nextVersion =tribeRepository.findTopByImageTagOrderByVersionDesc(imageTag)
                    .map(Tribe::getVersion)
                    .map(v -> v + 1)
                    .orElse(1);

            Tribe newTribe = Tribe.create(imageTag, nextVersion);
            return tribeRepository.save(newTribe);
    }


}
