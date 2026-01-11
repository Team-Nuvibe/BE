package com.umc.nuvibe.domain.tribe.service;

import com.umc.nuvibe.domain.tribe.dto.request.TribeReq;
import com.umc.nuvibe.domain.tribe.dto.response.TribeRes;
import com.umc.nuvibe.domain.tribe.entity.Tribe;

public interface TribeService {

    TribeRes.JoinRes joinOrCreateTribe(Long userId, TribeReq.JoinReq request);

}
