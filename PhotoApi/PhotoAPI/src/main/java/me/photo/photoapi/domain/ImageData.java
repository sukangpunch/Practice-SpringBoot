package me.photo.photoapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ImageData")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    //mySQL은 BLOB은 64KB 밖에 안되기 때문에, MEDIUMBLOB으로 지정해줘야한다.
    @Column(name = "imagedata",columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    @Builder
    public ImageData(String name, String type, byte[] imageData){
        this.name =name;
        this.type = type;
        this.imageData = imageData;
    }


}
