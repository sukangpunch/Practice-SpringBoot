package com.example.redisproject.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, // 클라이언트 요청 객체 
                                    HttpServletResponse response, // 서버의 응답 객체
                                    FilterChain filterChain) throws ServletException, IOException { // 다음 필터로 요청을 전달하기 위한 객체
        log.info("[doFilterInternal] 토큰 가져오기");
        String token = jwtProvider.getAuthorizationToken(request); // jwt 토큰을 추출
        log.info("[doFilterInternal] Token  = {}", token);

        if(token != null && jwtProvider.validateToken(token)){ // 토큰이 유요한지 검증
            Authentication authentication = jwtProvider.getAuthentication(token); // 해당 토큰을 기반으로 인증 객체를 생성
            SecurityContextHolder.getContext().setAuthentication(authentication); // Spring Security의 SecurityContext 에 인증 객체 설정
            log.info("[doFilterInternal] 토큰 값 유효성 검증 완료");
        }

        filterChain.doFilter(request, response); // filterChain.doFilter() 를 호출하여, 요청을 다음 필터로 전달
    }
}
