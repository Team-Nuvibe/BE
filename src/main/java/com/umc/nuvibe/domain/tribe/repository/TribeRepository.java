package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.Tribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TribeRepository extends JpaRepository<Tribe, Long> {

    @Query(value = "SELECT * FROM tribes t WHERE t.tag_Name = :tagName AND t.counts < 100 ORDER BY t.version ASC LIMIT 1", nativeQuery = true)
    Optional<Tribe> findAvailableRoom(@Param("tagName") String tagName);

    Optional<Tribe> findTopByTagNameOrderByVersionDesc(String tagName);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts + 1 WHERE t.id = :id AND t.counts < 100")
    int incrementCounts(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts - 1 WHERE t.id = :tribeId AND t.counts > 0")
    int decrementCounts(@Param("tribeId") Long tribeId);

    // 특정 태그의 최신 트라이브 ID 조회
    @Query("SELECT t.id FROM Tribe t WHERE t.tagName = :tagName ORDER BY t.version DESC LIMIT 1")
    Long findLatestTribeIdByTagName(@Param("tagName") String tagName);
}
