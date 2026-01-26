package com.umc.nuvibe.domain.tribe.service.userTribe;

import com.umc.nuvibe.domain.tribe.dto.request.ActiveTribeListReq;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.*;

public interface UserTribeService {

    LeaveRes leaveTribe(Long userId, Long userTribeId);

    UserTribeActivateRes activateUserTribe(Long userId, Long userTribeId);

    UserTribeFavoriteRes toggleFavorite(Long userId, Long userTribeId);

    // 활성화된 트라이브 챗 목록 조회
    ActiveTribeListRes getActiveTribeList(Long userId, ActiveTribeListReq req);

    // 대기 중 트라이브 챗 목록 조회
    WaitingTribeListRes getWaitingTribeList(Long userId, Long cursorTribeId, Integer size);

    // 트라이브 챗 내 채팅 읽음 처리
    TribeReadRes readChat(Long userId, Long tribeId);

}

