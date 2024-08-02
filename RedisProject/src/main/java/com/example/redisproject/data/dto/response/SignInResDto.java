package com.example.redisproject.data.dto.response;

import lombok.Builder;

@Builder
public record SignInResDto(
        String accessToken,
        String refreshToken
){}
