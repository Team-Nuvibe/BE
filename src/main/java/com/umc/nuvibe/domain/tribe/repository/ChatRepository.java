package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * 특정 imageTag의 최신 트라이브에서 최근 이미지가 있는 Chat 조회 (최대 limit개)
     */
    @Query("""
                SELECT c FROM Chat c
                JOIN FETCH c.image
                JOIN c.tribe t
                WHERE t.imageTag = :imageTag
                AND c.image IS NOT NULL
                ORDER BY c.createdAt DESC
                LIMIT :limit
            """)
    List<Chat> findLatestChatsWithImageByImageTag(@Param("imageTag") ImageTag imageTag, @Param("limit") int limit);

    @Query("SELECT c FROM Chat c JOIN FETCH c.tribe JOIN FETCH c.image WHERE c.id = :chatId")
    Optional<Chat> findByIdWithImageAndTribe(@Param("chatId") Long chatId);

    /**
     * 트라이브 챗 내 채팅 타임라인 첫 페이지 조회 (최신순)
     */
    @Query("""
        SELECT c FROM Chat c
        JOIN FETCH c.user u
        JOIN FETCH c.image i
        WHERE c.tribe.id = :tribeId
        ORDER BY c.id DESC
    """)
    List<Chat> findChatTimelineFirstPage(
            @Param("tribeId") Long tribeId,
            Pageable pageable
    );

    /**
     * 트라이브 챗 내 채팅 타임라인 다음 페이지 조회 (더 과거)
     */
    @Query("""
        SELECT c FROM Chat c
        JOIN FETCH c.user u
        JOIN FETCH c.image i
        WHERE c.tribe.id = :tribeId
          AND c.id < :cursorChatId
        ORDER BY c.id DESC
    """)
    List<Chat> findChatTimelineNextPage(
            @Param("tribeId") Long tribeId,
            @Param("cursorChatId") Long cursorChatId,
            Pageable pageable
    );

    //트라이브 챗 내 채팅 이미지 그리드 첫 페이지 조회
    @Query("""
        select c
        from Chat c
        join fetch c.image i
        where c.tribe.id = :tribeId
          and c.image is not null
        order by c.createdAt desc, c.id desc
    """)
    List<Chat> findChatGridFirstPage(
            @Param("tribeId") Long tribeId,
            Pageable pageable
    );

    /**
     * 트라이브 챗 내 채팅 이미지 그리드 다음 페이지 조회
     * 복합 커서: (createdAt, chatId)
     */
    @Query("""
        select c
        from Chat c
        join fetch c.image i
        where c.tribe.id = :tribeId
          and c.image is not null
          and (
                c.createdAt < :cursorCreatedAt
                or (c.createdAt = :cursorCreatedAt and c.id < :cursorChatId)
          )
        order by c.createdAt desc, c.id desc
    """)
    List<Chat> findChatGridNextPage(
            @Param("tribeId") Long tribeId,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            @Param("cursorChatId") Long cursorChatId,
            Pageable pageable
    );


    boolean existsByIdAndTribe_Id(Long id, Long tribeId);
}
