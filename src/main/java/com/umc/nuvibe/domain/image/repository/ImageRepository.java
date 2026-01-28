package com.umc.nuvibe.domain.image.repository;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageStatus;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // 특정 태그의 이미지 1개 조회 (랜덤용 - 가장 최신)
    Optional<Image> findTopByImageTagOrderByIdDesc(ImageTag imageTag);

    // 특정 태그의 이미지 목록 조회
    List<Image> findByImageTagOrderByIdDesc(ImageTag imageTag);

    // 특정 태그들의 이미지 조회 (최대 limit개)
    @Query("""
                SELECT i FROM Image i
                WHERE i.imageTag IN :tags
                ORDER BY i.id DESC
                LIMIT :limit
            """)
    List<Image> findByTagsWithLimit(@Param("tags") List<ImageTag> tags, @Param("limit") int limit);

    Optional<Image> findByFileName(String fileName);

    // PENDING 상태이면서 생성 시간이 특정 시간보다 오래된 이미지 조회
    List<Image> findByStatusAndCreatedAtBefore(ImageStatus status, LocalDateTime dateTime);
}
