package me.forsse2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.forsse2.common.exception.customs.CustomException;
import me.forsse2.common.exception.customs.ExceptionCode;
import me.forsse2.common.jwt.JwtProvider;
import me.forsse2.dto.request.SignInReqDto;
import me.forsse2.dto.request.SignUpReqDto;
import me.forsse2.dto.response.SignInResDto;
import me.forsse2.dto.response.SignUpResDto;
import me.forsse2.entity.User;
import me.forsse2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    public SignUpResDto signUp(SignUpReqDto userReqDto) {

        User user = User.builder()
                .name(userReqDto.name())
                .loginId(userReqDto.loginId())
                .password(passwordEncoder.encode(userReqDto.password()))
                .email(userReqDto.email())
                //단일 권한을 가진 리스트 생성, 하나의 요소를 가진 불변의 리스트 생성
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        return SignUpResDto.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public SignInResDto signIn(SignInReqDto signInReqDto) {
        User user = userRepository.findByLoginId(signInReqDto.loginId()).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInReqDto.password(), user.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_NOT_EQUAL);
        }
        String accessToken = jwtProvider.createAccessToken(user.getLoginId(), user.getRoles());

        SignInResDto signInResponseDto = SignInResDto.builder()
                .accessToken(accessToken)
                .build();

        return signInResponseDto;
    }


    public void deleteUser(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        userRepository.deleteById(user.getId());
    }
}

