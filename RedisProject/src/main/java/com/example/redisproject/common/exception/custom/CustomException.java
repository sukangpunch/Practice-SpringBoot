package com.example.redisproject.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
// CustomException 클래스는 RuntimeException을 상속받아 사용자 정의 예외 클래스를 정의합니다.
// 예외 발생 시에 ExceptionCode를 포함하여 예외 정보를 전달할 수 있도록 설계되었습니다.
public class CustomException extends RuntimeException {
    // CustomException 객체가 생성될 때 필요한 예외 코드(ExceptionCode)를 나타내는 필드입니다.
    // ExceptionCode는 예외 발생 시 반환할 HTTP 상태 코드와 메시지를 정의하는 열거형(enum)입니다.
    private final ExceptionCode exceptionCode;
}
