package me.forsse2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.forsse2.common.dto.CommonResponseDto;
import me.forsse2.dto.request.SignInReqDto;
import me.forsse2.dto.request.SignUpReqDto;
import me.forsse2.dto.response.SignInResDto;
import me.forsse2.dto.response.SignUpResDto;
import me.forsse2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name ="[User] User API", description = "유저 생성,유저 조회, 유저 수정, 유저 삭제")
@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "유저 삭제",description = "유저 번호로 유저를 삭제합니다.")
    //@Parameter(name = "userId", description = "삭제할 유저의 id")
    @DeleteMapping()
    public ResponseEntity<CommonResponseDto<Void>> deleteUser(){
        //SecurityContextHolder에서 현재 인증된 사용자의 정보를 담고 있는 Authentication 객체를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Authentcation객체가 가지고 있는 Principal 객체가 반환됩니다. 이 객체는 UserDetails 인터페이스를 구현한 사용자 정보 객체입니다.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //UserDetails객체에서 인증된 사용자의 loginId를 getUsername()메서드로 가져옵니다.
        String loginId = userDetails.getUsername(); // 로그인한 사용자의 아이디

        userService.deleteUser(loginId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "유저 삭제가 성공적으로 완료되었습니다.",
                        null));
    }

    @Operation(summary = "회원가입", description = "회원 가입 할 유저의 정보를 입력합니다.<br> -회원가입 화면-")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponseDto<SignUpResDto>> signUp(@RequestBody SignUpReqDto userReqDto){
        //String role = "USER";

        SignUpResDto userResDto = userService.signUp(userReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CommonResponseDto<>(
                        "유저 회원가입을 성공적으로 완료하였습니다.",
                        userResDto));
    }

    @Operation(summary = "로그인", description = "회원 가입 한 유저의 loginId와 password를 입력합니다.<br> -로그인 화면-")
    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponseDto<SignInResDto>> signIn(@RequestBody SignInReqDto signInReqDto){
        SignInResDto signInResDto = userService.signIn(signInReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "유저 로그인을 성공적으로 완료하였습니다.",
                        signInResDto));
    }

    //현재 인증된 사용자의 loginId를 가져옵니다.
    public String getLoginId(){
        //SecurityContextHolder에서 현재 인증된 사용자의 정보를 담고 있는 Authentication 객체를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Authentcation객체가 가지고 있는 Principal 객체가 반환됩니다. 이 객체는 UserDetails 인터페이스를 구현한 사용자 정보 객체입니다.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
    }

}
