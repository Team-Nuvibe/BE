package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.user.entity.EmailVerificationToken;
import com.umc.nuvibe.domain.user.repository.EmailVerificationTokenRepository;
import com.umc.nuvibe.domain.user.vo.VerificationType;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.base-url}")
    private String baseURL;

    // 인증 코드 발송
    @Transactional
    public void sendVerificationCode(String email, VerificationType verificationType) {
        // 같은 이메일과 타입으로 발급받았던 기존 토큰 삭제
        tokenRepository.deleteByEmailAndVerificationType(email, verificationType);

        // 새로운 인증 토큰 생성
        EmailVerificationToken verificationToken = new EmailVerificationToken(email, verificationType);
        tokenRepository.save(verificationToken);

        // 6자리 코드 전송
        String code = verificationToken.getVerificationCode();
        String subject = getEmailSubject(verificationType);
        String purpose = getEmailPurpose(verificationType);

        try {
            MimeMessage message = createVerificationCodeEmailMessage(email, code, subject, purpose);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException(MailErrorCode.EMAIL_SEND_FAILED);
        }
    }

    // 인증 코드 검증
    @Transactional
    public void verifyCode(String email, String code, VerificationType verificationType) {
        EmailVerificationToken verificationToken = tokenRepository.findByEmailAndVerificationType(email, verificationType)
                .orElseThrow(() -> new BusinessException(MailErrorCode.INVALID_VERIFICATION_CODE));

        if (verificationToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new BusinessException(MailErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!verificationToken.getVerificationCode().equals(code)) {
            throw new BusinessException(MailErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 성공으로 변경
        verificationToken.verified();
    }

    // 인증 완료 여부 확인
    @Transactional(readOnly = true)
    public void checkCodeIsVerified(String email, VerificationType verificationType) {
        EmailVerificationToken token = tokenRepository
                .findByEmailAndVerificationType(email, verificationType)
                .orElseThrow(() -> new BusinessException(MailErrorCode.CODE_NOT_VERIFIED));

        if (!token.isVerified()) {
            throw new BusinessException(MailErrorCode.CODE_NOT_VERIFIED);
        }
    }

    // 인증 타입에 따른 이메일 제목
    private String getEmailSubject(VerificationType type) {
        return switch (type) {
            case JOIN -> "[Nuvibe] 회원가입 이메일 인증";
            case EMAIL_CHANGE -> "[Nuvibe] 이메일 변경 인증";
            case PASSWORD_RESET -> "[Nuvibe] 비밀번호 재설정 인증";
        };
    }

    // 인증 타입에 따른 이메일 용도
    private String getEmailPurpose(VerificationType type) {
        return switch (type) {
            case JOIN -> "회원가입";
            case EMAIL_CHANGE -> "이메일 변경";
            case PASSWORD_RESET -> "비밀번호 재설정";
        };
    }

    // HTML 이메일 메시지 생성 (6자리 코드)
    private MimeMessage createVerificationCodeEmailMessage(String email, String code, String subject, String purpose) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(subject);

        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                    <h2 style="color: #4CAF50;">Nuvibe %s 인증</h2>
                    <p>안녕하세요,</p>
                    <p>아래 6자리 인증 코드를 입력하여 인증을 완료해주세요.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <div style="display: inline-block; padding: 20px 40px; background-color: #f5f5f5; border: 2px dashed #4CAF50; border-radius: 8px;">
                            <span style="font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #4CAF50;">%s</span>
                        </div>
                    </div>
                    <p style="color: #999; font-size: 14px; text-align: center;">
                        이 인증 코드는 <strong>5분간</strong> 유효합니다.
                    </p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="color: #999; font-size: 12px;">
                        본인이 요청하지 않은 경우, 이 이메일을 무시하셔도 됩니다.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(purpose, code);

        helper.setText(htmlContent, true);
        return message;
    }
}
