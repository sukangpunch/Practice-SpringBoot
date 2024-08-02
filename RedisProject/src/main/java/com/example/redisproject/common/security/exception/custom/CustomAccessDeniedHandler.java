package com.example.redisproject.common.security.exception.custom;


import com.example.redisproject.common.dto.CommonResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
// AccessDeniedHandler 인터페이스를 구현하는 이유는 Spring Security에서 인증된 사용자가 필요한 권한이 없는 리소스에 접근하려고 할 때
// 발생하는 접근 거부(Access Denied) 상황을 커스터마이징하여 처리하기 위함입니다.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[CustomAccessDeniedHandler] 해당 리소스에 엑세스할 권한이 없습니다.");

        ObjectMapper objectMapper = new ObjectMapper();

        //HTTP 응답 헤더의 Content-Type을 JSON으로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //HTTP 응답 상태 코드를  403(FORBIDDEN)으로 설정합니다.
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");

        CommonResponseDto commonResponseDto = new CommonResponseDto("해당 리소스에 접근할 권한이 없습니다.", null);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
        }catch (IOException e){
            // getWriter() 메서드는 HttpServletResponse 객체의 메서드로, HTTP 응답 본문에 데이터를 쓰기 위해 PrintWriter 객체를 반환합니다.
            // 이 과정에서 입출력 예외가 발생할 수 있습니다.
            // write 메서드는 PrintWriter 객체의 메서드로, 지정된 문자열을 출력 스트림에 씁니다.
            // 이 과정에서도 입출력 예외가 발생할 수 있습니다.
            e.printStackTrace();
        }

    }
}
