package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.CommonErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.MailErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final EmailVerificationService verificationService;

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
        if (!userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }
        verificationService.sendVerificationEmail(email);
    }

    @Override
    @Transactional
    public void completeEmailUpdate(Long userId, String token) {
        String verifiedEmail=verificationService.verifyToken(token);

        User user=userRepository.findById(userId)
                .orElseThrow(()->new BusinessException(UserErrorCode.USER_NOT_FOUND));
        user.updateEmail(verifiedEmail);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String password) {

    }

    @Override
    @Transactional
    public void updateSetting(Long userId) {

    }
}
