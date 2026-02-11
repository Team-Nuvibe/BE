package com.umc.nuvibe.domain.notification.entity;

import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;  // [추가] AccessLevel import
import lombok.Builder;      // [추가] Builder import
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "fcm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // [수정] access = AccessLevel.PROTECTED 추가
public class Fcm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_fcm_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL"))
    private User user;

    @Column(nullable = false)  // [수정] nullable = false 추가
    private String token;

    // [추가] 토큰 활성화 상태
    @Column(name = "is_active")
    private boolean isActive = true;

    @Builder
    private Fcm(User user, String token) {
        this.user = user;
        this.token = token;
    }

    // [추가] 토큰 갱신 메서드
    public void updateToken(String token) {
        this.token = token;
        this.isActive = true;
    }

    // [추가] 토큰 비활성화 메서드
    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
