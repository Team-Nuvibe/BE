package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.user.entity.EmailVerificationToken;
import com.umc.nuvibe.domain.user.repository.EmailVerificationTokenRepository;
import com.umc.nuvibe.global.apiPayLoad.error.MailErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.base-url}")
    private String baseURL;

    // 인증 메일 발송
    @Transactional
    public void sendVerificationEmail(String email) {

        // 같은 이메일로 발급받았던 기존 토큰 삭제
        tokenRepository.deleteByEmail(email);

        // 토큰 생성 후 db에 저장
        String token= UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, email);
        tokenRepository.save(verificationToken);

        // 메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Nuvibe] 이메일 인증");

        // 이거 푸쉬하기 전에 환경변수로 고쳐놓기
        String link = baseURL+ token;
        message.setText("아래 링크를 클릭해 이메일 변경을 완료해주세요.\n"+link);
        javaMailSender.send(message);

    }

    // 토큰 검증, 성공시 이메일 반환
    @Transactional
    public String verifyToken(String token) {
        EmailVerificationToken verificationToken=tokenRepository.findByToken(token)
                .orElseThrow(()->new BusinessException(MailErrorCode.INVALID_TOKEN));

        if (verificationToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new BusinessException(MailErrorCode.TOKEN_EXPIRED);
        }

        // 인증 성공으로 변경
        verificationToken.verified();

        return verificationToken.getEmail();
    }

    @Transactional(readOnly = true)
    public void checkEmailIsVerified(String email) {
        EmailVerificationToken token=tokenRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(MailErrorCode.EMAIL_NOT_VERIFIED));
        if (!token.isVerified()) {
            throw new BusinessException(MailErrorCode.EMAIL_NOT_VERIFIED);
        }
    }
}
