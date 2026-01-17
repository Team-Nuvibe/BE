package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.ScrapedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScrapedImageRepository extends JpaRepository<ScrapedImage, Long> {

    //스크랩 이미지 단건 조회
    Optional<ScrapedImage> findByUser_IdAndTribe_IdAndImage_Id(Long userId, Long tribeId, Long imageId);


    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ScrapedImage si WHERE si.user.id = :userId AND si.tribe.id = :tribeId")
    void deleteAllByUserIdAndTribeId(@Param("userId") Long userId, @Param("tribeId") Long tribeId);
}
