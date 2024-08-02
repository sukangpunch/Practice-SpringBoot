package me.foroauth2.oauth.google.dto.req;

import lombok.Builder;

@Builder
public record GoogleTokenReqDto(
        String redirectUri,
        String clientId,
        String clientSecret,
        String code,
        String grantType

)
{}
