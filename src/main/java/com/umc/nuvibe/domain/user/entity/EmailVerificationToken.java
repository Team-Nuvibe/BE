package com.umc.nuvibe.domain.user.entity;

import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    private boolean isVerified=false;

    public void verified(){
        this.isVerified=true;
    }

    public EmailVerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
        this.isVerified=false;
    }
}
