package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.dto.internal.CloseTargetView;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TribeRepository extends JpaRepository<Tribe, Long> {

    Optional<Tribe> findFirstByImageTagAndCountsLessThanOrderByVersionAsc(ImageTag imageTag, int counts);

    default Optional<Tribe> findAvailableRoom(ImageTag imageTag) {
        return findFirstByImageTagAndCountsLessThanOrderByVersionAsc(imageTag, 100);
    }

    Optional<Tribe> findTopByImageTagOrderByVersionDesc(ImageTag imageTag);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts + 1 WHERE t.id = :id AND t.counts < 100")
    int incrementCounts(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts - 1 WHERE t.id = :tribeId AND t.counts > 0")
    int decrementCounts(@Param("tribeId") Long tribeId);

    // 특정 태그의 최신 트라이브 ID 조회
    @Query("SELECT t.id FROM Tribe t WHERE t.imageTag = :imageTag ORDER BY t.version DESC LIMIT 1")
    Long findLatestTribeIdByImageTag(@Param("imageTag") ImageTag imageTag);

    /**
     * 트라이브 챗 자동 삭제 대상 정보 조회
     * 활성화 인원이 5명 이하인 것만 조회
     */
    @Query("""
        SELECT
            t.id        AS tribeId,
            t.createdAt AS createdAt,
            COUNT(ut.id) AS activeCount
        FROM Tribe t
        LEFT JOIN UserTribe ut
               ON ut.tribe = t
              AND ut.userTribeStatus = :activeStatus
        WHERE t.createdAt <= :cutoff
        GROUP BY t.id, t.createdAt
        HAVING COUNT(ut.id) < :minActiveCount
        ORDER BY t.createdAt ASC, t.id ASC
    """)
    Slice<CloseTargetView> findCloseTargets(
            @Param("cutoff") LocalDateTime cutoff,
            @Param("activeStatus") UserTribeStatus activeStatus,
            @Param("minActiveCount") long minActiveCount,
            Pageable pageable
    );

    //트라이브 하드 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Tribe t WHERE t.id = :tribeId")
    void deleteById(@Param("tribeId") Long tribeId);
}
