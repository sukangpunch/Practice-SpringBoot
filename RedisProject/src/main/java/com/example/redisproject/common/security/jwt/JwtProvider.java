package com.example.redisproject.common.security.jwt;


import com.example.redisproject.common.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// Spring이 해당 클래스를 컴포넌트 스캔을 통해 자동으로 빈 등록을 합니다.
// 이후 이 빈은 다른 클래스에서 필요한 곳에 주입되거나, @Autowired를 사용하여 DI를 통해 사용될 수 있습니다.
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsServiceImpl userDetailsService;

    private Key secretKey;

    @Value("${accessTokenValidTime}")
    private Long accessTokenValidTime;

    @Value("${refreshTokenValidTime}")
    private Long refreshTokenValidTime;

    @PostConstruct
    protected void init(){
        log.info("[init] JwtTokenProvide 내 secretKey 초기화 시작");
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    public String createAccessToken(Long id, String email, String role){
        log.info("[createAccessToken] 엑세스 토큰 생성 시작");

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", id);
        claims.put("role", role);
        claims.put("type", "access");
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(secretKey)
                .compact();

        log.info("[createAccessToken] 엑세스 토큰 생성 완료");
        return token;
    }

    public String createRefreshToken(Long id, String email, String role){
        log.info("[createRefreshToken] 리프레시 토큰 생성 시작");

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", id);
        claims.put("role", role);
        claims.put("type", "refresh");
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey)
                .compact();

        log.info("[createAccessToken] 엑세스 토큰 생성 완료");
        return token;

    }

    // 필터에서 인증 성공 후, SecurityContextHolder 에 저장할 Authentication 을 생성
    //UsernamePasswordAuthenticationToken 클래스를 사용
    public Authentication getAuthentication(String token){
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails loginId : {}",userDetails.getUsername());

        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    //토큰 클래스를 사용하기 위해 초기화를 위한 UserDetails가 필요하다.
    //Jwts.parser()를 통해 secretKey를 설정하고 클레임을 추출해서 토큰을 생성할 때 넣었던 sub값을 추출합니다.
    public String getUsername(String token)
    {
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String email = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, loginId : {}",email);
        return email;
    }

    public Long getUserId(String token) {
        log.info("[getUserId] 토큰 기반 회원 구별 정보 추출");
        Long userId = Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId").toString());
        log.info("[getUserId] 토큰 기반 회원 구별 정보 추출 완료, userId : {}", userId);
        return userId;
    }


    public String getAuthorizationToken(HttpServletRequest request){
        log.info("[getAuthorizationToken] HTTP 헤더에서 Token 값 추출");
        String token = request.getHeader("Authorization");
        try{
            if(!token.substring(0,"BEARER ".length()).equalsIgnoreCase("Bearer ")){
                throw new IllegalStateException("Token 정보가 존재하지 않습니다.");
            }
            token = token.split(" ")[1].trim();
        }catch (Exception e){
            return null;
        }
        return token;
    }

    /*
        이 메소드는 토큰을 전달 받아 클레임의 유효기간을 체크하고 boolean 타입 값을 리턴하는 역할을 한다.
    */
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try{
            //복잡한 설정일 떈, Jwts.parserBuilder()를 이용
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (ExpiredJwtException ex){
            log.error("[validateToken] 토큰 만료됨: {}", ex.getMessage());
            throw ex;
        }
        catch (Exception e){
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
