package me.foroauth2.oauth.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.foroauth2.common.domain.Role;
import me.foroauth2.jwt.JwtProvider;
import me.foroauth2.jwt.redis.RefreshToken;
import me.foroauth2.jwt.redis.RefreshTokenRepository;
import me.foroauth2.user.domain.User;
import me.foroauth2.oauth.kakao.dto.req.KaKaoTokenReqDto;
import me.foroauth2.oauth.kakao.dto.res.KaKaoUserInfoResDto;
import me.foroauth2.oauth.kakao.dto.res.KaKaoTokenResDto;
import me.foroauth2.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakaoOAuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoAuthGrantType;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;


    /* 인가 코드를 받기 위한 메서드 */
    public HttpHeaders makeLoginURI(){
        String reqUrl = kakaoAuthUrl+"?response_type=code"+"&client_id="+kakaoClientId+
                "&redirect_uri="+kakaoRedirectUrl+
                "&scope=profile_nickname,profile_image,account_email";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        return headers;
    }

    /* 인가 코드를 통해 로그인/ 회원가입 구현 */
    public Map<String, String> socialLogin(String authCode) throws JsonProcessingException {

        KaKaoTokenReqDto kaKaoTokenReqDto = KaKaoTokenReqDto.builder()
                .clientId(kakaoClientId)
                .code(authCode)
                .redirectUri(kakaoRedirectUrl)
                .grantType(kakaoAuthGrantType)
                .clientSecret(kakaoClientSecret)
                .build();

        String kakaoToken = getKaKaoToken(kaKaoTokenReqDto);

        KaKaoUserInfoResDto kaKaoOAuthMemberResDto = getKaKaoInfo(kakaoToken);

        User user = checkExistMember(kaKaoOAuthMemberResDto);
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

    public String getKaKaoToken(KaKaoTokenReqDto kaKaoOAuthReqDto){
        WebClient webClient = WebClient.create();

        //LinkedMultiValueMap 내부적으로는 Map<String, List<String>> 형태로 데이터를 저장 / 각 키에 대해 여러 개의 값을 리스트 형태로 저장
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", kaKaoOAuthReqDto.grantType());
        formData.add("client_id", kaKaoOAuthReqDto.clientId());
        formData.add("client_secret", kaKaoOAuthReqDto.clientSecret());
        formData.add("redirect_uri", kaKaoOAuthReqDto.redirectUri());
        formData.add("code", kaKaoOAuthReqDto.code());

        //WebClient 를 사용하여 OAuth 인증 코드를 사용하여 kakao OAuth 서버에 액세스 토큰을 요청. 이를 위해 POST 요청을 보내고, 응답으로부터 KaKaoOAuthResDto를 얻습니다.
        KaKaoTokenResDto kaKaoOAuthResDto = webClient.post()
                .uri(kakaoTokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8")
                .bodyValue(formData)
                // WebClient 를 사용하여 HTTP 요청을 보내고, 해당 요청에 대한 응답을 비동기식으로 수신하는데 사용.
                .retrieve()
                // 받은 응답의 본문을 Mono로 변환합니다. Mono는 0 또는 1개의 요소를 가질 수 있는 Reactor 프로젝트 타입으로, 비동기 결과를 표현하는데 사용.
                .bodyToMono(KaKaoTokenResDto.class)
                // Mono 를 동기적으로 가져와서 해당 결과를 반환, 이 메서드는 호출 스레드를 차단하고 Mono 에서 결과를 기다립니다. 즉 이 부분에서 비동기식으로 받은 응답을 기다렸다가 결과를 반환합니다.
                .block();

        log.info("responseBody {}", kaKaoOAuthResDto);

        return Objects.requireNonNull(kaKaoOAuthResDto).access_token().toString();
    }

    public KaKaoUserInfoResDto getKaKaoInfo(String kakaoToken) throws JsonProcessingException {

        WebClient webClient = WebClient.create();

        String resultJson = webClient.get()
                .uri(kakaoUserInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        KaKaoUserInfoResDto kaKaoOAuthMemberResDto = parseKakaoUserInfo(resultJson);


        // resultJson 을 OAuthMemberResDto 의 객체로 변환한다, 즉 OAuthMemberResDto 클래스의 객체로 매핑하는 것
        return kaKaoOAuthMemberResDto;
    }

    public KaKaoUserInfoResDto parseKakaoUserInfo(String resultJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(resultJson);

        // Extract required information
        JsonNode kakaoAccountNode = rootNode.path("kakao_account");
        String email = kakaoAccountNode.path("email").asText();
        String nickname = kakaoAccountNode.path("profile").path("nickname").asText();
        String profileImage = kakaoAccountNode.path("profile").path("profile_image_url").asText();

        // Populate DTO
        KaKaoUserInfoResDto kaKaoOAuthMemberResDto = KaKaoUserInfoResDto.builder()
                .email(email)
                .nickname(nickname)
                .profile_photo_url(profileImage)
                .build();

        return kaKaoOAuthMemberResDto;
    }

    public User checkExistMember(KaKaoUserInfoResDto kaKaoOAuthMemberResDto){
        Optional<User> member = memberRepository.findByEmail(kaKaoOAuthMemberResDto.email());
        User returnMember;

        if(member.isEmpty()){
            log.info("[getGoogleInfo] 첫 로그인, 회원 가입 시작");

            List<String> role = new ArrayList<>();
            role.add(Role.ROLE_USER.getRole());

            returnMember = new User("Google", kaKaoOAuthMemberResDto.nickname(),
                    kaKaoOAuthMemberResDto.email(),role);

            memberRepository.save(returnMember);
            log.info("[getGoogleInfo] 회원가입 성공");
            return returnMember;
        }

        log.info("[getGoogleInfo] 이미 가입된 유저, 데이터베이스에서 사용자 정보 가져오기");
        log.info("[getGoogleInfo] 로그인 성공");
        return member.get();

    }

}
