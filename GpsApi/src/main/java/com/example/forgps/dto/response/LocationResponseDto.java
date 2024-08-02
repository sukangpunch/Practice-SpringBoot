package com.example.forgps.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class LocationResponseDto {

    private Long id;
    private String name;
    private double latitude;
    private double longitude;

}
