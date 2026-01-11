package com.umc.nuvibe.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.nuvibe.global.apiPayLoad.error.CommonErrorCode;
import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 헤더에서 jwt 추출, 토큰 유효성 검증
    // 유효할 경우, 토큰에서 직접 식별자와 권한 정보를 추출하여 SecurityContextHolder에 저장
    // db 조회 없이 무상태로 동작
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 토큰 꺼내기
        String bearerToken = request.getHeader("Authorization");

        // authorization 헤더가 없는 경우 그냥 다음 필터로 넘겨버림
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            //bearer 토큰 추출
            String token= jwtTokenProvider.extractBearerToken(bearerToken);

            // 토큰 유효성 검증
            jwtTokenProvider.validateToken(token);

            // 토큰에서 사용자 id 추출
            Long userId=jwtTokenProvider.getUserIdFromToken(token);

            // 인증 객체 생성
            // db 조회를 하지 않으므로 credential은 null로, role도 로직상 존재하지 않으므로 emptyList로 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

            // SecurityContextHolder에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 인증 완료, 다음 필터로 진행
            filterChain.doFilter(request, response);

        } catch (BusinessException e) { // jwt 검증 실패 또는 사용자 조회 실패 등
            log.warn("[401] jwt 필터 인증 실패", e);
            setErrorResponse(response, e.getErrorCode());

        } catch (Exception e) { // 500 에러
            log.error("[500] jwt 필터 처리 중 예기치 못 한 오류 발생", e);
            setErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
        }


    }

    // JSON 응답 생성
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        Response<Void> errorResponse = Response.fail(errorCode);
        String json = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
