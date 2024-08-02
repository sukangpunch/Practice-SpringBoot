package com.example.redisproject.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

//Spring Security 에서 사용되는 비밀번호 인코더를 빈으로 등록하는 설정 클래스
@Configuration
public class PasswordEncoderConfig {
    //PasswordEncoderFactories 팩트리 클래스의 creatDelegatingPasswordEncoder()메서드를 사용하여 여러 암호화 전략 중 하나를 선택하고,
    //생성된 'PasswordEncoder' 를 반환합니다. 기본적으로 Bcrypt 알고리즘을 사용합니다.
    @Bean
    public PasswordEncoder passwordEncoder(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
