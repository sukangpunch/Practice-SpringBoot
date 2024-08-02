package querydsl.querydslapi.album.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydslapi.album.domain.Album;
import querydsl.querydslapi.album.dto.response.AlbumResponseDto;
import querydsl.querydslapi.album.repository.AlbumRepository;
import querydsl.querydslapi.user.domain.User;
import querydsl.querydslapi.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private  final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    @Transactional
    public AlbumResponseDto createAlbum(Long userId, String name){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException());

        Album album = Album.builder()
                .albumName(name)
                .user(user)
                .build();

        albumRepository.save(album);

        return AlbumResponseDto.builder()
                .name(album.getAlbumName())
                .userName(user.getName())
                .build();
    }

}
