package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.Tribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TribeRepository extends JpaRepository<Tribe, Long> {

    @Query(value = "SELECT * FROM tribes t WHERE t.tag_name = :imageTag AND t.counts < 100 ORDER BY t.version ASC LIMIT 1", nativeQuery = true)
    Optional<Tribe> findAvailableRoom(@Param("imageTag") ImageTag imageTag);

    Optional<Tribe> findTopByImageTagOrderByVersionDesc(ImageTag imageTag);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts + 1 WHERE t.id = :id AND t.counts < 100")
    int incrementCounts(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Tribe t SET t.counts = t.counts - 1 WHERE t.id = :tribeId AND t.counts > 0")
    int decrementCounts(@Param("tribeId") Long tribeId);

}
