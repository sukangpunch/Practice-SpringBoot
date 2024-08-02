package com.example.redisproject.common.config;


import com.example.redisproject.common.security.exception.handler.JwtExceptionHandlerFilter;
import com.example.redisproject.common.security.exception.custom.CustomAccessDeniedHandler;
import com.example.redisproject.common.security.exception.custom.CustomAuthenticationEntryPoint;
import com.example.redisproject.common.security.jwt.JwtAuthFilter;
import com.example.redisproject.common.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
//Spring 기반의 웹 애플리케이션에서 보안 설정을 활성화하고 커스터마이즈하는 데 중요한 역할
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean // Spring IoC 컨테이너에 의해 관리되는 Bean 을 반환함을 나타냄
    // SecurityFilterChain 객체를 반환하며 Spring Security 의 설정을 구성
    // HttpSecurity 객체를 매개변수로 받아서 사용하며, 이를 통해 Spring Security 의 다양한 보안설정 적용
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity //filterChain 메서드의 매개변수로 전달되며, 이를 통해 다양한 메서드 체이닝을 통해 보안 구성을 정의
                // .httpBasic() 메서드는 Http Basic 인증을 활성화,
                // AbstractHttpConfigurer::disable는 Java 8의 메서드 참조를 사용하여 해당 설정을 비활성화합니다. 즉, Basic 인증을 사용하지 않도록 설정합니다.
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 기능을 비활성화 합니다. RestApi를 개발할 때 csrf 보호는 필요하지 않을 수 있습니다.
                .csrf(AbstractHttpConfigurer::disable)
                .cors(security ->{
                    security.configurationSource(corsConfigurationSource());
                })
                // 람다 표현식을 사용하여 세션 생성 정책 설정
                // STATLESS 는 세션을 사용하지 않고, 각 요청을 독립적으로 처리함을 나타낸다. Rest Api 에서 주로 사용된다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // HTTP 요청에 대한 인가(authorization) 설정을 정의
                // 'requests -> ... ' 람다 표현식 내에서 다양한 요청 패턴에 대한 접근 권한을 설정 
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/swagger-resources/**", "/swagger-ui/index.html", "/webjars/**", "/swagger/**", "/users/exception", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                .requestMatchers("/users/sign-in", "/users/sign-up","/users/reissue").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .anyRequest().hasRole("USER")
                )
                // addFilterBefore() 메서드는 특정 필터를 다른 필터 이전에 추가
                .addFilterBefore(new JwtExceptionHandlerFilter(), // 커스텀으로 구현된 jwt예외처리 필터를 추가
                        UsernamePasswordAuthenticationFilter.class) // 해당 필터를 추가할 위치를 지정합니다.
                .addFilterBefore(new JwtAuthFilter(jwtProvider), // jwt 토큰을 검증하고 사용자 인증을 처리하는 커스텀 필터
                        UsernamePasswordAuthenticationFilter.class) // 이 필터를 추가할 위치를 지정
                // 예외처리 설정을 정의
                .exceptionHandling(exceptionHandling ->  //람다 표현식 내에서 다양한 예외 처리 관련 설정을 구성
                        exceptionHandling
                                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 접근 거부 예외가 발생했을 때 처리할 커스텀 접근 거부 핸들러 설정
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())); // 인증 진입 지점에 예외가 발생했을 때 처리할 커스텀 인증 진입 지점 핸들러를 설정

        // httpSecurity.build() 는 Spring Security 의 설정이 완료된 후 그 설정을 적용할 수 있는,
        // SecurityFilterChain 객체를 생섷하여 반환하는 역할을 합니다.
        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
