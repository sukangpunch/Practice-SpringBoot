package me.foroauth2.common.service;

import lombok.RequiredArgsConstructor;
import me.foroauth2.user.domain.User;
import me.foroauth2.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return CustomUserDetails.from(user);
    }
}
