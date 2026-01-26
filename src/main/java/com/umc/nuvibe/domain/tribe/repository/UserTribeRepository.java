package com.umc.nuvibe.domain.tribe.repository;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeRow;
import com.umc.nuvibe.domain.tribe.dto.response.userTribe.WaitingTribeItemRes;
import com.umc.nuvibe.domain.tribe.entity.UserTribe;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
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
     * 고정, 안 읽음, 채팅시간, ID 순으로 정렬
     */
    @Query("""
    select new com.umc.nuvibe.domain.tribe.dto.internal.ActiveTribeRow(
        t.id, t.imageTag, t.counts, ut.isFavorite, t.lastChatAt, ut.unreadCount)
    from UserTribe ut
    join ut.tribe t
    where ut.user.id = :userId
      and ut.userTribeStatus = :status
      and (
        :hasCursor = false
        or (
               (case when ut.isFavorite = true then 1 else 0 end) < :cFav
            or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
                and (case when ut.unreadCount > 0 then 1 else 0 end) < :cUnread)
            or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
                and (case when ut.unreadCount > 0 then 1 else 0 end) = :cUnread
                and t.lastChatAt < :cLastAt)
            or ((case when ut.isFavorite = true then 1 else 0 end) = :cFav
                and (case when ut.unreadCount > 0 then 1 else 0 end) = :cUnread
                and t.lastChatAt = :cLastAt
                and t.id < :cTribeId)
        )
      )
    order by
      ut.isFavorite desc,
      (case when ut.unreadCount > 0 then 1 else 0 end) desc,
      t.lastChatAt desc,
      t.id desc
    """)
    List<ActiveTribeRow> findActiveTribes(
            @Param("userId") Long userId,
            @Param("status") UserTribeStatus status,
            @Param("hasCursor") boolean hasCursor,
            @Param("cFav") int cFav,
            @Param("cUnread") int cUnread,
            @Param("cLastAt") LocalDateTime cLastAt,
            @Param("cTribeId") Long cTribeId,
            Pageable pageable
    );

    // Waiting 트라이브 챗 목록 조회
    @Query("""
        select new com.umc.nuvibe.domain.tribe.dto.response.userTribe.WaitingTribeItemRes(
            t.id,
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
}
