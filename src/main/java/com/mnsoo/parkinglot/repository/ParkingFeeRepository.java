package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingFeeRepository extends JpaRepository<ParkingFeeEntity, Long> {
}
