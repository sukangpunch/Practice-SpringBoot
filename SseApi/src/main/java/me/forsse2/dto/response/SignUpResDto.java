package me.forsse2.dto.response;


import lombok.Builder;
import me.forsse2.entity.User;

@Builder
public record SignUpResDto(
    Long id,
    String name,
    String loginId,
    String password,
    String email

){
    public static SignUpResDto toDto(User user){
        return SignUpResDto.builder()
                .id(user.getId())
                .name(user.getName())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }

}
