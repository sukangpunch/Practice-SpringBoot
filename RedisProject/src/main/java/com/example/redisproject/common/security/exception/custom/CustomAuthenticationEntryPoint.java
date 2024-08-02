package com.example.redisproject.common.security.exception.custom;

import com.example.redisproject.common.dto.CommonResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[CustomAuthenticationEntryPoint] 인증에 실패하였습니다. : " + authException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        //HTTP 응답 헤더의 Content-Type을 JSON으로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //HTTP 응답 상태 코드를 401 Unauthorized로 설정합니다.
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");

        CommonResponseDto commonResponse = new CommonResponseDto("인증되지 않은 사용자입니다. 로그인을 수행하세요",null);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
        } catch (IOException e){
            // getWriter() 메서드는 HttpServletResponse 객체의 메서드로, HTTP 응답 본문에 데이터를 쓰기 위해 PrintWriter 객체를 반환합니다.
            // 이 과정에서 입출력 예외가 발생할 수 있습니다.
            // write 메서드는 PrintWriter 객체의 메서드로, 지정된 문자열을 출력 스트림에 씁니다.
            // 이 과정에서도 입출력 예외가 발생할 수 있습니다.
            e.printStackTrace();
        }
    }
}
