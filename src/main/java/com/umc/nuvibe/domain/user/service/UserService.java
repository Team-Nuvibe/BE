package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.ReissuePasswordReq;
import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void updateProfileImage(Long userId, MultipartFile file);
    void updateUserNickname(Long userId, String nickname);
    void requestEmailUpdate(Long userId, String email);
    void completeEmailUpdate(Long userId, String token);
    void reissuePassword(Long userId, ReissuePasswordReq request);
    void updateSetting(Long userId, UserSettingReq request);


}
