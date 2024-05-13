package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenApiController {
    private final OpenApiService openApiService;

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
            @RequestParam(defaultValue = "37.498071922037155") Double userLat,
            @RequestParam(defaultValue = "127.02799461568996") Double userLng,
            @RequestParam(defaultValue = "5.0") Double radius
    ){
        var result = openApiService.searchNearbyParkingLots(userLat, userLng, radius);
        return ResponseEntity.ok(result);
    }
}
