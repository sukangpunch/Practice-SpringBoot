package querydsl.querydslapi.user.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import querydsl.querydslapi.album.domain.Album;
import querydsl.querydslapi.user.domain.User;
import querydsl.querydslapi.user.dto.response.QueryDslDto;
import querydsl.querydslapi.user.repository.UserCustomRepository;

import java.util.*;

import static querydsl.querydslapi.album.domain.QAlbum.album;
import static querydsl.querydslapi.profile.domain.QProfile.profile;
import static querydsl.querydslapi.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public QueryDslDto getQueryInfo(User reqUser) {

        List<Tuple> result = jpaQueryFactory.select(user.name, profile.id, album.id)
                .from(user)
                .join(profile).on(user.profile.id.eq(profile.id))
                .rightJoin(album).on(album.user.id.eq(user.id))
                .where(user.id.eq(reqUser.getId()))
                .fetch();

        Map<String, List<Long>> userAndAlbums = new HashMap<>();

        result.forEach(tuple -> {
            String userName = tuple.get(user.name);
            Long albumId = tuple.get(album.id);

            userAndAlbums.computeIfAbsent(
                    userName,
                    key -> new ArrayList<>()
            ).add(albumId);
        });

        Tuple firstTuple = result.get(0);
        String userName = firstTuple.get(user.name);
        Long profileId = firstTuple.get(profile.id);


        return QueryDslDto.builder()
                .userName(userName)
                .profileId(profileId)
                .albumIdList(userAndAlbums.get(userName))
                .build();
    }
}
