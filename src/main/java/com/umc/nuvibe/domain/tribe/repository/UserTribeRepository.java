package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long> {

    Boolean existsByUserIdAndTribeId(Long userId, Long tribeId);
}
