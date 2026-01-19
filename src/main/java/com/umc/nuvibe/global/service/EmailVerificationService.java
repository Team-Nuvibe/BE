package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.user.entity.EmailVerificationToken;
import com.umc.nuvibe.domain.user.repository.EmailVerificationTokenRepository;
import com.umc.nuvibe.global.apiPayLoad.error.MailErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
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

        // 평문 토큰 생성
        String rawToken = UUID.randomUUID().toString();

        // 토큰을 해시하여 DB에 저장
        EmailVerificationToken verificationToken = new EmailVerificationToken(rawToken, email);
        tokenRepository.save(verificationToken);



        // 해시된 토큰 전송
        String hashedToken = EmailVerificationToken.hashToken(rawToken);
        String link = baseURL + hashedToken;

        try {
            MimeMessage message = createVerificationEmailMessage(email, link);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException(MailErrorCode.EMAIL_SEND_FAILED);
        }
    }

    // HTML 이메일 메시지 생성 (별도 메서드로 분리)
    private MimeMessage createVerificationEmailMessage(String email, String link) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[Nuvibe] 이메일 인증");

        String htmlContent = """
            <html>
            <body>
                <h2>Nuvibe 이메일 인증</h2>
                <p>아래 링크를 클릭해 이메일 인증을 완료해주세요.</p>
                <p>
                    <a href="%s" 
                       style="display:inline-block;padding:10px 20px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:5px;">
                        이메일 인증하기
                    </a>
                </p>
                <p style="color:#999;font-size:11px;">
                    이 링크는 5분간 유효합니다.
                </p>
            </body>
            </html>
            """.formatted(link);

        helper.setText(htmlContent, true);
        return message;
    }

    // 토큰 검증, 성공시 이메일 반환
    @Transactional
    public String verifyToken(String hashedToken) {


        EmailVerificationToken verificationToken = tokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> {

                    return new BusinessException(MailErrorCode.INVALID_TOKEN);
                });

        if (verificationToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new BusinessException(MailErrorCode.TOKEN_EXPIRED);
        }

        // 인증 성공으로 변경
        verificationToken.verified();

        String email = verificationToken.getEmail();

        return email;
    }

    @Transactional(readOnly = true)
    public void checkEmailIsVerified(String email) {
        EmailVerificationToken token = tokenRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MailErrorCode.EMAIL_NOT_VERIFIED));
        if (!token.isVerified()) {
            throw new BusinessException(MailErrorCode.EMAIL_NOT_VERIFIED);
        }
    }
}
