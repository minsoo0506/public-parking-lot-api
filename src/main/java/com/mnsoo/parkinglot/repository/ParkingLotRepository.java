package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

    Optional<ParkingLotEntity> findByParkingCode(Integer parkingCode);

    @Query("SELECT p FROM parking_lot p WHERE p.parkingName LIKE %:query%")
    List<ParkingLotEntity> findByParkingNameContaining(@Param("query") String query);

    @Query("SELECT p FROM parking_lot p WHERE " +
            "ST_Distance_Sphere(point(p.lng, p.lat), point(:userLng, :userLat)) <= :radius")
    Page<ParkingLotEntity> findNearbyParkingLots(@Param("userLat") double userLat,
                                                 @Param("userLng") double userLng,
                                                 @Param("radius") int radius,
                                                 Pageable pageable);
}
