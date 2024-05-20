package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.dto.ReviewDTO;
import com.mnsoo.parkinglot.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = {"리뷰 정보를 다루는 Controller"})
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰를 등록합니다.", consumes = "multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    // @ModelAttribute : 일반 HTTP 요청 파라미터나 multipart/form-data 형태의 파라미터를 받아 객체로 사용하고 싶을 때 이용
    // @ModelAttribute 는 "가장 적절한" 생성자를 찾아 객체를 생성 및 초기화 함.
    public ResponseEntity<?> writeReview(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("files") List<MultipartFile> files) {
        try {
            reviewService.createReview(reviewDTO, files);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "특정 주차장에 대해 작성한 리뷰를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "parkingCode", value = "조회할 주차장의 코드", required = true, dataType = "int", paramType = "query")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/read/specific")
    public ResponseEntity<?> getReviewByUserAndParkingLot(@RequestParam int parkingCode) {
        var review = reviewService.getSpecificParkingLotReview(parkingCode);
        return ResponseEntity.ok(review);
    }

    @ApiOperation(value = "회원이 작성한 리뷰 전체를 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/read/all")
    public ResponseEntity<?> getAllReviewsByUser() {
        var reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation(value = "특정 주차장에 대한 리뷰를 업데이트 합니다.", consumes = "multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("files") List<MultipartFile> files) throws IOException {
        var result  = this.reviewService.updateReview(reviewDTO, files);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "회원이 작성한 리뷰를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "parkingCode", value = "삭제할 리뷰의 주차장의 코드", required = true, dataType = "int", paramType = "query")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam int parkingCode){
        this.reviewService.deleteReview(parkingCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
