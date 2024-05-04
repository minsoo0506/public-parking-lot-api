package com.mnsoo.parkinglot.domain.persist;

import com.mnsoo.parkinglot.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "parking_operation")
public class ParkingOperationEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_code")
    private ParkingLotEntity parkingLot;

    @Column
    private String operationRule; // 운영 구분

    @Column
    private String queStatus; // 주차 현황 정보 제공 여부

    @Column
    private int capacity;// 총 주차면

    @Column
    private int curParking; // 현재 주차 차량수

    @Column
    private LocalDateTime curParkingTime; // 현재 주차 차량수 업데이트 시간

    @Column
    private String weekdayBeginTime; // 평일 운영 시작 시각(HHMM)

    @Column
    private String weekdayEndTime; // 평일 운영 종료 시각(HHMM)

    @Column
    private String weekendBeginTime; // 주말 운영 시작 시각(HHMM)

    @Column
    private String weekendEndTime; // 주말 운영 종료 시각(HHMM)

    @Column
    private String holidayBeginTime; // 공휴일 운영 시작 시각(HHMM)

    @Column
    private String holidayEndTime; // 공휴일 운영 종료 시각(HHMM)

    @Column
    private String saturdayPayYN; // 토요일 유,무료 구분

    @Column
    private String holidayPayYN; // 공휴일 유,무료 구분
}
