package querydsl.querydslapi.album.dto.response;

import lombok.Builder;

@Builder
public record AlbumResponseDto(
        String name,
        String userName
){}
