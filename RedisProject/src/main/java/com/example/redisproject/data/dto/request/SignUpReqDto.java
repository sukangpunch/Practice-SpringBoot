package com.example.redisproject.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpReqDto(
        @Schema(
                example = "강형준",
                description = "유저 이름을 입력해 주세요."
        )
        String name,
        @Schema(
                example = "popora99@gmail.com",
                description = "이메일을 입력해 주세요"
        )
        String email,

        @Schema(
                example = "1234k",
                description = "비밀번호를 입력해 주세요."
        )
        String password
){}
