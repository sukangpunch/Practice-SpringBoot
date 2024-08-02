package com.example.redisproject.contorller;


import com.example.redisproject.common.dto.CommonResponseDto;
import com.example.redisproject.data.dto.request.SignInReqDto;
import com.example.redisproject.data.dto.request.SignUpReqDto;
import com.example.redisproject.data.dto.response.SignInResDto;
import com.example.redisproject.data.dto.response.SignUpResDto;
import com.example.redisproject.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[User] User Login Api", description = "유저 로그인 api")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final SignService signService;
    private final UserDetailsService userDetailsService;

    @Operation(summary = "유저 로그인", description = "유저 로그인 구현")
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponseDto<SignInResDto>> signIn(@RequestBody SignInReqDto signInReqDto){
        SignInResDto signInResDto = signService.signIn(signInReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "로그인 성공",
                        signInResDto));
    }

    @Operation(summary = "유저 회원가입", description = "유저 회원가입 구현")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponseDto<SignUpResDto>> signUp(@RequestBody SignUpReqDto signUpReqDto){
        SignUpResDto signUpResDto = signService.signUp(signUpReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "회원가입 성공",
                        signUpResDto));
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "리프레시 토큰을 통해 AccessToken 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponseDto<SignInResDto>> reissue(HttpServletRequest request){
        SignInResDto signInResDto = signService.reissueAccessToken(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "리프레시 토큰으로 엑세스 토큰 재발급 성공",
                        signInResDto
                ));
    }

    @Operation(summary = "로그인 성공 테스트", description = "토큰이 제대로 동작하는지 확인")
    @GetMapping("/test")
    public ResponseEntity<String> tokenTest(@AuthenticationPrincipal UserDetails principal){
        String email = principal.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(String.format("토큰에서 이메일 추출"+email));
    }
}
