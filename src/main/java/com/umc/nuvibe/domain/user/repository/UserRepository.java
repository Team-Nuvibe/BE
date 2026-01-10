package com.umc.nuvibe.domain.user.repository;


import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.service.AuthService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    boolean existsByEmail(String email);

}
