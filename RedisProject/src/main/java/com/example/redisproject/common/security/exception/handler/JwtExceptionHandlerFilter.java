package com.example.redisproject.common.security.exception.handler;

import com.example.redisproject.common.dto.CommonResponseDto;
import com.example.redisproject.common.exception.custom.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// JWT를 파싱하고 검증하는 과정에서 발생하는 예외를 처리합니다.
// 보통 UsernamePasswordAuthenticationFilter 전에 실행되도록 설정합니다.
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (DecodingException e) { //jwt 디코딩 중 발생할 수 있는 예외. Base64 형식이 아닌경우, 헤더,페이로드,서명이 유효하지 않은경우, 페이로드 파싱에 문제가 있는경우
            setErrorResponse(response, ExceptionCode.TOKEN_INVALID_FORMAT);
            log.info("[JwtExceptionHandlerFilter] error name = DecodingException");
        } catch (MalformedJwtException e) { //jwt 형식이 잘못되었을 때 발생, 헤더,페이로드,서명 누락 또는 잘못된 형식.
            setErrorResponse(response, ExceptionCode.TOKEN_UNAUTHENTICATED);
            log.info("[JwtExceptionHandlerFilter] error name = MalformedJwtException");
        } catch (ExpiredJwtException e) { //jwt 만료되었을 때 발생하는 예외
            setErrorResponse(response, ExceptionCode.TOKEN_EXPIRED);
            log.info("[JwtExceptionHandlerFilter] error name = ExpiredJwtException");
        } catch (IllegalArgumentException e) { // 잘못된 인수나 인수의 값이 올바르지 않을 때 발생
            setErrorResponse(response, ExceptionCode.TOKEN_NOT_FOUND);
            log.info("[JwtExceptionHandlerFilter] error name = IllegalArgumentException");
        } catch (NullPointerException e) { //참조하는 객체가 없는 상태에서 해당 객체의 멤버나 메서드에 접근하려고 할 때.
            setErrorResponse(response, ExceptionCode.TOKEN_NOT_FOUND);
            log.info("[JwtExceptionHandlerFilter] error name = NullPointerException");
        }
    }

    private void setErrorResponse(HttpServletResponse response,
                                  ExceptionCode exceptionMessage){

        //ObjectMapper = Jackson 라이브러리의 객체로, Java 객체를 JSON으로 변환하는 데 사용됩니다.
        ObjectMapper objectMapper = new ObjectMapper();
        //상태 코드는 요청이 성공적으로 처리되었는지, 클라이언트가 잘못된 요청을 보냈는지, 인증이 필요한지 등 다양한 정보를 클라이언트에게 전달합니다.
        response.setStatus(exceptionMessage.getStatus().value());
        //콘텐츠 타입은 클라이언트에게 응답 본문의 데이터 형식을 지정합니다. 이는 클라이언트가 데이터를 올바르게 파싱하고 처리할 수 있도록 합니다.
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정 추가
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        CommonResponseDto commonResponseDto = new CommonResponseDto(exceptionMessage.getMessage(), null);

        try{
            response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
