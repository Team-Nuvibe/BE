package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.user.dto.request.ReissuePasswordReq;
import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final EmailVerificationService verificationService;

    @Value("${frontend.redirect.email-verify-success}")
    private String emailVerifySuccessUrl;

    @Value("${frontend.redirect.email-verify-failed}")
    private String emailVerifyFailedUrl;

    @Override
    @Transactional
    public void updateProfileImage(Long userId, MultipartFile file) {
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));
        String profileURL=imageService.uploadImage(file);
        user.updateProfileImage(profileURL);
    }

    @Override
    @Transactional
    public void updateUserNickname(Long userId, String nickname) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        LocalDateTime lastUpdated=user.getLastNicknameUpdatedDate();

        if (!(lastUpdated==null || lastUpdated.plusDays(14).isBefore(LocalDateTime.now()))) {
            throw new BusinessException(UserErrorCode.NICKNAME_UPDATE_RESTRICTED);
        }

        user.updateNickname(nickname);

    }

    @Override
    @Transactional
    public void requestEmailUpdate(Long userId, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }
        log.info("이메일 변경 요청 - 사용자ID: {}, 새 이메일: {}", userId, email);
        verificationService.sendVerificationEmail(email, false);
    }

    @Override
    @Transactional
    public void verifyAndUpdateEmailWithRedirect(Long userId, String token, HttpServletResponse response) throws IOException {
        try {
            String verifiedEmail = verificationService.verifyToken(token);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

            String oldEmail = user.getEmail();
            user.updateEmail(verifiedEmail);

            log.info("이메일 변경 완료 - 사용자ID: {}, 이전 이메일: {}, 새 이메일: {}",
                     userId, oldEmail, verifiedEmail);

            response.sendRedirect(emailVerifySuccessUrl);
        } catch (BusinessException e) {
            log.error("이메일 변경 실패 - 사용자ID: {}, 토큰: {}, 에러: {}", userId, token, e.getMessage());
            response.sendRedirect(emailVerifyFailedUrl + "&code=" + e.getErrorCode().getCode());
        }
    }

    @Override
    @Transactional
    public void reissuePassword(Long userId, ReissuePasswordReq request) {
        validateReissuePassword(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.updatePassword(request.password());
    }

    @Override
    @Transactional
    public void updateSetting(Long userId, UserSettingReq request) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.updateSetting(request);
    }

    private void validateReissuePassword(ReissuePasswordReq request) {
        if (!PASSWORD_PATTERN.matcher(request.password()).matches()) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        if(!request.password().equals(request.confirmPassword())) {
            throw new BusinessException(AuthErrorCode.CONFIRM_PASSWORD_MISMATCH);
        }
    }

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
}
