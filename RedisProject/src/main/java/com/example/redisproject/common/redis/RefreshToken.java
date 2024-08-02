package com.example.redisproject.common.redis;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 30)
public class RefreshToken {

    private String refreshToken;

    //userId에 해당한다
    @Id
    @Indexed
    private Long id;

    public RefreshToken(Long id, String refreshToken){
        this.refreshToken = refreshToken;
        this.id = id;
    }
}
