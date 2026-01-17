package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.image.service.ImageService;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ImageService imageService;
    private final UserRepository userRepository;

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
    public void updateNickname(Long userId, String nickname) {

    }

    @Override
    @Transactional
    public void updateEmail(Long userId, String email) {

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
