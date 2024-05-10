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
        return ResponseEntity.ok(result);
    }
}
