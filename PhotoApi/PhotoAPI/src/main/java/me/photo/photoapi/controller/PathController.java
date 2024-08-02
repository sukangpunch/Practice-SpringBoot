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
@RequestMapping("/path")
@AllArgsConstructor
public class PathController {

    private final StorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/fileSystem")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImageToFileSystem(@PathVariable("fileName") String fileName) throws IOException{
        byte[] downloadImage = storageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
