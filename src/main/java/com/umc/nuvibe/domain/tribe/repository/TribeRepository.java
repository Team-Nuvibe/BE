package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.Tribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TribeRepository extends JpaRepository<Tribe, Long> {

    @Query("SELECT t FROM Tribe t WHERE t.tagName = :tagName AND t.counts < 100 ORDER BY t.version ASC")
    List<Tribe> findAvailableRooms(@Param("tagName") String tagName);

    Optional<Tribe> findTopByTagNameOrderByVersionDesc(String tagName);

    @Modifying
    @Query("UPDATE Tribe t SET t.counts = t.counts + 1 WHERE t.id = :id AND t.counts < 100")
    int incrementCounts(@Param("id") Long id);
}
