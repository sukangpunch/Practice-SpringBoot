package com.example.forgps.service;

import com.example.forgps.domain.Location;
import com.example.forgps.dto.request.LocationRequestDto;
import com.example.forgps.dto.response.LocationResponseDto;
import com.example.forgps.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private static double setDistance = 50.0;
    
    public LocationResponseDto settingLocation(LocationRequestDto locationRequestDto) {
        Location location = Location.builder()
                .name(locationRequestDto.getName())
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .build();

        locationRepository.save(location);
        LocationResponseDto locationResponseDto = new LocationResponseDto(location.getId(), location.getName(), location.getLatitude(), location.getLongitude());


        return locationResponseDto;
    }

    public List<LocationResponseDto> calculateLocation(double lat, double Lon){
        List<Location> locations = new ArrayList<>();
        List<LocationResponseDto> locationResponseDtos = new ArrayList<>();
        locations = locationRepository.findAll();

        for (Location location : locations) {
            double distance = calculateDistance(lat, Lon, location.getLatitude(), location.getLongitude());
            if (distance <= setDistance) {
                // 거리가 distance threshold 내에 있는 위치를 결과 리스트에 추가
                locationResponseDtos.add(new LocationResponseDto(location.getId(),location.getName(),location.getLatitude(),location.getLongitude()));
            }
        }

        return locationResponseDtos;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }

    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

}
