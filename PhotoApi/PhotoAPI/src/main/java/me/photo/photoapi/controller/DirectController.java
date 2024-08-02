package me.photo.photoapi.controller;

import lombok.AllArgsConstructor;
import me.photo.photoapi.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/direct")
@AllArgsConstructor
public class DirectController {

    private final StorageService storageService;

    //요청은 '/fileSystem' 엔드포인트로 전송되어야 하며, 요청 본문은 'multipart/form-data' 형식으로 전송되어야한다.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException{

        //이미지 파일을 실제로 저장하는 서비스 메서드 실행
        String uploadImage = storageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) throws IOException{
        // 요청된 파일명에 해당하는 이미지 파일을 가져온다.
        byte[] downloadImage = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                //응답의 컨텐츠 타입을 이미지/png 로 설정. 만약 이미지 파일의 타입이 png가 아니라면 타입을 맞춰줘야 한다.
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
