package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeRow;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.WaitingTribeItemRes;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserTribeRepository extends JpaRepository<UserTribe, Long>{

    boolean existsByUser_IdAndTribe_ImageTag(Long userId, ImageTag imageTag);

    boolean existsByUser_IdAndTribe_Id(Long userId, Long tribeId);

    boolean existsByUser_IdAndTribe_IdAndUserTribeStatus(Long userId, Long tribeId, UserTribeStatus userTribeStatus);

    /**
    * 채팅 발신 시, 동일 트라이브의 ACTIVE 사용자 중
    * 발신자를 제외한 모든 사용자에 대해 unreadCount를 1 증가
    */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update UserTribe ut
           set ut.unreadCount = ut.unreadCount + 1
         where ut.tribe.id = :tribeId
           and ut.userTribeStatus = :activeStatus
           and ut.user.id <> :senderUserId
    """)
    int incrementUnreadCountForActiveMembers(
            @Param("tribeId") Long tribeId,
            @Param("activeStatus") UserTribeStatus activeStatus,
            @Param("senderUserId") Long senderUserId
    );

    /**
     * Active 트라이브 챗 목록 조회
     * 고정, 최신 활동 시간(동일할 시 메시지 읽음 여부 순) 순으로 정렬
     */
    @Query("""
select new com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeRow(
    t.id,
    ut.id,
    t.imageTag,
    t.counts,
    ut.isFavorite,
    ut.lastActivityAt,
    ut.unreadCount,
    t.lastChatId
)
from UserTribe ut
join ut.tribe t
where ut.user.id = :userId
  and ut.userTribeStatus = :status
  and (
    :hasCursor = false
    or (
         (case when ut.isFavorite = true then 1 else 0 end) < :cFav
      or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
          and ut.lastActivityAt < :cActivityAt)
      or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
          and ut.lastActivityAt = :cActivityAt
          and (case when ut.unreadCount > 0 then 1 else 0 end) < :cUnread)
      or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
          and ut.lastActivityAt = :cActivityAt
          and (case when ut.unreadCount > 0 then 1 else 0 end) = :cUnread
          and (case when t.lastChatId is null then 0 else t.lastChatId end) < :cLastChatId)
    )
  )
order by
  ut.isFavorite desc,
  ut.lastActivityAt desc,
  (case when ut.unreadCount > 0 then 1 else 0 end) desc,
  (case when t.lastChatId is null then 0 else t.lastChatId end) desc
""")
    List<ActiveTribeRow> findActiveTribes(
            @Param("userId") Long userId,
            @Param("status") UserTribeStatus status,
            @Param("hasCursor") boolean hasCursor,
            @Param("cFav") int cFav,
            @Param("cActivityAt") LocalDateTime cActivityAt,
            @Param("cUnread") int cUnread,
            @Param("cLastChatId") long cLastChatId,
            Pageable pageable
    );

    // Waiting 트라이브 챗 목록 조회
    @Query("""
        select new com.umc.nuvibe.domain.tribe.dto.response.userTribe.WaitingTribeItemRes(
            t.id,
            ut.id,
            t.imageTag,
            t.counts
        )
        from UserTribe ut
        join ut.tribe t
        where ut.user.id = :userId
          and ut.userTribeStatus = :status
          and (:hasCursor = false or t.id < :cTribeId)
        order by t.id desc
    """)
    List<WaitingTribeItemRes> findWaitingTribes(
            @Param("userId") Long userId,
            @Param("status") UserTribeStatus status,
            @Param("hasCursor") boolean hasCursor,
            @Param("cTribeId") Long cTribeId,
            Pageable pageable
    );

    // 트라이브 챗 읽음 처리
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update UserTribe ut
           set ut.lastReadChatId = :lastChatId,
               ut.unreadCount = 0
         where ut.user.id = :userId
           and ut.tribe.id = :tribeId
           and ut.userTribeStatus = :status
           and (
                ut.lastReadChatId is null
                or ut.lastReadChatId < :lastChatId
           )
    """)
    int readChat(
            @Param("userId") Long userId,
            @Param("tribeId") Long tribeId,
            @Param("lastChatId") Long lastChatId,
            @Param("status") UserTribeStatus status
    );

    // 유저트라이브 하드 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserTribe ut WHERE ut.tribe.id = :tribeId")
    int deleteByTribeId(@Param("tribeId") Long tribeId);

    // 마지막 활동 시간 갱신
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update UserTribe ut
       set ut.lastActivityAt = :activityAt
     where ut.tribe.id = :tribeId
       and ut.userTribeStatus = :status
""")
    int updateLastActivityAt(
            Long tribeId,
            UserTribeStatus status,
            LocalDateTime activityAt
    );


    // 기존: ACTIVE 유저만 (NOTI-03 채팅 알림 등에서 사용)
    @Query("SELECT ut.user FROM UserTribe ut WHERE ut.tribe.id = :tribeId AND ut.userTribeStatus = 'ACTIVE'")
    List<User> findActiveUsersByTribeId(@Param("tribeId") Long tribeId);

    @Query("SELECT ut.user FROM UserTribe ut WHERE ut.tribe.id = :tribeId AND ut.user.id != :excludeUserId AND ut.userTribeStatus = 'ACTIVE'")
    List<User> findActiveUsersByTribeIdExcept(@Param("tribeId") Long tribeId, @Param("excludeUserId") Long excludeUserId);

    // 추가: 전체 참여 유저 (ACTIVE + WAITING) — NOTI-05, NOTI-06용
    @Query("SELECT ut.user FROM UserTribe ut WHERE ut.tribe.id = :tribeId")
    List<User> findAllUsersByTribeId(@Param("tribeId") Long tribeId);

    // 추가: WAITING 유저만 — NOTI-01용 (5명 도달 시 전원)
    @Query("SELECT ut.user FROM UserTribe ut WHERE ut.tribe.id = :tribeId AND ut.userTribeStatus = 'WAITING'")
    List<User> findWaitingUsersByTribeId(@Param("tribeId") Long tribeId);

    // 추가: WAITING 유저 중 특정 유저 제외 — NOTI-02용 (기존 대기자만)
    @Query("SELECT ut.user FROM UserTribe ut WHERE ut.tribe.id = :tribeId AND ut.user.id != :excludeUserId AND ut.userTribeStatus = 'WAITING'")
    List<User> findWaitingUsersByTribeIdExcept(@Param("tribeId") Long tribeId, @Param("excludeUserId") Long excludeUserId);
}
