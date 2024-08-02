package me.forsse2.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import me.forsse2.entity.Todo;
import me.forsse2.entity.User;


public record TodoReqDto(
        @Schema(
                example = "1.손톱깎기, 2.아이스크림, 3. 물통",
                description = "할 일 내용을 작성해주세요."
        )
        String content,

        @Schema(
                example = "2024-10-29",
                description = "마감 기한을 작성해주세요."
        )
        String deadLine
) {
    public static Todo toEntity(TodoReqDto todoReqDto, User user){
        return Todo.builder()
                .content(todoReqDto.content())
                .deadLine(todoReqDto.deadLine())
                .user(user)
                .build();
    }
}
