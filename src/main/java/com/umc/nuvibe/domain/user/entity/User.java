package com.umc.nuvibe.domain.user.entity;

import com.umc.nuvibe.domain.user.vo.ProviderType;
import com.umc.nuvibe.domain.user.vo.UserSetting;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String nickname;

    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String password;

    @Column(name="profile_image")
    private String profileImage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private UserSetting setting;

    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    private String providerId;

    @Builder
    public User (String name, String nickname, String email, ProviderType provider) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
    }

}
