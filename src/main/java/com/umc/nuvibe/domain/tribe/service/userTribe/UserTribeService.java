package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.response.userTribe.LeaveRes;
import com.umc.nuvibe.domain.tribe.dto.response.tribe.TribeListRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeActivateRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeFavoriteRes;

public interface UserTribeService {

    TribeListRes getTribeList(Long userId);

    LeaveRes leaveTribe(Long userId, Long userTribeId);

    UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId);

    UserTribeFavoriteRes toggleFavorite(Long userId, Long userTribeId);
}

