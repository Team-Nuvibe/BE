package com.umc.nuvibe.domain.tribe.repository.userTribeRepository;

import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long>{

    Boolean existsByUserIdAndTribe_TagName(Long userId, String tribeTagName);

    @EntityGraph(attributePaths = {"tribe"})
    List<UserTribe> findAllByUserIdAndTribe_StatusOrderByCreatedAtDesc(Long userId, TribeStatus status);

}
