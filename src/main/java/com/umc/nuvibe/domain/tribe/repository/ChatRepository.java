package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.Chat;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
