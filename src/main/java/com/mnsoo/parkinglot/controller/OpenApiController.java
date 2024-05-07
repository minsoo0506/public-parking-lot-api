package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenApiController {
    private final OpenApiService openApiService;

    @GetMapping("/get/data")
    void getParkingLotData(){
        openApiService.autoSaveParkingLotData();
    }
}
