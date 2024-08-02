package me.photo.photoapi.repository;

import me.photo.photoapi.domain.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImgRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);

}
