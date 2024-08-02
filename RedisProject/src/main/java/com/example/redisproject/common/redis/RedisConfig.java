package com.example.redisproject.common.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // RedisStandaloneConfiguration 클래스는 Redis 서버의 설정을 정의
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        // Lettuce는 비동기 Redis 클라이언트 라이브러리, Redis서버와의 연결 관리
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate은 Redis와 상호 작용 하기 위한 고수준 추상화 제공
        // String은 키 , Object는 값
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // redisConnectionFactory() 메서드를 호출하여 생성된 RedisConnectionFactory를 RedisTemplate에 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        // Redis의 키를 직렬화하기 위해 StringRedisSerializer를 사용합니다. 이 직렬화기는 키를 문자열로 변환하여 Redis에 저장
        redisTemplate.setKeySerializer(new StringRedisSerializer());

//        // ObjectMapper 는 객체를 JSON으로 직렬화하거나 JSON을 객체로 역직렬화 한다
//        ObjectMapper mapper = new ObjectMapper();
//        // JavaTimeModule을 등록하여 ObjectMapper가 시간 API를 처리할 수 있도록 한다.
//        mapper.registerModule(new JavaTimeModule());
//        // Jackson2JsonRedisSerializer는 값들을 JSON으로 직렬화하기 위해 Jackson을 사용, Object 타입의 값을 처리
//        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);
//
//        // RedisTemplate의 값을 직렬화하기 위해 Jackson2JsonRedisSerializer를 사용, 이를 통해 객체가 JSON 형식으로 Redis에 저장
//        redisTemplate.setValueSerializer(valueSerializer);

        return redisTemplate;
    }
}
