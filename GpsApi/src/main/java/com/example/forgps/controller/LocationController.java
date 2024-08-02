package com.example.forgps.controller;


import com.example.forgps.dto.request.LocationRequestDto;
import com.example.forgps.dto.response.LocationResponseDto;
import com.example.forgps.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping()
    public ResponseEntity<LocationResponseDto> createLocation(@RequestBody LocationRequestDto locationRequestDto)
    {
        return ResponseEntity.status(HttpStatus.OK).body(locationService.settingLocation(locationRequestDto));
    }

    @GetMapping()
    public ResponseEntity<List<LocationResponseDto>> calculateLocation(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude)
    {
        return ResponseEntity.status(HttpStatus.OK).body(locationService.calculateLocation(latitude,longitude));
    }



}
