package me.foroauth2.oauth.kakao.dto.res;

import lombok.Builder;

@Builder
public record KaKaoTokenResDto(
        String access_token,
        String refresh_token

) {
}
