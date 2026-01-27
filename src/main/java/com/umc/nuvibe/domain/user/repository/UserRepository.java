package com.umc.nuvibe.domain.user.repository;

import com.umc.nuvibe.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    // 오늘 드랍(이미지 업로드) 안 한 사용자 조회
    @Query("""
    SELECT u FROM User u
    WHERE u.id NOT IN (
        SELECT DISTINCT bi.board.user.id
        FROM BoardImage bi
        WHERE bi.createdAt >= :todayStart
    )
    """)
    Slice<User> findUsersWithoutDropToday(LocalDateTime todayStart, Pageable pageable);

    // 특정 날짜 이후 드랍 없는 사용자 조회
    @Query("""
    SELECT u FROM User u
    WHERE u.id NOT IN (
        SELECT DISTINCT bi.board.user.id
        FROM BoardImage bi
        WHERE bi.createdAt >= :since
    )
    """)
    Slice<User> findUsersWithoutDropSince(@Param("since") LocalDateTime since, Pageable pageable);


    // 특정 기간 내 드랍한 사용자 조회 (NOTI-09, 10 공통)
    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN ArchiveBoard ab ON ab.user = u
    JOIN BoardImage bi ON bi.board = ab
    WHERE bi.createdAt >= :start AND bi.createdAt < :end
    """)
    Slice<User> findUsersWithDropBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
    
    boolean existsByEmail(String email);
}