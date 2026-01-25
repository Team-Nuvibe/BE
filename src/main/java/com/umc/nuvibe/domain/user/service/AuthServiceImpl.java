package com.umc.nuvibe.domain.user.service;

import com.umc.nuvibe.domain.user.dto.request.CheckPasswordReq;
import com.umc.nuvibe.domain.user.dto.request.LoginReq;
import com.umc.nuvibe.domain.user.dto.request.SignUpReq;
import com.umc.nuvibe.domain.user.dto.response.TokenRes;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.domain.user.repository.UserRepository;
import com.umc.nuvibe.domain.user.vo.AuthProvider;
import com.umc.nuvibe.domain.user.vo.VerificationType;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.UserErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.security.jwt.JwtTokenProvider;
import com.umc.nuvibe.global.service.EmailVerificationService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService verificationService;


    public AuthServiceImpl(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider,
                          EmailVerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.verificationService = verificationService;
    }

    @Override
    @Transactional
    public void signUp(SignUpReq request) {
        validateSignUpRequest(request);

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        // 이메일이 인증되었는지 확인
        verificationService.checkEmailVerified(request.email());

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


    @Override
    public void sendJoinVerificationCode(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        verificationService.sendVerificationCode(email, VerificationType.JOIN);
    }

    @Override
    @Transactional
    public void verifyJoinCode(String email, String code) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }

        verificationService.verifyCode(email, code, VerificationType.JOIN);
    }

    @Override
    public void sendPasswordResetCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        verificationService.sendVerificationCode(email, VerificationType.PASSWORD_RESET);
    }

    @Override
    @Transactional
    public void verifyPasswordResetCode(String email, String code) {
        if (!userRepository.existsByEmail(email)) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        verificationService.verifyCode(email, code, VerificationType.PASSWORD_RESET);
    }

    @Override
    @Transactional
    public void resetPasswordWithCode(String email, String code, String newPassword, String confirmPassword) {
        // 비밀번호 유효성 검사
        validatePassword(newPassword, confirmPassword);

        // 코드가 인증되었는지 확인
        verificationService.checkCodeIsVerified(email, VerificationType.PASSWORD_RESET);

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 비밀번호 변경
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        // RefreshToken 무효화
        user.updateRefreshToken(null);
        userRepository.save(user);
    }


    @Override
    public void checkCurrentPassword(Long userId, CheckPasswordReq request) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(AuthErrorCode.PASSWORD_UNMATCH_ERROR);
        }
    }

    @Override
    @Transactional
    public TokenRes reissueToken(String authorizationHeader) {
        String refreshToken = jwtTokenProvider.extractBearerToken(authorizationHeader);

        jwtTokenProvider.validateToken(refreshToken);

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new BusinessException(AuthErrorCode.JWT_INVALID_TOKEN);
        }

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        AuthProvider authProvider = AuthProvider.valueOf(claims.get("AuthProvider", String.class));

        String newAccessToken = jwtTokenProvider.createAccessToken(user, authProvider);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user, authProvider);

        user.updateRefreshToken(newRefreshToken);

        return new TokenRes(newAccessToken, newRefreshToken);
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

    private void validatePassword(String password, String confirmPassword) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        if (!password.equals(confirmPassword)) {
            throw new BusinessException(AuthErrorCode.CONFIRM_PASSWORD_MISMATCH);
        }
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");


}
