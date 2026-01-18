package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long>{

    boolean existsByUser_IdAndTribe_ImageTag(Long userId, ImageTag imageTag);

    boolean existsByUser_IdAndTribe_Id(Long userId, Long tribeId);

    @EntityGraph(attributePaths = {"tribe"})
    List<UserTribe> findAllByUserIdAndTribe_StatusOrderByCreatedAtDesc(Long userId, TribeStatus status);

}
