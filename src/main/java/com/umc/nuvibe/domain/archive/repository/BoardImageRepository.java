package com.umc.nuvibe.domain.archive.repository;

import com.umc.nuvibe.domain.archive.dto.BoardOldestImageDto;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                     "ORDER BY bi.createdAt DESC "
                     )
       List<BoardImage> findTopByBoardIdOrderByCreatedAtDesc(
               @Param("boardId") Long boardId,
               Pageable pageable
       );

       // 여러 보드의 최신 썸네일 한 번에 조회 (N+1 방지)
       @Query("SELECT bi FROM BoardImage bi " +
                     "JOIN FETCH bi.image " +
                     "WHERE bi.board.id IN :boardIds " +
                     "AND bi.createdAt = (SELECT MAX(bi2.createdAt) FROM BoardImage bi2 WHERE bi2.board.id = bi.board.id)")
       List<BoardImage> findLatestByBoardIds(@Param("boardIds") List<Long> boardIds);

       // 보드 삭제 시 연결된 이미지 전체 삭제 (벌크 연산 명시)
       @Modifying(clearAutomatically = true)
       @Query("DELETE FROM BoardImage bi WHERE bi.board.id = :boardId")
       void deleteByBoardId(@Param("boardId") Long boardId);

       // 여러 보드의 이미지 삭제 (다중 보드 삭제용)
       @Modifying(clearAutomatically = true)
       @Query("DELETE FROM BoardImage bi WHERE bi.board.id IN :boardIds AND bi.board.user.id = :userId")
       void deleteByBoardIdInAndUserId(@Param("boardIds") List<Long> boardIds, @Param("userId") Long userId);

       // 보드 내 이미지 삭제 (반환: 삭제 건수)
       @Modifying(clearAutomatically = true)
       @Query("DELETE FROM BoardImage bi WHERE bi.id IN :imageIds AND bi.board.id = :boardId")
       int deleteByIdInAndBoardId(@Param("imageIds") List<Long> imageIds, @Param("boardId") Long boardId);

       // 여러 보드의 가장 오래된 이미지 업로드 시간 조회 (타입 안전한 DTO 사용)
       @Query("SELECT new com.umc.nuvibe.domain.archive.dto.BoardOldestImageDto(bi.board.id, MIN(bi.createdAt)) " +
                     "FROM BoardImage bi " +
                     "WHERE bi.board.id IN :boardIds " +
                     "GROUP BY bi.board.id")
       List<BoardOldestImageDto> findOldestCreatedAtByBoardIds(@Param("boardIds") List<Long> boardIds);

       // 사용자가 올린 모든 이미지 조회 (페이징, 최신순)
       @EntityGraph(attributePaths = { "image", "board" })
       @Query("SELECT bi FROM BoardImage bi " +
                     "WHERE bi.board.user.id = :userId " +
                     "ORDER BY bi.createdAt DESC")
       Page<BoardImage> findAllByUserIdOrderByCreatedAtDesc(
                     @Param("userId") Long userId,
                     Pageable pageable);

       // 사용자가 가장 많이 사용한 태그 Top 4 조회
       @Query("SELECT i.imageTag " +
                     "FROM BoardImage bi " +
                     "JOIN bi.image i " +
                     "JOIN bi.board b " +
                     "WHERE b.user.id = :userId AND i.imageTag IS NOT NULL " +
                     "GROUP BY i.imageTag " +
                     "ORDER BY COUNT(i.imageTag) DESC")
       List<ImageTag> findTopTagsByUserId(@Param("userId") Long userId, Pageable pageable);

       // 이미지가 보드에 포함되어 있는 지
       boolean existsByImageId(Long imageId);

       // 이미지 상세 정보 조회
       @Query("""
                         select bi from BoardImage bi
                         join fetch bi.image i
                         join fetch bi.board b
                         join fetch b.user u
                         where bi.image.id = :imageId
                     """)
       Optional<BoardImage> findByImageId(@Param("imageId") Long imageId);


       //기간 내 총 이미지 드랍 수
        @Query("select count(bi) from BoardImage bi " +
                "where bi.board.user.id = :userId " +
                "and bi.image.createdAt >= :start " +
                "and bi.image.createdAt <= :end "
        )
        Long countTotalImageByPeriod(
                @Param("userId") Long userId,
                @Param("start")LocalDateTime start,
                @Param("end") LocalDateTime end
        );


       //기간 내 많이 사용한 태그 조회
        @Query("select bi.image.imageTag from BoardImage bi " +
            "where bi.board.user.id = :userId " +
            "and bi.image.createdAt >= :start " +
            "and bi.image.createdAt < :end " +
            "group by bi.image.imageTag " +
            "order by count(bi) desc")
        List<ImageTag> findTopTagsByPeriod(
            @Param("userId") Long userId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
        );

    interface TopBoardProjection {
        Long getBoardId();
        Long getCount();
    }

        //기간 내 가장 많이 업로드한 보드
        @Query("select bi.board.id as boardId, count(bi) as count from BoardImage bi " +
                "where bi.board.user.id = :userId " +
                "and bi.image.createdAt >= :start " +
                "and bi.image.createdAt < :end " +
                "group by bi.board.id " +
                "order by count desc " +
                "limit 1"
        )
       TopBoardProjection findTopBoardByPeriod (
                @Param("userId") Long userId,
                @Param("start")LocalDateTime start,
                @Param("end") LocalDateTime end
        );


        //기간 내 활동 보드 수(새로운 보드 생성 또는 이미지 추가)
    @Query("select count(distinct board.id) from ArchiveBoard board " +
            "left join BoardImage bi on bi.board = board " +
            "where board.user.id = :userId " +
            "and (board.createdAt between :start and :end " + //새로운 보드가 생성
            "or bi.createdAt between :start and :end)") //보드에 이미지가 추가
        long countActiveBoardsByPeriod (
                @Param("userId") Long userId,
                @Param("start")LocalDateTime start,
                @Param("end") LocalDateTime end
        );

    //총 태그 사용 횟수 조회
    @Query ("select count(bi.image.imageTag) from BoardImage bi " +
            "where bi.board.user.id = :userId " +
            "and bi.image.createdAt >= :start " +
            "and bi.image.createdAt <= :end")
    long countTotalTagByPeriod(
            @Param("userId") Long userId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //하루 최대 업로드 수 조회
    @Query("select count(bi) as count from BoardImage bi " +
            "where bi.board.user.id = :userId " +
            "and bi.createdAt >= :start " +
            "and bi.createdAt <= :end " +
            "group by DATE(bi.createdAt) " +
            "order by count desc " +
            "limit 1 " )
    Long findMaxDailyDropCount(
            @Param("userId") Long userId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //가장 많이 업로드한 요일 조회
    @Query(value =
            "select DAYNAME(bi.created_at) " +
                    "from board_images bi " +
                    "join archive_board ab on bi.board_id = ab.board_id " +
                    "join images i on bi.image_id = i.image_id " +
                    "where ab.user_id = :userId " +
                    "and bi.created_at >= :start and bi.created_at <= :end " +
                    "group by DAYNAME(bi.created_at) " +
                    "order by count(*) desc, " + //가장 많이 업로드 순 조회
                    "max(bi.created_at) desc " + //드랍 수 동일 시 가장 최근 업로드 순
                    "limit 1",
            nativeQuery = true)
    String findTopDayOfWeekByPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //가장 많이 올린 시간대 조회
    @Query(value =
            "select hour(bi.created_at) " +
                    "from board_images bi " +
                    "join archive_board ab on bi.board_id = ab.board_id " +
                    "where ab.user_id = :userId " +
                    "and bi.created_at >= :start and bi.created_at < :end " +
                    "group by hour(bi.created_at) " +
                    "order by count(*) desc, " + //가장 많이 업로드 순 조회
                    "max(bi.created_at) desc " + //드랍 수 동일 시 가장 최근 업로드 순
                    "limit 1",
            nativeQuery = true)
    Integer findTopHourByPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //해당 달에 업로드한 날자
    @Query(value =
            "select distinct date(i.created_at) from board_images bi " +
                    "join archive_board ab on ab.board_id = bi.board_id " +
                    "join images i on i.image_id = bi.image_id " +
                    "where ab.user_id = :userId " +
                    "and i.created_at >= :start " +
                    "and i.created_at < :end " +
                    "order by date(i.created_at)",
            nativeQuery = true)
    List<java.sql.Date> findImageDropDatesByMonth(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    //해당 날에 업로드한 이미지 조회
    @Query("select i from BoardImage bi " +
            "join bi.image i " +
            "join bi.board b " +
            "where b.user.id = :userId " +
            "and function('DATE', i.createdAt) = :date "
    )
    List<Image> findImagesByDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    // 각 보드별 고유 태그 개수 조회
    @Query("SELECT bi.board.id, COUNT(DISTINCT i.imageTag) " +
            "FROM BoardImage bi " +
            "JOIN bi.image i " +
            "WHERE bi.board.id IN :boardIds " +
            "GROUP BY bi.board.id")
    List<Object[]> countDistinctTagsByBoardIds(@Param("boardIds") List<Long> boardIds);

    // 보드 내 이미지 ID 목록으로 보드 이미지 조회
    List<BoardImage> findAllByIdInAndBoardId(List<Long> ids, Long boardId);

    @Modifying
    @Query("UPDATE BoardImage bi SET bi.board.id = :targetBoardId WHERE bi.id IN :ids AND bi.board.id = :sourceBoardId")
    int bulkMoveToBoard(@Param("ids") List<Long> ids,
                        @Param("sourceBoardId") Long sourceBoardId,
                        @Param("targetBoardId") Long targetBoardId);
}
