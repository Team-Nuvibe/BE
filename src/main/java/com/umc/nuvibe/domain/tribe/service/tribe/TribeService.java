package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.tribe.dto.request.TribeJoinReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeJoinRes;

public interface TribeService {

    TribeJoinRes joinOrCreateTribe(Long userId, TribeJoinReq request);

}
