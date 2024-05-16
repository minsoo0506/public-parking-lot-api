package com.mnsoo.parkinglot.domain.persist;
import com.mnsoo.parkinglot.domain.BaseEntity;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "parking_lot")
public class ParkingLotEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int parkingCode; // 주차장 코드

    @Column(nullable = false)
    private String parkingName; // 주차장 명

    @Column
    private String address; // 주소

    @Column
    private String tel; // 전화번호

    @Column
    private double lat; // 위도

    @Column
    private double lng; // 경도
}
