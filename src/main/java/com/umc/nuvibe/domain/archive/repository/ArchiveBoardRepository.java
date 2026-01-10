package com.umc.nuvibe.domain.archive.repository;

import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArchiveBoardRepository extends JpaRepository<ArchiveBoard, Long> {
    
    // 사용자의 모든 보드 조회
    List<ArchiveBoard> findByUserId(Long userId);
    
    // 사용자의 특정 보드 조회 (권한 체크용)
    Optional<ArchiveBoard> findByIdAndUserId(Long boardId, Long userId);
    
    // 보드명 중복 체크
    boolean existsByUserIdAndName(Long userId, String name);
    
    // 여러 보드 삭제 (다중 선택 삭제용)
    @Modifying
    @Query("DELETE FROM ArchiveBoard ab WHERE ab.id IN :boardIds AND ab.user.id = :userId")
    void deleteByIdInAndUserId(@Param("boardIds") List<Long> boardIds, @Param("userId") Long userId);
}
