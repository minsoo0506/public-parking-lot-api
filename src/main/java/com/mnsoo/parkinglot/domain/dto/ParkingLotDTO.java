package com.mnsoo.parkinglot.domain.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ParkingLotDTO {
    @JsonProperty("PARKING_CODE") // 주차장 코드
    private int parkingCode;

    @JsonProperty("PARKING_NAME") // 주차장 명
    private String parkingName;

    @JsonProperty("ADDR") // 주소
    private String address;

    @JsonProperty("TEL") // 전화번호
    private String tel;

    @JsonProperty("LAT") // 위도
    private double lat;

    @JsonProperty("LNG") // 경도
    private double lng;

    @JsonProperty("PARKING_TYPE") // 주차장 타입
    private String parkingType;

    @JsonProperty("OPERATION_RULE") // 운영 규칙
    private String operationRule;

    @JsonProperty("QUE_STATUS") // 대기 상태
    private String queStatus;

    @JsonProperty("CAPACITY") // 용량
    private int capacity;

    @JsonProperty("CUR_PARKING") // 현재 주차
    private int curParking;

    @JsonProperty("CUR_PARKING_TIME") // 현재 주차 시간
    private String curParkingTime;

    @JsonProperty("PAY_YN") // 유료 여부
    private String payYn;

    @JsonProperty("NIGHT_FREE_OPEN") // 야간 무료 여부
    private String nightFreeOpen;

    @JsonProperty("WEEKDAY_BEGIN_TIME") // 평일 시작 시간
    private String weekdayBeginTime;

    @JsonProperty("WEEKDAY_END_TIME") // 평일 종료 시간
    private String weekdayEndTime;

    @JsonProperty("WEEKEND_BEGIN_TIME") // 주말 시작 시간
    private String weekendBeginTime;

    @JsonProperty("WEEKEND_END_TIME") // 주말 종료 시간
    private String weekendEndTime;

    @JsonProperty("HOLIDAY_BEGIN_TIME") // 공휴일 시작 시간
    private String holidayBeginTime;

    @JsonProperty("HOLIDAY_END_TIME") // 공휴일 종료 시간
    private String holidayEndTime;

    @JsonProperty("SATURDAY_PAY_YN") // 토요일 유료 여부
    private String saturdayPayYn;

    @JsonProperty("HOLIDAY_PAY_YN") // 공휴일 유료 여부
    private String holidayPayYn;

    @JsonProperty("RATES") // 요금
    private int rates;

    @JsonProperty("TIME_RATE") // 시간당 요금
    private int timeRate;

    @JsonProperty("ADD_RATES") // 추가 요금
    private int addRates;

    @JsonProperty("ADD_TIME_RATE") // 추가 시간당 요금
    private int addTimeRate;

    @JsonProperty("DAY_MAXIMUM") // 일 최대 요금
    private int dayMaximum;
}