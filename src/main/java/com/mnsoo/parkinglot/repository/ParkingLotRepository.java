package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    ParkingLotEntity findByParkingCode(Integer parkingCode);

    @Query("SELECT p FROM parking_lot p WHERE p.parkingName LIKE %:query%")
    List<ParkingLotEntity> findByParkingNameContaining(@Param("query") String query);
}
