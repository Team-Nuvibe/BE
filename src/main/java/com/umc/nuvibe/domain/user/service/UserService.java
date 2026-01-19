package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.ReissuePasswordReq;
import com.umc.nuvibe.domain.user.dto.request.UserSettingReq;
import com.umc.nuvibe.domain.user.dto.response.UserNicknameUpdateRes;
import com.umc.nuvibe.domain.user.dto.response.UserProfileImageRes;
import com.umc.nuvibe.domain.user.dto.response.UserSettingUpdateRes;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserProfileImageRes getUserProfileImage(Long userId);
    void updateProfileImage(Long userId, MultipartFile file);
    UserNicknameUpdateRes updateUserNickname(Long userId, String nickname);
    void requestEmailUpdate(Long userId, String email);
    void verifyAndUpdateEmailWithRedirect(Long userId, String token, HttpServletResponse response) throws IOException;
    void reissuePassword(Long userId, ReissuePasswordReq request);
    UserSettingUpdateRes updateSetting(Long userId, UserSettingReq request);


}
