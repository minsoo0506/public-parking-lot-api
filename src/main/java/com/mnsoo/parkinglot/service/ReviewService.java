package com.mnsoo.parkinglot.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mnsoo.parkinglot.domain.dto.ReviewDTO;
import com.mnsoo.parkinglot.domain.persist.Image;
import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.domain.persist.ReviewEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.ParkingLotNotFoundException;
import com.mnsoo.parkinglot.exception.impl.ReviewNotFoundException;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import com.mnsoo.parkinglot.repository.ReviewRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final AmazonS3Client amazonS3Client;

    private final ReviewRepository reviewRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ReviewService(AmazonS3Client amazonS3Client, ReviewRepository reviewRepository, ParkingLotRepository parkingLotRepository) {
        this.amazonS3Client = amazonS3Client;
        this.reviewRepository = reviewRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void processFiles(List<MultipartFile> files, ReviewEntity review) throws IOException {
        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            String filename = uuid + "_" + file.getOriginalFilename();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            amazonS3Client.putObject(
                    new PutObjectRequest("review-img-bucket", filename, file.getInputStream(), objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl = amazonS3Client.getUrl("review-img-bucket", filename).toExternalForm();

            Image image = Image.builder()
                    .filename(filename)
                    .filepath(fileUrl)
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
