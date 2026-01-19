package com.umc.nuvibe.domain.user.entity;

import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import com.umc.nuvibe.global.apiPayLoad.error.MailErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String token; // 해시된 토큰 저장

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    private boolean isVerified=false;

    public void verified(){
        this.isVerified=true;
    }

    public EmailVerificationToken(String rawToken, String email) {
        this.token = hashToken(rawToken); // 토큰을 해시하여 저장
        this.email = email;
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
        this.isVerified=false;
    }

    // SHA-256으로 토큰 해싱
    public static String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(MailErrorCode.NO_SHA_256);
        }
    }
}
