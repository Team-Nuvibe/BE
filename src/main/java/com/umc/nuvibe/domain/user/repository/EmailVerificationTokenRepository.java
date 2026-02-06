package com.umc.nuvibe.domain.user.repository;

import com.umc.nuvibe.domain.user.entity.EmailVerificationToken;
import com.umc.nuvibe.domain.user.vo.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
    Optional<EmailVerificationToken> findByEmailAndVerificationType(String email, VerificationType verificationType);
    void deleteByEmailAndVerificationType(String email, VerificationType verificationType);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.createdAt < :cutoff")
    int deleteAllCreatedBefore(@Param("cutoff") LocalDateTime cutoff);
}
