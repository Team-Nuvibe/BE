package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.tribe.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN FETCH c.tribe JOIN FETCH c.image WHERE c.id = :chatId")
    Optional<Chat> findByIdWithImageAndTribe(@Param("chatId") Long chatId);
}
