package me.forsse2.dto.response;


import lombok.Builder;

@Builder
public record SignInResDto(
        String accessToken
)
{}
