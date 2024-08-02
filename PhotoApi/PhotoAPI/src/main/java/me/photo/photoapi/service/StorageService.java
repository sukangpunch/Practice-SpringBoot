package me.photo.photoapi.service;


import lombok.RequiredArgsConstructor;
import me.photo.photoapi.domain.FileData;
import me.photo.photoapi.domain.ImageData;
import me.photo.photoapi.repository.FileDataRepository;
import me.photo.photoapi.repository.ImgRepository;
import me.photo.photoapi.utils.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final ImgRepository imgRepository;
    private final FileDataRepository fileDataRepository;


    //이미지를 데이터베이스에 저장
    public String uploadImage(MultipartFile file) throws IOException{
        imgRepository.save( //엔티티를 데이터베이스에 저장
                ImageData.builder()
                        .name(file.getOriginalFilename()) //파일의 원본 파일명
                        .type(file.getContentType()) //파일의 컨텐츠 타입
                        .imageData(ImageUtils.compressImage(file.getBytes())) // 파일의 데이터를 바이트 배열로 가져옴
                        .build());

        return "file upload successfully : "+file.getOriginalFilename();

    }

    public byte[] downloadImage(String fileName){
        ImageData imageData = imgRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        //압축된 이미지 데이터를 받아 압축을 해제하고, 압축 해제 된 이미지 데이터를 바이트 배열로 반환
        return ImageUtils.decompressImage(imageData.getImageData());
    }

    //업로드된 파일을 파일 시스템에 저장하는 메서드
    public String uploadImageToFileSystem(MultipartFile file) throws IOException{
        
        //파일을 저장할 폴더 경로
        String FOLDER_PATH = "C:\\SnapTimeProject\\images\\";
        //저장할 파일 전체 경로
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        //name은 업로드된 파일의 원본 파일명, type은 파일의 컨텐트 타입, filePath는 파일의 전체 경로
        FileData fileData = fileDataRepository.save(
                FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .build());

        //업로드된 파일을 지정된 파일 경로로 파일 시스템에 저장. MultipartFile의 transferTo() 메서드 사용
        file.transferTo(new File(filePath));

        return "파일이 성공적으로 업로드 됨; 파일 경로: " + filePath;
    }

    //파일 시스템에서 특정 파일명에 해당하는 파일을 읽어와서 해당 파일의 데이터를 바이트 배열로 반환하는 기능을 수행
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException{
        //데이터베이스에서 FileData 객체 조회
        FileData fileData = fileDataRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        //FileData객체에서 파일의 경로를 가져온다.
        String filePath = fileData.getFilePath();

        //파일 시스템에서 해당 경로의 파일을 읽어 와서 바이트 배열로 변환하여 반환.
        return Files.readAllBytes(new File(filePath).toPath());
    }


}
