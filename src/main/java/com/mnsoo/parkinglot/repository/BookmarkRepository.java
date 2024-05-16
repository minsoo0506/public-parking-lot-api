package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.BookmarkEntity;
import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    boolean existsByUserAndParkingLot(UserEntity user, ParkingLotEntity parkingLot);

    List<BookmarkEntity> findByUser_LoginId(String loginId);

    BookmarkEntity findByUserAndParkingLot(UserEntity user, ParkingLotEntity parkingLot);
}
