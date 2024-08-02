package com.example.forgps.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Builder
    public Location(String name, double latitude, double longitude)
    {
        this.name=name;
        this.latitude = latitude;
        this.longitude = longitude;
    }





}
