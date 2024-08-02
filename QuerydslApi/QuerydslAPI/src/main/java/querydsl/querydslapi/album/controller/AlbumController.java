package querydsl.querydslapi.album.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import querydsl.querydslapi.album.dto.response.AlbumResponseDto;
import querydsl.querydslapi.album.service.AlbumService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping()
    public ResponseEntity<AlbumResponseDto> createAlbum(@RequestParam Long userId, @RequestParam String name){

        AlbumResponseDto albumResponseDto = albumService.createAlbum(userId,name);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumResponseDto);
    }
}
