package me.forsse2.dto.response;


import lombok.Builder;
import me.forsse2.entity.Todo;
import me.forsse2.entity.User;


@Builder
public record TodoResDto(
        Long id,
        String content,
        String deadLine,
        Long userId
        ){
    public static TodoResDto toDto(Todo todo)
    {
        return TodoResDto.builder()
                .id(todo.getId())
                .content(todo.getContent())
                .deadLine(todo.getDeadLine())
                .userId(todo.getUser().getId())
                .build();
    }

}
