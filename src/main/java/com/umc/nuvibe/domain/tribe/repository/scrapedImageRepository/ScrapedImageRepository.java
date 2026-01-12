package com.umc.nuvibe.domain.tribe.repository.scrapedImageRepository;

import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapedImageRepository extends JpaRepository<ScrapedImage, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ScrapedImage si WHERE si.user.id = :userId AND si.tribe.id = :tribeId")
    void deleteAllByUserIdAndTribeId(@Param("userId") Long userId, @Param("tribeId") Long tribeId);
}
