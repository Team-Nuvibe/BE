package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribe_id")
    private Tribe tribe;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Enumerated(EnumType.STRING)
    private UserTribeStatus userTribeStatus;



    @Builder
    private UserTribe(User user, Tribe tribe, UserTribeStatus userTribeStatus, boolean isFavorite) {
        this.user = user;
        this.tribe = tribe;
        this.userTribeStatus = userTribeStatus;
        this.isFavorite = isFavorite;
    }

    public static UserTribe of(User user, Tribe tribe) {
        return UserTribe.builder()
                .user(user)
                .tribe(tribe)
                .userTribeStatus(UserTribeStatus.WAITING)
                .isFavorite(false)
                .build();
    }

    public void activate() {
        this.userTribeStatus = UserTribeStatus.ACTIVE;
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
    }

}
