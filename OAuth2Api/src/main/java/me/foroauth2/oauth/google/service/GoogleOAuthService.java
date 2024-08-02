package me.foroauth2.oauth.google.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.foroauth2.common.domain.Role;
import me.foroauth2.jwt.JwtProvider;
import me.foroauth2.jwt.redis.RefreshToken;
import me.foroauth2.jwt.redis.RefreshTokenRepository;
import me.foroauth2.user.domain.User;
import me.foroauth2.oauth.google.dto.req.GoogleTokenReqDto;
import me.foroauth2.oauth.google.dto.res.GoogleTokenResDto;
import me.foroauth2.oauth.google.dto.res.GoogleUserInfoResDto;
import me.foroauth2.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GoogleOAuthService {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String googleAuthUrl;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUrl;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUrl;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String googleAuthGrantType;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    public HttpHeaders makeLoginURI(){
        String reqUrl = googleAuthUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        return headers;
    }

    public Map<String, String> socialLogin(String authCode) throws JsonProcessingException{
        //Google OAuth에 필요한 요청 정보를 담은 dto 생성
        GoogleTokenReqDto googleTokenReqDto = GoogleTokenReqDto.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType(googleAuthGrantType)
                .build();

        /* 해당 dto 를 이용하여 구글에서 AccessToken 을 받아온다. */
        String googleToken = getGoogleToken(googleTokenReqDto);

        /* 해당 AccessToken을 이용하여 사용자의 정보를 불러온다 */
        GoogleUserInfoResDto googleUserInfoResDto = getGoogleInfo(googleToken);

        /* 해당 dto를 이용하여 member가 존재하면 로그인, member가 존재하지 않으면 회원가입 -> 로그인 을 수행함*/
        User user = checkExistMember(googleUserInfoResDto);
        Map<String, String> tokens = new HashMap<>();

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getEmail(), user.getRole());

        tokens.put("Access", accessToken);
        tokens.put("Refresh", refreshToken);

        //레디스에 Refresh 토큰을 저장한다. (사용자 기본키 Id, refresh 토큰 저장)
        refreshTokenRepository.save(new RefreshToken(user.getUserId(), refreshToken));

        return tokens;
    }

    public String getGoogleToken(GoogleTokenReqDto googleTokenReqDto){

        WebClient webClient = WebClient.create();
        //WebClient를 사용하여 OAuth 인증 코드를 사용하여 Google OAuth 서버에 액세스 토큰을 요청. 이를 위해 POST 요청을 보내고, 응답으로부터 GoogleOAuthResDto를 얻습니다.
        GoogleTokenResDto googleTokenResDto = webClient.post()
                .uri(googleTokenUrl + "/token")
                .bodyValue(googleTokenReqDto)
                // WebClient를 사용하여 HTTP 요청을 보내고, 해당 요청에 대한 응답을 비동기식으로 수신하는데 사용.
                .retrieve()
                // 받은 응답의 본문을 Mono로 변환합니다. Mono는 0 또는 1개의 요소를 가질 수 있는 Reactor 프로젝트 타입으로, 비동기 결과를 표현하는데 사용.
                .bodyToMono(GoogleTokenResDto.class)
                // Mono를 동기적으로 가져와서 해당 결과를 반환, 이 메서드는 호출 스레드를 차단하고 Mono에서 결과를 기다립니다. 즉 이 부분에서 비동기식으로 받은 응답을 기다렸다가 결과를 반환합니다.
                .block();

        //id_token이 null이면, NullPointerException 발생
        return Objects.requireNonNull(googleTokenResDto).id_token();
    }

    public GoogleUserInfoResDto getGoogleInfo(String googleToken) throws JsonProcessingException {

        WebClient webClient = WebClient.create();
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleUserInfoUrl)
                // id_token 이라는 이름의 매개변수에 googleToken 값을 설정합니다. Google OAuth 서버에 대한 요청에 인증 토큰을 전달하는데 사용
                .queryParam("id_token", googleToken)
                // UriComponentsBuilder에 구성된 URL을 문자열 형식으로 반환.
                .toUriString();

        String resultJson = webClient.get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // resultJson 을 GoogleUserInfoResDto 의 객체로 변환한다, 즉 GoogleUserInfoResDto 클래스의 객체로 매핑하는 것
        return objectMapper.readValue(resultJson, GoogleUserInfoResDto.class);
    }

    public User checkExistMember(GoogleUserInfoResDto googleUserInfoResDto){
        Optional<User> oldUser = userRepository.findByEmail(googleUserInfoResDto.email());
        User newUser;
        
        if(oldUser.isEmpty()){
            log.info("[getGoogleInfo] 첫 로그인, 회원 가입 시작");

            List<String> role = new ArrayList<>();
            role.add(Role.ROLE_USER.getRole());

            newUser = new User("Google", googleUserInfoResDto.given_name(),
                    googleUserInfoResDto.email(),role);

            userRepository.save(newUser);
            log.info("[getGoogleInfo] 회원가입 성공");
            return newUser;
        }

        log.info("[getGoogleInfo] 이미 가입된 유저, 데이터베이스에서 사용자 정보 가져오기");

        return oldUser.get();
    }
}
