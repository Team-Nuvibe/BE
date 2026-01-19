package com.umc.nuvibe.domain.tribe.service.chat;

import com.umc.nuvibe.domain.tribe.dto.internal.EmojiAggRow;
import com.umc.nuvibe.domain.tribe.dto.internal.MyEmojiRow;
import com.umc.nuvibe.domain.tribe.dto.request.ChatTimelineReq;
import com.umc.nuvibe.domain.tribe.dto.response.ChatTimelineItemRes;
import com.umc.nuvibe.domain.tribe.dto.response.ChatTimelineListRes;
import com.umc.nuvibe.domain.tribe.dto.response.EmojiSummaryRes;
import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.tribe.repository.ChatRepository;
import com.umc.nuvibe.domain.tribe.repository.EmojiRepository;
import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.global.apiPayLoad.error.ChatErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserTribeErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final EmojiRepository emojiRepository;
    private final UserTribeRepository userTribeRepository;

    @Override
    @Transactional(readOnly = true)
    public ChatTimelineListRes getChatTimelineList(Long userId, Long tribeId, ChatTimelineReq req) {

        // 1. 유저-트라이브 접근 권한 체크
        validateUserInTribe(userId, tribeId);

        // 1-1. 커서 유효성 체크
        if (req.lastChatId() != null) {
            validateCursorChatInTribe(req.lastChatId(), tribeId);
        }

        // 2. size는 dto에서 기본값 처리 (null → 20)
        int limit = req.size();
        Pageable pageable = PageRequest.of(0, limit + 1);

        // 3. lastChatId 여부에 따라 첫 페이지 / 다음 페이지 분기
        List<Chat> chats = (req.lastChatId() == null)
                ? chatRepository.findChatTimelineFirstPage(tribeId, pageable)
                : chatRepository.findChatTimelineNextPage(tribeId, req.lastChatId(), pageable);

        // 4. hasNext 판단
        boolean hasNext = chats.size() > limit;
        List<Chat> pageItems = hasNext ? chats.subList(0, limit) : chats;

        // 5. 채팅들에서 chatIds 추출
        List<Long> chatIds = pageItems.stream()
                .map(Chat::getId)
                .toList();

        // 6. 채팅이 아직 존재하지 않을 시 반환
        if (chatIds.isEmpty()) {
            return new ChatTimelineListRes(List.of(), null, false);
        }

        // 7. 각 채팅당 이모지 집계 및 요약
        Map<Long, List<EmojiSummaryRes>> emojiSummaryMap =
                buildEmojiSummaryMap(chatIds);

        // 8. 내가 누른 이모지 매핑
        Map<Long, EmojiType> myEmojiMap =
                buildMyEmojiMap(userId, chatIds);

        // 9. dto로 매핑
        List<ChatTimelineItemRes> items = pageItems.stream()
                .map(chat -> ChatTimelineItemRes.from(
                        chat,
                        emojiSummaryMap.getOrDefault(chat.getId(), List.of()),
                        myEmojiMap.get(chat.getId())
                ))
                .toList();

        // 10. 다음 페이지 존재 시 마지막 데이터 정보로 커서 설정
        Long nextLastChatId = null;
        if (hasNext && !items.isEmpty()) {
            nextLastChatId = items.get(items.size() - 1).chatId();
        }

        return new ChatTimelineListRes(items, nextLastChatId, hasNext);

    }


    /**
     * 각 chatId에 대해 이모지 타입별 개수를 집계 및 요약한 결과
     * EmojiSummaryRes 목록 형태로 변환
     */
    private Map<Long, List<EmojiSummaryRes>> buildEmojiSummaryMap(List<Long> chatIds) {
        List<EmojiAggRow> rows =
                emojiRepository.countByChatIdGroupByType(chatIds);

        return rows.stream()
                .collect(Collectors.groupingBy(
                        EmojiAggRow::chatId,
                        Collectors.mapping(
                                r -> new EmojiSummaryRes(r.emojiType(), r.count()),
                                Collectors.toList()
                        )
                ));
    }

    /**
     * 특정 유저가 각 chat에 대해 선택한 이모지를 EmojiType 형태로 매핑
     * 이모지는 종류 불문없이 chatId당 최대 1건
     */
    private Map<Long, EmojiType> buildMyEmojiMap(Long userId, List<Long> chatIds) {
        List<MyEmojiRow> rows =
                emojiRepository.findMyEmoji(userId, chatIds);

        return rows.stream()
                .collect(Collectors.toMap(
                        MyEmojiRow::chatId,
                        MyEmojiRow::emojiType
                ));
    }

    // lastChatId 존재 시 해당 커서가 동일한 트라이브 챗 내의 커서인지 확인
    private void validateCursorChatInTribe(Long lastChatId, Long tribeId) {
        if (!chatRepository.existsByIdAndTribe_Id(lastChatId, tribeId)) {
            throw new BusinessException(ChatErrorCode.CHAT_CURSOR_INVALID);
        }
    }

    private void validateUserInTribe(Long userId, Long tribeId) {
        if (!userTribeRepository.existsByUser_IdAndTribe_Id(userId, tribeId)) {
            throw new BusinessException(UserTribeErrorCode.USERTRIBE_NOT_FOUND);
        }
    }
}
