package com.umc.nuvibe.domain.tribe.service.chat;

import com.umc.nuvibe.domain.tribe.dto.request.ChatGridReq;
import com.umc.nuvibe.domain.tribe.dto.request.ChatTimelineReq;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatDetailRes;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatGridListRes;
import com.umc.nuvibe.domain.tribe.dto.response.chat.ChatTimelineListRes;

public interface ChatService {

    /**
     * 채팅방 내 타임라인 조회 (최신순)
     * 발신자 정보, 각 채팅 이모지 요약 통계, 내 이모지 표현 포함 반환
     */
    ChatTimelineListRes getChatTimelineList(Long userId, Long tribeId, ChatTimelineReq req);

    // 트라이브 챗 내 채팅 이미지 그리드 목록 조회
    ChatGridListRes getChatGridList(Long userId, Long tribeId, ChatGridReq req);

    //채팅 이미지 상세 조회
    ChatDetailRes getChatDetail(Long userId, Long chatId);
}
