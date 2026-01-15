package com.umc.nuvibe.domain.archive.repository;

import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

       // 보드의 가장 최근 이미지 조회 (썸네일용)
       @Query("SELECT bi FROM BoardImage bi " +
                     "JOIN FETCH bi.image " +
                     "WHERE bi.board.id = :boardId " +
                     "ORDER BY bi.createdAt DESC " +
                     "LIMIT 1")
       Optional<BoardImage> findTopByBoardIdOrderByCreatedAtDesc(@Param("boardId") Long boardId);

       // 여러 보드의 최신 썸네일 한 번에 조회 (N+1 방지)
       @Query("SELECT bi FROM BoardImage bi " +
                     "JOIN FETCH bi.image " +
                     "WHERE bi.board.id IN :boardIds " +
                     "AND bi.createdAt = (SELECT MAX(bi2.createdAt) FROM BoardImage bi2 WHERE bi2.board.id = bi.board.id)")
       List<BoardImage> findLatestByBoardIds(@Param("boardIds") List<Long> boardIds);

       // 보드 삭제 시 연결된 이미지 전체 삭제
       @Modifying
       void deleteByBoardId(Long boardId);

       // 여러 보드의 이미지 삭제 (다중 보드 삭제용)
       @Modifying(clearAutomatically = true) // 캐시 삭제
       @Query("DELETE FROM BoardImage bi WHERE bi.board.id IN :boardIds AND bi.board.user.id = :userId")
       void deleteByBoardIdInAndUserId(@Param("boardIds") List<Long> boardIds, @Param("userId") Long userId);

       // 보드 내 이미지 삭제 (반환: 삭제 건수)
       @Modifying(clearAutomatically = true)
       @Query("DELETE FROM BoardImage bi WHERE bi.id IN :imageIds AND bi.board.id = :boardId")
       int deleteByIdInAndBoardId(@Param("imageIds") List<Long> imageIds, @Param("boardId") Long boardId);

       // 여러 보드의 가장 오래된 이미지 업로드 시간 조회 (홈 화면 정렬용)
       @Query("SELECT bi.board.id, MIN(bi.createdAt) FROM BoardImage bi " +
                     "WHERE bi.board.id IN :boardIds " +
                     "GROUP BY bi.board.id")
       List<Object[]> findOldestCreatedAtByBoardIds(@Param("boardIds") List<Long> boardIds);
}
