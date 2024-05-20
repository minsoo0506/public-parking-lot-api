package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.domain.persist.ReviewEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    boolean existsByUserAndParkingLot(UserEntity user, ParkingLotEntity parkingLot);

    Optional<ReviewEntity> findByUserAndParkingLot(UserEntity user, ParkingLotEntity parkingLot);

    List<ReviewEntity> findByUser(UserEntity user);
}
