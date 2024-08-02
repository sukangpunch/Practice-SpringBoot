package me.foroauth2.oauth.kakao.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.foroauth2.common.dto.CommonResponse;
import me.foroauth2.oauth.kakao.service.KakaoOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class KaKaoOAuthController {

    private final KakaoOAuthService kaKaoOAuthService;

    //OpenAPI 문서 생성 도구에서 해당 메서드를 숨기는 역할을 한다.
    @Operation(hidden = true)
    @GetMapping(value = "/login-page/kakao")
    public ResponseEntity<Void> getGoogleAuthUrl(HttpServletRequest request) throws Exception {

        //HttpStatus.MOVED_PERMANENTLY 해야 리턴 된 uri 경로로 바로 이동 할 수 있다.
        return new ResponseEntity<>(kaKaoOAuthService.makeLoginURI(), HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(hidden = true)
    @GetMapping(value = "/login/kakao")
    public ResponseEntity<CommonResponse> sign(@RequestParam(value = "code") String authCode) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(1, "성공", kaKaoOAuthService.socialLogin(authCode)));
    }
}
