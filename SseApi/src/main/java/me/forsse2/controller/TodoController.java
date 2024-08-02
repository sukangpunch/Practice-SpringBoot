package me.forsse2.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.forsse2.common.dto.CommonResponseDto;
import me.forsse2.dto.request.TodoReqDto;
import me.forsse2.dto.response.TodoResDto;
import me.forsse2.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;

    @Operation(summary = "sse 연결", description = "sse를 연결합니다.")
    @GetMapping(value = "/subscribe",produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetails principal){
        return todoService.subscribe(principal.getUsername());
    }

    @Operation(summary = "투두리스트 추가",description = "해당 유저의 투두를 셍성합니다.<br>투두 추가 화면")
    @PostMapping()
    public ResponseEntity<CommonResponseDto<TodoResDto>> createTodo(@AuthenticationPrincipal UserDetails principal,
                                                                    @RequestBody TodoReqDto todoReqDto){
        log.info("[createTodo]  할일을 생성합니다.");

        TodoResDto todoResponseDto = todoService.saveTodo(principal.getUsername(), todoReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "투두 생성이 성공적으로 완료되었습니다.",
                        todoResponseDto));
    }


}
