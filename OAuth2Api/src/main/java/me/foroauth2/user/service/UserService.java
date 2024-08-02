package me.foroauth2.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.foroauth2.exception.customs.CustomException;
import me.foroauth2.exception.customs.ExceptionCode;
import me.foroauth2.jwt.JwtProvider;
import me.foroauth2.user.domain.User;
import me.foroauth2.user.dto.res.UserResDto;
import me.foroauth2.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResDto getUserInfo(Long userId) {
        log.info("[getMemberInfo] 유저의 정보 조회");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        return UserResDto.toDto(user);
    }

    public void deleteUser(Long id){
        log.info("[deleteUser] 유저 삭제");

        userRepository.deleteById(id);
    }

}
