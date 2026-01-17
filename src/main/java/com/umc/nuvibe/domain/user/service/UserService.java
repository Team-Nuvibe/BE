package com.umc.nuvibe.domain.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void updateProfileImage(Long userId, MultipartFile file);
    void updateUserNickname(Long userId, String nickname);
    void updateEmail(Long userId, String email);
    void updatePassword(Long userId, String password);
    void updateSetting(Long userId);


}
