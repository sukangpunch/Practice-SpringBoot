package me.foroauth2.oauth.kakao.dto.res;

import lombok.Builder;

@Builder
public record KaKaoUserInfoResDto(

        String email,
        String nickname,
        String profile_photo_url
){}
