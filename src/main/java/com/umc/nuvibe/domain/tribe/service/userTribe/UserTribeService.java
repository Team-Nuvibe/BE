package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.request.ActiveTribeListReq;
import com.umc.nuvibe.domain.tribe.dto.response.tribe.ActiveTribeListRes;
import com.umc.nuvibe.domain.tribe.dto.response.tribe.WaitingTribeListRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.LeaveRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeActivateRes;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.UserTribeFavoriteRes;

public interface UserTribeService {

    LeaveRes leaveTribe(Long userId, Long userTribeId);

    UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId);

    UserTribeFavoriteRes toggleFavorite(Long userId, Long userTribeId);

    ActiveTribeListRes getActiveTribeList(Long userId, ActiveTribeListReq req);

    WaitingTribeListRes getWaitingTribeList(Long userId, Long cursorTribeId, Integer size);
}

