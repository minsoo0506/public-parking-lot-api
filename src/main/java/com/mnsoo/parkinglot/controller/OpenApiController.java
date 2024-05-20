package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.service.OpenApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"주차장 정보를 제공하는 Controller"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenApiController {

    private final OpenApiService openApiService;

    @ApiOperation(value = "관리자 권한으로 주차장 테이블을 최신화 합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/manually")
    ResponseEntity<?> updateManually(){
        openApiService.autoSaveParkingLotData();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value = "키워드와 관련된 주차장 리스트를 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query", value = "검색할 키워드", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/search")
    ResponseEntity<?> searchParkingLots(@RequestParam String query){
        var result = openApiService.searchParkingLots(query);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "주차장 코드로 특정 주차장의 상세 정보를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "조회할 주차장의 코드", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping("/search/specific/{code}")
    ResponseEntity<?> searchSpecifically(@PathVariable String code){
        var result = openApiService.searchSpecifically(code);

        if(result == null){
            return ResponseEntity.ok("No data found for parking code: " + code);
        }

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 위치로부터 가까운 주차장 리스트를 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "페이지 크기", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userLat", value = "사용자의 위도", dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "userLng", value = "사용자의 경도", dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "radius", value = "검색 반경(미터)", dataType = "int", paramType = "query")
    })
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
