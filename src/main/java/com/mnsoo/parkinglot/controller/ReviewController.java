package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.dto.ReviewDTO;
import com.mnsoo.parkinglot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    public ResponseEntity<?> writeReview(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("files") List<MultipartFile> files) {
        try {
            reviewService.createReview(reviewDTO, files);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/read/specific")
    public ResponseEntity<?> getReviewByUserAndParkingLot(@RequestParam int parkingCode) {
        var review = reviewService.getSpecificParkingLotReview(parkingCode);
        return ResponseEntity.ok(review);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/read/all")
    public ResponseEntity<?> getAllReviewsByUser() {
        var reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("files") List<MultipartFile> files) throws IOException {
        var result  = this.reviewService.updateReview(reviewDTO, files);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam int parkingCode){
        this.reviewService.deleteReview(parkingCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
