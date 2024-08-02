package me.foroauth2.exception.customs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
}
