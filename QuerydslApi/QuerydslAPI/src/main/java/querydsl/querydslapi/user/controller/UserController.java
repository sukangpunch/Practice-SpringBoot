package querydsl.querydslapi.user.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import querydsl.querydslapi.user.dto.response.QueryDslDto;
import querydsl.querydslapi.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @Parameter(name = "userId", description = "querydsl 을 위한 userId")
    @GetMapping("/{userId}")
    public ResponseEntity<QueryDslDto> getQuerydsl(@PathVariable("userId")Long userId){

        QueryDslDto queryDslDto = userService.getQueryDsl(userId);

        return ResponseEntity.status(HttpStatus.OK).body(queryDslDto);
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestParam String name){

        String result = userService.createUser(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }



}
