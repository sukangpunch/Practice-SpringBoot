package me.forsse2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import me.forsse2.common.exception.customs.CustomException;
import me.forsse2.common.exception.customs.ExceptionCode;
import me.forsse2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] loadUserByUsername 수행. username: {}", loginId);
        return (UserDetails) userRepository.findByLoginId(loginId).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
    }
}
