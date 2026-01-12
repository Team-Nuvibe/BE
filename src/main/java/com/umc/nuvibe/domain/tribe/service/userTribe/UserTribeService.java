package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;

public interface UserTribeService {

    TribeRes.TribeListRes getTribeList(Long userId);

    TribeRes.LeaveRes leaveTribe(Long userId, Long userTribeId);
}

