package com.mnsoo.parkinglot.domain.persist;

import com.mnsoo.parkinglot.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "parking_fee")
public class ParkingFeeEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_code")
    private ParkingLotEntity parkingLot;

    @Column
    private Integer fulltimeMonthly; // 월 정기권 금액

    @Column
    private int rates; // 기본 주차 요금

    @Column
    private int timeRate; // 기본 주차 시간

    @Column
    private int addRates; // 추가 단위 요금

    @Column
    private int addTimeRate; // 추가 단위 시간

    @Column
    private int dayMaximum; // 일 최대 요금
}
