package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.user.dto.request.ReissuePasswordReq;
import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import com.umc.nuvibe.domain.user.dto.response.UserNicknameUpdateRes;
import com.umc.nuvibe.domain.user.dto.response.UserProfileImageRes;
import com.umc.nuvibe.domain.user.dto.response.UserSettingUpdateRes;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.UserSetting;
import com.umc.nuvibe.domain.user.vo.VerificationType;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final EmailVerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserProfileImageRes getUserProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        return new UserProfileImageRes(user.getProfileImage());
    }

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
    public UserNicknameUpdateRes updateUserNickname(Long userId, String nickname) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        LocalDateTime lastUpdated=user.getLastNicknameUpdatedDate();

        if (!(lastUpdated == null || lastUpdated.plusDays(14).isBefore(LocalDateTime.now()))) {
            LocalDate nextAvailableDate = lastUpdated.plusDays(14).toLocalDate();
            throw new BusinessException(UserErrorCode.NICKNAME_UPDATE_RESTRICTED,
                    Map.of("nextAvailableDate", nextAvailableDate.toString()));
        }

        user.updateNickname(nickname);

        return new UserNicknameUpdateRes(nickname);
    }

    @Override
    @Transactional
    public void sendEmailVerification(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        verificationService.sendVerificationCode(email, VerificationType.EMAIL_CHANGE);
    }

    @Override
    @Transactional
    public void verifyEmailCode(String email, String code) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        verificationService.verifyCode(email, code, VerificationType.EMAIL_CHANGE);
    }

    @Override
    @Transactional
    public void updateEmail(Long userId, String newEmail) {
        // 이메일이 인증되었는지 확인
        verificationService.checkCodeIsVerified(newEmail, VerificationType.EMAIL_CHANGE);

        if (userRepository.existsByEmail(newEmail)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.updateEmail(newEmail);


    }

    @Override
    @Transactional
    public void reissuePassword(Long userId, ReissuePasswordReq request) {
        validateReissuePassword(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.password());
        user.updatePassword(encodedPassword);

    }

    @Override
    @Transactional
    public UserSettingUpdateRes updateSetting(Long userId, UserSettingReq request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 변경 전 설정 기억해두기
        UserSetting oldSetting = user.getSetting();

        user.updateSetting(request);

        // 변경 후 설정 가져온 다음 비교
        UserSetting newSetting = user.getSetting();
        List<String> changeLogs = newSetting.getDiff(oldSetting);

        return new UserSettingUpdateRes(newSetting, changeLogs);
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
