package com.umc.nuvibe.domain.notification.repository;

import com.umc.nuvibe.domain.notification.entity.Fcm;
import com.umc.nuvibe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmRepository extends JpaRepository<Fcm, Long> {

    List<Fcm> findByUserAndIsActiveTrue(User user);

    boolean existsByUserAndToken(User user, String token);

    List<Fcm> findByToken(String token);

    List<Fcm> findByTokenAndIsActiveTrueAndUserNot(String token, User user);

    Optional<Fcm> findByUserAndToken(User user, String token);
}