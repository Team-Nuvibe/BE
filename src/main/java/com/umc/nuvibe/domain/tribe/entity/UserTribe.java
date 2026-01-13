package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
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

    private UserTribe(User user, Tribe tribe) {
        this.user = user;
        this.tribe = tribe;
    }

    public static UserTribe of(User user, Tribe tribe){
        return new UserTribe(user, tribe);
    }

}
