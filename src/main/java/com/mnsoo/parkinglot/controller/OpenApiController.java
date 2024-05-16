package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenApiController {
    private final OpenApiService openApiService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/manually")
    ResponseEntity<?> updateManually(){
        openApiService.autoSaveParkingLotData();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<?> searchParkingLots(@RequestParam String query){
        var result = openApiService.searchParkingLots(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/specific/{code}")
    ResponseEntity<?> searchSpecifically(@PathVariable String code){
        var result = openApiService.searchSpecifically(code);

        if(result == null){
            return ResponseEntity.ok("No data found for parking code: " + code);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/nearby")
    ResponseEntity<?> searchNearbyParkingLots(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // 사용자의 default 위치는 강남역
            @RequestParam(defaultValue = "37.498071922037155") double userLat,
            @RequestParam(defaultValue = "127.02799461568996") double userLng,
            // 검색하고자 하는 default 반경은 5km
            @RequestParam(defaultValue = "5000") int radius
    ){
        var result = openApiService.searchNearbyParkingLots(page, size, userLat, userLng, radius);
        return ResponseEntity.ok(result);
    }
}
