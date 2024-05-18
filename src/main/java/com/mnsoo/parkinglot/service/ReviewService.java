package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.dto.ReviewDTO;
import com.mnsoo.parkinglot.domain.persist.Image;
import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.domain.persist.ReviewEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.ParkingLotNotFoundException;
import com.mnsoo.parkinglot.exception.impl.ReviewNotFoundException;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import com.mnsoo.parkinglot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    @Value("${image.path}")
    private String path;

    private final ReviewRepository reviewRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ReviewService(ReviewRepository reviewRepository, ParkingLotRepository parkingLotRepository) {
        this.reviewRepository = reviewRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void processFiles(List<MultipartFile> files, ReviewEntity review) throws IOException {
        String imagePath = System.getProperty("user.dir") + path;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            String filename = uuid + "_" + file.getOriginalFilename();
            File dest = new File(imagePath + filename);
            file.transferTo(dest);

            Image image = Image.builder()
                    .filename(filename)
                    .filepath("/images/" + filename)
                    .review(review)
                    .build();

            review.getImages().add(image);
        }
    }

    public void createReview(ReviewDTO reviewDTO, List<MultipartFile> files) throws IOException {
        UserEntity user = getCurrentUser();
        ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(reviewDTO.getParkingCode())
                .orElseThrow(ParkingLotNotFoundException::new);

        ReviewEntity review = ReviewEntity.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .user(user)
                .parkingLot(parkingLot)
                .images(new ArrayList<>())
                .build();

        processFiles(files, review);

        reviewRepository.save(review);
    }

    public ReviewEntity getSpecificParkingLotReview(int parkingCode) {
        UserEntity user = getCurrentUser();
        ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(parkingCode)
                .orElseThrow(ParkingLotNotFoundException::new);

        return reviewRepository.findByUserAndParkingLot(user, parkingLot)
                .orElseThrow(ReviewNotFoundException::new);
    }

    public List<ReviewEntity> getAllReviews() {
        UserEntity user = getCurrentUser();
        return reviewRepository.findByUser(user);
    }

    public ReviewEntity updateReview(ReviewDTO reviewDTO, List<MultipartFile> files) throws IOException {
        UserEntity user = getCurrentUser();
        ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(reviewDTO.getParkingCode())
                .orElseThrow(ParkingLotNotFoundException::new);

        ReviewEntity review = reviewRepository.findByUserAndParkingLot(user, parkingLot)
                .orElseThrow(ReviewNotFoundException::new);

        review.getImages().clear();

        processFiles(files, review);

        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());

        return reviewRepository.save(review);
    }

    public void deleteReview(int parkingCode) {
        UserEntity user = getCurrentUser();
        ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(parkingCode)
                .orElseThrow(ParkingLotNotFoundException::new);

        ReviewEntity review = reviewRepository.findByUserAndParkingLot(user, parkingLot)
                .orElseThrow(ReviewNotFoundException::new);

        reviewRepository.delete(review);
    }
}
