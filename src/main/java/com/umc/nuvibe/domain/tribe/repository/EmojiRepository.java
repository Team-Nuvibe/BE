package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.dto.internal.EmojiAggRow;
import com.umc.nuvibe.domain.tribe.dto.internal.MyEmojiRow;
import com.umc.nuvibe.domain.tribe.entity.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

    /**
     * 각 채팅에 대한 이모지 타입별 집계
     * count가 0인 이모지 타입은 결과 생성 X
     */
    @Query("""
        SELECT new com.umc.nuvibe.domain.tribe.dto.internal.EmojiAggRow(
            e.chat.id,
            e.type,
            COUNT(e.id)
        )
        FROM Emoji e
        WHERE e.chat.id IN :chatId
        GROUP BY e.chat.id, e.type
    """)
    List<EmojiAggRow> countByChatIdGroupByType(
            @Param("chatId") List<Long> chatId
    );

    /**
     * 내가 누른 이모지 조회
     * 이모지는 종류 불문없이 chatId당 최대 1건
     */
    @Query("""
        SELECT new com.umc.nuvibe.domain.tribe.dto.internal.MyEmojiRow(
            e.chat.id,
            e.type
        )
        FROM Emoji e
        WHERE e.user.id = :userId
          AND e.chat.id IN :chatId
    """)
    List<MyEmojiRow> findMyEmoji(
            @Param("userId") Long userId,
            @Param("chatId") List<Long> chatId
    );
}
