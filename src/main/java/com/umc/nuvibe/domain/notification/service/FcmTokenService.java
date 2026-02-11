package com.umc.nuvibe.domain.notification.service;

import com.umc.nuvibe.domain.notification.entity.Fcm;
import com.umc.nuvibe.domain.notification.repository.FcmRepository;
import com.umc.nuvibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmRepository fcmRepository;

    @Transactional
    public void deactivateToken(String token) {
        List<Fcm> fcmList = fcmRepository.findByToken(token);
        fcmList.forEach(Fcm::deactivate);
    }

    @Transactional
    public void deactivateAllTokens(User user) {
        List<Fcm> tokens = fcmRepository.findByUserAndIsActiveTrue(user);
        tokens.forEach(Fcm::deactivate);
    }
}
