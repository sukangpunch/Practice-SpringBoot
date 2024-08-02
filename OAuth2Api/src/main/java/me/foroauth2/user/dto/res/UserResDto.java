package me.foroauth2.user.dto.res;

import lombok.Builder;
import me.foroauth2.user.domain.User;


@Builder
public record UserResDto(
        Long userId,
        String email,
        String name
){
    public static UserResDto toDto(User user)
    {
        return UserResDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
