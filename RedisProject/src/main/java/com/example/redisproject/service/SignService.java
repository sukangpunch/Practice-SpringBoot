package com.example.redisproject.service;


import com.example.redisproject.common.exception.custom.CustomException;
import com.example.redisproject.common.exception.custom.ExceptionCode;
import com.example.redisproject.common.redis.RefreshToken;
import com.example.redisproject.common.redis.RefreshTokenRepository;
import com.example.redisproject.common.security.jwt.JwtProvider;
import com.example.redisproject.data.domain.User;
import com.example.redisproject.data.dto.request.SignInReqDto;
import com.example.redisproject.data.dto.request.SignUpReqDto;
import com.example.redisproject.data.dto.response.SignInResDto;
import com.example.redisproject.data.dto.response.SignUpResDto;
import com.example.redisproject.data.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
// 생성자를 자동으로 생성해주는 기능
// 필수 필드를 가진 생성자 생성
// 클래스 내에서 final로 선언된 필드를 초기화하는 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
@Transactional
public class SignService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    public SignUpResDto signUp(SignUpReqDto signUpReqDto) {

        if (userRepository.findByEmail(signUpReqDto.email()).isPresent()) {
            throw new CustomException(ExceptionCode.LOGIN_ID_ALREADY_EXIST);
        }

        User user = User.builder()
                .name(signUpReqDto.name())
                .email(signUpReqDto.email())
                .password(passwordEncoder.encode(signUpReqDto.password()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        return SignUpResDto.toDto(user);
    }

    public SignInResDto signIn(SignInReqDto signInReqDto){
        User user = userRepository.findByEmail(signInReqDto.email()).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        if(!passwordEncoder.matches(signInReqDto.password(), user.getPassword())){
            throw new CustomException(ExceptionCode.PASSWORD_NOT_EQUAL);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(),user.getEmail(), user.getRole());

        //로그인 할 때마다 RefreshToken을 새로 생성한다.
        String refreshToken = jwtProvider.createRefreshToken(user.getId(),user.getEmail(),user.getRole());
        refreshTokenRepository.save(new RefreshToken(user.getId(),refreshToken));

        SignInResDto signInResDto = SignInResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return signInResDto;
    }

    public SignInResDto reissueAccessToken(HttpServletRequest request){

        String token = jwtProvider.getAuthorizationToken(request);
        log.info("token: {}",token);
        Long userId = jwtProvider.getUserId(token);

        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));

        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(()-> new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN));

        if(!refreshToken.getRefreshToken().equals(token)) {
            throw new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtProvider.createAccessToken(userId,user.getEmail(),user.getRole());
        String newRefreshToken = jwtProvider.createRefreshToken(userId,user.getEmail(),user.getRole());

        SignInResDto signInResDto = SignInResDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        refreshTokenRepository.save(new RefreshToken(userId, newRefreshToken));

        return signInResDto;
    }
}
