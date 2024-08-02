package com.example.redisproject.common.exception.handler;


import com.example.redisproject.common.dto.CommonResponseDto;
import com.example.redisproject.common.exception.custom.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// 전역 예외 처리
// @RestControllerAdvice 어노테이션이 붙은 클래스는
// 모든 @RestController 에서 발생하는 예외를 처리할 수 있는 컨트롤러 어드바이스(Controller Advice)로 동작합니다.
@RestControllerAdvice
@Log4j2

//ResponseEntityExceptionHandler 는 Spring 에서 제공하는 기본적인 예외 처리 클래스입니다.
//이 클래스를 상속받아 사용함으로써 기본적인 예외 처리 로직을 오버라이드하거나 확장할 수 있습니다.
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // @ExceptionHandler(CustomException.class) 어노테이션이 붙은 메서드는
    // CustomException 타입의 예외가 발생했을 때 호출됩니다.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponseDto> handleCustomException(CustomException e){
        log.error("예외가 발생하였습니다.: " + e.getExceptionCode().getMessage());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(new CommonResponseDto(e.getExceptionCode().getMessage(), null));
    }
}
