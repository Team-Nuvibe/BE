package com.umc.nuvibe.domain.user.entity;

import com.umc.nuvibe.domain.user.vo.VerificationType;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String verificationCode; // 6자리 인증 코드

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType verificationType;

    private boolean isVerified=false;

    public void verified(){
        this.isVerified = true;
    }

    public EmailVerificationToken(String email, VerificationType verificationType) {
        this.verificationCode = generateVerificationCode();
        this.email = email;
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
        this.verificationType = verificationType;
        this.isVerified = false;
    }

    // 6자리 랜덤 숫자 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000~999999
        return String.valueOf(code);
    }
}
