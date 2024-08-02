package querydsl.querydslapi.user.dto.response;

import lombok.Builder;
import querydsl.querydslapi.album.domain.Album;

import java.util.List;

@Builder
public record QueryDslDto(
        String userName,
        Long profileId,
        List<Long> albumIdList
){}
