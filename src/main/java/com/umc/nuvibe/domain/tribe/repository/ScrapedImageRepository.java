package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageItemRes;
import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScrapedImageRepository extends JpaRepository<ScrapedImage, Long> {

    //스크랩 이미지 단건 조회
    Optional<ScrapedImage> findByUser_IdAndTribe_IdAndImage_Id(Long userId, Long tribeId, Long imageId);

    //유저의 해당 채팅 이미지 스크랩 여부 조회
    boolean existsByUser_IdAndImage_Id(Long userId, Long imageId);

    //첫 페이지 조회 (태그가 null이면 전체, 있으면 태그별 필터링 / 트라이브 ID 존재 시 방 별 조회)
    @Query("""
        SELECT new com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageItemRes(
            si.id, img.id, COALESCE(img.thumbnailUrl, img.imageUrl), img.imageTag, si.createdAt, c.id, c.user.id, c.user.nickname
            )
        FROM ScrapedImage si
        JOIN si.image img
        JOIN Chat c ON c.image.id = img.id
        WHERE si.user.id = :userId
          AND (:imageTag IS NULL OR img.imageTag = :imageTag)
          AND (:tribeId IS NULL OR si.tribe.id = :tribeId)
        ORDER BY si.createdAt DESC, si.id DESC
    """)
    List<ScrapedImageItemRes> findMyScrapsFirstPage(
            @Param("userId") Long userId,
            @Param("tribeId") Long tribeId,
            @Param("imageTag") ImageTag imageTag,
            Pageable pageable
    );

    //다음 페이지 조회 (커서 조건 필수 + 태그 선택 + 트라이브 ID 선택)
    @Query("""
        SELECT new com.umc.nuvibe.domain.tribe.dto.response.scrapedImage.ScrapedImageItemRes(
            si.id, img.id, COALESCE(img.thumbnailUrl, img.imageUrl), img.imageTag, si.createdAt, c.id, c.user.id, c.user.nickname
            )
        FROM ScrapedImage si
        JOIN si.image img
        JOIN Chat c ON c.image.id = img.id
        WHERE si.user.id = :userId
          AND (:imageTag IS NULL OR img.imageTag = :imageTag)
          AND (:tribeId IS NULL OR si.tribe.id = :tribeId)
          AND (si.createdAt < :cursorCreatedAt 
               OR (si.createdAt = :cursorCreatedAt AND si.id < :cursorId))
        ORDER BY si.createdAt DESC, si.id DESC
    """)
    List<ScrapedImageItemRes> findMyScrapsNextPage(
            @Param("userId") Long userId,
            @Param("tribeId") Long tribeId,
            @Param("imageTag") ImageTag imageTag,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    // 스크랩 이미지 하드 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ScrapedImage si where si.tribe.id = :tribeId")
    void deleteByTribeId(@Param("tribeId") Long tribeId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ScrapedImage si WHERE si.user.id = :userId AND si.tribe.id = :tribeId")
    void deleteAllByUserIdAndTribeId(@Param("userId") Long userId, @Param("tribeId") Long tribeId);

    @Query("""
        SELECT si.image.id FROM ScrapedImage si
        WHERE si.user.id = :userId
          AND si.tribe.id = :tribeId
          AND si.image.id IN :imageIds
    """)
    List<Long> findImageIdsByUserIdAndTribeIdAndImageIds(
            @Param("userId") Long userId,
            @Param("tribeId") Long tribeId,
            @Param("imageIds") List<Long> imageIds
    );
}
