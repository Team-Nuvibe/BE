package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "user_tribe",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_tribe_user_tribe", columnNames = {"user_id", "tribe_id"})
)
@NoArgsConstructor
public class UserTribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tribe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_user_tribe_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribe_id")
    private Tribe tribe;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Column(name = "is_muted")
    private boolean isMuted = false;

    public void toggleMute() {
        this.isMuted = !this.isMuted;
    }

    @Enumerated(EnumType.STRING)
    private UserTribeStatus userTribeStatus;

    // 마지막으로 읽은 chatId
    private Long lastReadChatId;

    // 안 읽은 메시지 수
    private int unreadCount;

    // 마지막 활동 시각
    private LocalDateTime lastActivityAt;

    @Builder
    private UserTribe(User user, Tribe tribe, UserTribeStatus userTribeStatus, boolean isFavorite, Long lastReadChatId, int unreadCount, LocalDateTime lastActivityAt) {
        this.user = user;
        this.tribe = tribe;
        this.userTribeStatus = userTribeStatus;
        this.isFavorite = isFavorite;
        this.lastReadChatId = lastReadChatId;
        this.unreadCount = unreadCount;
        this.lastActivityAt = lastActivityAt;
    }

    public static UserTribe of(User user, Tribe tribe) {
        return UserTribe.builder()
                .user(user)
                .tribe(tribe)
                .userTribeStatus(UserTribeStatus.WAITING)
                .isFavorite(false)
                .lastReadChatId(null)
                .unreadCount(0)
                .lastActivityAt(LocalDateTime.now())
                .build();
    }

    public void activate() {
        this.userTribeStatus = UserTribeStatus.ACTIVE;
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }

    public void updateLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

}
