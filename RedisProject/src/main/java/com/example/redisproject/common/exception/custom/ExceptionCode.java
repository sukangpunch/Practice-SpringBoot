package com.example.redisproject.common.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

// ExceptionCode 열거형은 다양한 예외 상황을 정의하고,
// 각 상황에 맞는 HTTP 상태 코드와 메시지를 관리하기 위한 구조적인 방법을 제공합니다.
public enum ExceptionCode {
    // User Exception
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."),
    PASSWORD_DUPLICATE(HttpStatus.BAD_REQUEST,"같은 비밀번호로 수정 할 수 없습니다."),
    LOGIN_ID_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 loginId 입니다."),
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    // Jwt Exception
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,  "토큰이 만료되었습니다."),
    TOKEN_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    TOKEN_UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 토큰입니다."),
    TOKEN_INVALID_FORMAT(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 비었거나 null입니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"리프레시 토큰이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }
}
