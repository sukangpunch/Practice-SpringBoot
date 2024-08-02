package querydsl.querydslapi.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydslapi.profile.domain.Profile;
import querydsl.querydslapi.profile.repository.ProfileRepository;
import querydsl.querydslapi.user.domain.User;
import querydsl.querydslapi.user.dto.response.QueryDslDto;
import querydsl.querydslapi.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public QueryDslDto getQueryDsl(Long userId){
        User reqUser = userRepository.findById(userId).orElseThrow(()->new RuntimeException());

        QueryDslDto queryDslDto =  userRepository.getQueryInfo(reqUser);

        return queryDslDto;

    }

    @Transactional
    public String createUser(String name)
    {
        Profile profile = Profile.builder()
                .profileName(String.valueOf(LocalDate.now()))
                .profilePath("/" + String.valueOf(LocalDate.now()))
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .name("name")
                .profile(profile)
                .build();

        userRepository.save(user);

        return "성공!";
    }
}
