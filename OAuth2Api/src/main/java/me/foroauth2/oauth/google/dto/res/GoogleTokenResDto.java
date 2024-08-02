package me.foroauth2.oauth.google.dto.res;

import lombok.Builder;

@Builder
public record GoogleTokenResDto(
        String token_type,
        String id_token
){}
