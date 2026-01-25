package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long>{

    boolean existsByUser_IdAndTribe_ImageTag(Long userId, ImageTag imageTag);

    boolean existsByUser_IdAndTribe_Id(Long userId, Long tribeId);

    boolean existsByUser_IdAndTribe_IdAndUserTribeStatus(Long userId, Long tribeId, UserTribeStatus userTribeStatus);

    @EntityGraph(attributePaths = {"tribe"})
    List<UserTribe> findAllByUserIdAndTribe_StatusOrderByCreatedAtDesc(Long userId, TribeStatus status);

    // 유저트라이브 하드 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserTribe ut WHERE ut.tribe.id = :tribeId")
    int deleteByTribeId(@Param("tribeId") Long tribeId);
}
