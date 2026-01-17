package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.AuthProvider;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public void signUp(SignUpReq request) {
        validateSignUpRequest(request);

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        String encodedPassword=passwordEncoder.encode(request.password());

        User user=User.createLocalUser(
                request.name(),
                request.nickname(),
                request.email(),
                encodedPassword
        );

        userRepository.save(user);
    }

    @Override
    @Transactional
    public TokenRes login(LoginReq request) {
        User user=userRepository.findByEmail(request.email())
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(AuthErrorCode.PASSWORD_UNMATCH_ERROR);
            // passwordEncoder가 password를 해싱하여 비교해줌
        }

        String accessToken=jwtTokenProvider.createAccessToken(user,AuthProvider.LOCAL);
        String refreshToken=jwtTokenProvider.createRefreshToken(user, AuthProvider.LOCAL);

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenRes(accessToken,refreshToken);
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.updateRefreshToken(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void withdraw(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }


    private void validateSignUpRequest(SignUpReq request) {

        if (!PASSWORD_PATTERN.matcher(request.password()).matches()) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        if(!request.password().equals(request.confirmPassword())) {
            throw new BusinessException(AuthErrorCode.CONFIRM_PASSWORD_MISMATCH);
        }

        if (!EMAIL_PATTERN.matcher(request.email()).matches()) {
            throw new BusinessException(AuthErrorCode.INVAILD_EMAIL_FORMAT);
        }
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");


}
