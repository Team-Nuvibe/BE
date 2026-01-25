package com.umc.nuvibe.global.security.websocket;

import com.umc.nuvibe.domain.tribe.repository.UserTribeRepository;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import com.umc.nuvibe.global.apiPayLoad.error.AuthErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.WebsocketErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor{

    // 문자열 전체가 형식에 맞을 때만 허용
    private static final Pattern TRIBE_TOPIC = Pattern.compile("^/topic/tribe\\.(\\d+)$");

    private final JwtTokenProvider jwtTokenProvider;
    private final UserTribeRepository userTribeRepository;

    //STOMP 프레임이 서버로 들어오기 직전에 호출
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //STOMP 전용 헤더로 다루기 위해 래핑
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 내부 프레임은 통과
        if (accessor.getCommand() == null) {
                return message;
        }

        // 변경사항이 메시지에 저장되도록
        accessor.setLeaveMutable(true);

        //STOMP 명령어별로 분기 처리
        return switch (accessor.getCommand()) {
            case CONNECT -> handleConnect(message, accessor);
            case SUBSCRIBE -> handleSubscribe(message, accessor);
            default -> message;
        };
    }

    // CONNECT 프레임 처리
    private Message<?> handleConnect(Message<?> message, StompHeaderAccessor accessor) {

        // STOMP native header에서 인증값 추출
        String bearer = accessor.getFirstNativeHeader("Authorization");
        // 실제 토큰만 분리
        String token = jwtTokenProvider.extractBearerToken(bearer);

        // 토큰 유효성 검사
        if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new BusinessException(AuthErrorCode.JWT_INVALID_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // Principal을 userId로 고정
        accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, List.of()));

        Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
        if (sessionAttrs != null) sessionAttrs.put("userId", userId);

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }


    // SUBSCRIBE 프레임 처리
    private Message<?> handleSubscribe(Message<?> message, StompHeaderAccessor accessor) {

        // CONNECT 단계에서 저장한 userId 추출
        Long userId = extractUserId(accessor);

        // 구독 대상 주소
        String destination = accessor.getDestination();

        if (destination == null) {
                return message;
        }

        // 목적지가 트라이브 채널 형식인지 검사용
        Matcher m = TRIBE_TOPIC.matcher(destination);

        if (!m.matches()) {
            return message;
        }

        // 정규식에서 tribeId 추출
        Long tribeId = Long.valueOf(m.group(1));

        // 유저가 해당 트라이브에 ACTIVE 상태로 참여 중인지 확인
        boolean ok = userTribeRepository.existsByUser_IdAndTribe_IdAndUserTribeStatus(
                userId,
                tribeId,
                UserTribeStatus.ACTIVE
        );

        // 아닐 시 구독 실패
        if (!ok) {
                throw new BusinessException(WebsocketErrorCode.WS_SUBSCRIBE_FORBIDDEN);
        }

        return message;
    }

    private Long extractUserId(StompHeaderAccessor accessor) {
        Principal p = accessor.getUser();
        if (p instanceof Authentication auth && auth.getPrincipal() instanceof Long id) {
                return id;
        }

        Object v = accessor.getSessionAttributes() == null ? null : accessor.getSessionAttributes().get("userId");
        if (v instanceof Long id) return id;

        throw new BusinessException(WebsocketErrorCode.WS_UNAUTHORIZED);
    }
}

