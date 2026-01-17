package com.umc.nuvibe.domain.archive.repository;

import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    
    // 보드 내 전체 이미지 조회 (최신순)
    @Query("SELECT bi FROM BoardImage bi " +
           "JOIN FETCH bi.image " +
           "WHERE bi.board.id = :boardId " +
           "ORDER BY bi.createdAt DESC")
    List<BoardImage> findByBoardIdOrderByCreatedAtDesc(@Param("boardId") Long boardId);
    
    // 보드 내 태그별 이미지 조회 (최신순)
    @Query("SELECT bi FROM BoardImage bi " +
           "JOIN FETCH bi.image i " +
           "WHERE bi.board.id = :boardId AND i.imageTag = :tag " +
           "ORDER BY bi.createdAt DESC")
    List<BoardImage> findByBoardIdAndImageTagOrderByCreatedAtDesc(
            @Param("boardId") Long boardId,
            @Param("tag") ImageTag tag);
    
    // 여러 보드의 최신 썸네일 한 번에 조회 (N+1 방지)
    @Query("SELECT bi FROM BoardImage bi " +
              "JOIN FETCH bi.image " +
              "WHERE bi.board.id IN :boardIds " +
              "AND bi.createdAt = (SELECT MAX(bi2.createdAt) FROM BoardImage bi2 WHERE bi2.board.id = bi.board.id)")
    List<BoardImage> findLatestByBoardIds(@Param("boardIds") List<Long> boardIds);
    
    // 여러 보드의 이미지 삭제 (다중 보드 삭제용)
    @Modifying(clearAutomatically = true) // 캐시 삭제
    @Query("DELETE FROM BoardImage bi WHERE bi.board.id IN :boardIds AND bi.board.user.id = :userId")
    void deleteByBoardIdInAndUserId(@Param("boardIds") List<Long> boardIds, @Param("userId") Long userId);

    // 보드 내 이미지 삭제 (반환: 삭제 건수)
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BoardImage bi WHERE bi.id IN :imageIds AND bi.board.id = :boardId")
    int deleteByIdInAndBoardId(@Param("imageIds") List<Long> imageIds, @Param("boardId") Long boardId);

    // 사용자가 올린 모든 이미지 조회 (페이징, 최신순)
    @EntityGraph(attributePaths = {"image", "board"})
    @Query("SELECT bi FROM BoardImage bi " +
    "WHERE bi.board.user.id = :userId " +
    "ORDER BY bi.createdAt DESC")
    Page<BoardImage> findAllByUserIdOrderByCreatedAtDesc(
       @Param("userId") Long userId,
       Pageable pageable
     );

    // 사용자가 가장 많이 사용한 태그 Top 4 조회
    @Query("SELECT i.imageTag " +
              "FROM BoardImage bi " +
              "JOIN bi.image i " +
              "JOIN bi.board b " +
              "WHERE b.user.id = :userId AND i.imageTag IS NOT NULL " +
              "GROUP BY i.imageTag " +
              "ORDER BY COUNT(i.imageTag) DESC ")
    List<ImageTag> findTopTagsByUserId(@Param("userId") Long userId, Pageable pageable);

    //이미지가 보드에 포함되어 있는 지
    boolean existsByImageId(Long imageId);
    //이미지 상세 정보 조회
    @Query("""
        select bi from BoardImage bi
        join fetch bi.image i
        join fetch bi.board b
        join fetch b.user u
        where bi.image.id = :imageId
    """)
    Optional<BoardImage> findByImageId(@Param("imageId") Long imageId);
}
