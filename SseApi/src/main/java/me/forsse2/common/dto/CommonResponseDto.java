package me.forsse2.common.dto;

public record CommonResponseDto<T>(
        String msg,
        T result
) {
}
