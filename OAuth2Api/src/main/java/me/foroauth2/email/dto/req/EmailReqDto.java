package me.foroauth2.email.dto.req;

import lombok.Builder;

@Builder
public record EmailReqDto(
        String mail,
        String verifyCode
) {
}
