package com.umc.nuvibe.domain.tribe.service.tribe;

import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;

public interface TribeService {

    TribeRes.JoinRes joinOrCreateTribe(Long userId, TribeReq.JoinReq request);

}
