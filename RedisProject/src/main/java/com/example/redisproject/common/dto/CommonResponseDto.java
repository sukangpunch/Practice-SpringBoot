package com.example.redisproject.common.dto;

public record CommonResponseDto<T>(
        String msg,
        T result
) {
}
