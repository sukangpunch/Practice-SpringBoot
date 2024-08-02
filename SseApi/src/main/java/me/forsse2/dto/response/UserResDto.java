package me.forsse2.dto.response;

import lombok.Builder;
import me.forsse2.entity.User;

@Builder

public record UserResDto(
        Long id,
        String name,
        String loginId,
        String email
){
    public static UserResDto toDto(User user){
        return UserResDto.builder()
                .id(user.getId())
                .name(user.getName())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .build();
    }
}
