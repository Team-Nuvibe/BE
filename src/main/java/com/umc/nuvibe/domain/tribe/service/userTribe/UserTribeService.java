package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.response.LeaveRes;
import com.umc.nuvibe.domain.tribe.dto.response.TribeListRes;
import com.umc.nuvibe.domain.tribe.dto.response.UserTribeActivateRes;

public interface UserTribeService {

    TribeListRes getTribeList(Long userId);

    LeaveRes leaveTribe(Long userId, Long userTribeId);

    UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId);
}

