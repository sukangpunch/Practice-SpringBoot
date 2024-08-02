package com.example.redisproject.data.dto.response;

import com.example.redisproject.data.domain.User;
import lombok.Builder;

@Builder
public record SignUpResDto(
        Long id,
        String name,
        String email
){
    public static SignUpResDto toDto(User user){
        return SignUpResDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
