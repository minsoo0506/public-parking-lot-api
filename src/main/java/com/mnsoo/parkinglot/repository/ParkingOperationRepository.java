package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingOperationRepository extends JpaRepository<ParkingOperationEntity, Long> {
}
