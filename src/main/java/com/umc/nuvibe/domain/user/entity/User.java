package com.umc.nuvibe.domain.user.entity;

import com.umc.nuvibe.domain.user.vo.AuthProvider;
import com.umc.nuvibe.domain.user.vo.UserSetting;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private AuthProvider provider;

    private String providerId;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public static User createLocalUser(String name, String nickName, String email,String password) {
        return User.builder()
                .name(name)
                .nickname(nickName)
                .email(email)
                .password(password)
                .provider(AuthProvider.LOCAL)
                .build();
    }

    public static User createSoccialUser(String name,  String email, AuthProvider provider,String providerId) {
        return User.builder()
                .name(name)
                .provider(provider)
                .email(email)
                .providerId(providerId)
                .build();
    }

}
