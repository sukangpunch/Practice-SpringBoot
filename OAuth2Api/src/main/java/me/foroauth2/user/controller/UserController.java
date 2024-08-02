package me.foroauth2.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.foroauth2.config.SwaggerApiResponses;
import me.foroauth2.common.dto.CommonResponse;
import me.foroauth2.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{userId}")
    @Operation(summary = "유저 정보 조회", description = "userId로 user 를 조회합니다.")
    @SwaggerApiResponses
    public ResponseEntity<CommonResponse> getUserInfo(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(1, "성공적으로 유저 정보를 조회하였습니다.", userService.getUserInfo(userId)));
    }

    @DeleteMapping(value = "/delete/{userId}")
    @Operation(summary = "유저 삭제", description = "user를 삭제합니다.")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(1, "성공적으로 유저를 삭제하였습니다.",null));
    }
}
