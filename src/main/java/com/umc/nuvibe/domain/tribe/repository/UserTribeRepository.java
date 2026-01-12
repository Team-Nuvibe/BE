package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long>, UserTribeRepositoryCustom {

    Boolean existsByUserIdAndTribeId(Long userId, Long tribeId);

    @EntityGraph(attributePaths = {"tribe"})
    List<UserTribe> findAllByUserIdAndTribeStatusOrderByCreatedAtDesc(Long userId, TribeStatus status);
}
