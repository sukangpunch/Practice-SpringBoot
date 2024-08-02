package com.example.redisproject.common.security.service;


import com.example.redisproject.common.exception.custom.CustomException;
import com.example.redisproject.common.exception.custom.ExceptionCode;
import com.example.redisproject.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor

// UserDetailsService 인터페이스를 구현하여 loadUserByUsername 메서드를 오버라이드합니다.
// 이 메서드는 Spring Security 에서 인증 시 사용자 정보를 불러오는 역할을 합니다.
// 주어진 username 을 기반으로 사용자를 찾고, UserDetails 객체를 반환합니다.
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // loadUserByUsername 메서드는 주어진 username 을 사용하여 데이터베이스(userRepository)에서 사용자 정보를 조회합니다.
    // 조회된 사용자 정보를 기반으로 Spring Security 의 UserDetails 객체를 생성하여 반환합니다.
    // UserDetails 객체는 사용자의 인증과 관련된 정보를 담고 있으며, UserDetailsServiceImpl 클래스는 이를 구현하여 사용자 로그인 인증 과정에서 필요한 정보를 제공합니다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] loadUserByUsername 수행. email: {}", email);
        return (UserDetails) userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
    }
}
