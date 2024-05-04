package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
}
