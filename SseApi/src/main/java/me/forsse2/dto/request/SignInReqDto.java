package me.forsse2.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SignInReqDto(
        @Schema(
                example = "kang4746",
                description = "유저의 loginId를 입력해주세요"
        )
        String loginId,
        @Schema(
                example = "password",
                description = "유저의 password를 입력해주세요"
        )
        String password
)
{}
