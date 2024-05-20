package com.mnsoo.parkinglot.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ParkingLotDTO {

    @ApiModelProperty(value = "주차장 코드", example = "171721")
    @JsonProperty("PARKING_CODE") // 주차장 코드
    private int parkingCode;

    @ApiModelProperty(value = "주차장 명", example = "세종로 공영주차장(시)")
    @JsonProperty("PARKING_NAME") // 주차장 명
    private String parkingName;

    @ApiModelProperty(value = "주소", example = "종로구 세종로 80-1")
    @JsonProperty("ADDR") // 주소
    private String address;

    @ApiModelProperty(value = "전화번호", example = "02-2290-6566")
    @JsonProperty("TEL") // 전화번호
    private String tel;

    @ApiModelProperty(value = "위도", example = "37.57340269")
    @JsonProperty("LAT") // 위도
    private double lat;

    @ApiModelProperty(value = "경도", example = "126.97588429")
    @JsonProperty("LNG") // 경도
    private double lng;

    @ApiModelProperty(value = "주차장 타입", example = "NW")
    @JsonProperty("PARKING_TYPE") // 주차장 타입
    private String parkingType;

    @ApiModelProperty(value = "운영 규칙", example = "1")
    @JsonProperty("OPERATION_RULE") // 운영 규칙
    private String operationRule;

    @ApiModelProperty(value = "대기 상태", example = "1")
    @JsonProperty("QUE_STATUS") // 대기 상태
    private String queStatus;

    @ApiModelProperty(value = "전체 주차면 수", example = "1260")
    @JsonProperty("CAPACITY") // 전체 주차면 수
    private int capacity;

    @ApiModelProperty(value = "현재 주차된 차량 수", example = "71")
    @JsonProperty("CUR_PARKING") // 현재 주차된 차량 수
    private int curParking;

    @ApiModelProperty(value = "실시간 주차장 정보 업데이트 시각", example = "2024-05-20 02:21:26")
    @JsonProperty("CUR_PARKING_TIME") // 실시간 주차장 정보 업데이트 시각
    private String curParkingTime;

    @ApiModelProperty(value = "유료 여부", example = "Y")
    @JsonProperty("PAY_YN") // 유료 여부
    private String payYn;

    @ApiModelProperty(value = "야간 무료 여부", example = "N")
    @JsonProperty("NIGHT_FREE_OPEN") // 야간 무료 여부
    private String nightFreeOpen;

    @ApiModelProperty(value = "평일 시작 시간", example = "0000")
    @JsonProperty("WEEKDAY_BEGIN_TIME") // 평일 시작 시간
    private String weekdayBeginTime;

    @ApiModelProperty(value = "평일 종료 시간", example = "2400")
    @JsonProperty("WEEKDAY_END_TIME") // 평일 종료 시간
    private String weekdayEndTime;

    @ApiModelProperty(value = "주말 시작 시간", example = "0000")
    @JsonProperty("WEEKEND_BEGIN_TIME") // 주말 시작 시간
    private String weekendBeginTime;

    @ApiModelProperty(value = "주말 종료 시간", example = "2400")
    @JsonProperty("WEEKEND_END_TIME") // 주말 종료 시간
    private String weekendEndTime;

    @ApiModelProperty(value = "공휴일 시작 시간", example = "0000")
    @JsonProperty("HOLIDAY_BEGIN_TIME") // 공휴일 시작 시간
    private String holidayBeginTime;

    @ApiModelProperty(value = "공휴일 종료 시간", example = "2400")
    @JsonProperty("HOLIDAY_END_TIME") // 공휴일 종료 시간
    private String holidayEndTime;

    @ApiModelProperty(value = "토요일 유료 여부", example = "N")
    @JsonProperty("SATURDAY_PAY_YN") // 토요일 유료 여부
    private String saturdayPayYn;

    @ApiModelProperty(value = "공휴일 유료 여부", example = "N")
    @JsonProperty("HOLIDAY_PAY_YN") // 공휴일 유료 여부
    private String holidayPayYn;

    @ApiModelProperty(value = "요금", example = "430")
    @JsonProperty("RATES") // 요금
    private int rates;

    @ApiModelProperty(value = "시간당 요금", example = "5")
    @JsonProperty("TIME_RATE") // 시간당 요금
    private int timeRate;

    @ApiModelProperty(value = "추가 요금", example = "430")
    @JsonProperty("ADD_RATES") // 추가 요금
    private int addRates;

    @ApiModelProperty(value = "추가 시간당 요금", example = "5")
    @JsonProperty("ADD_TIME_RATE") // 추가 시간당 요금
    private int addTimeRate;

    @ApiModelProperty(value = "일 최대 요금", example = "30900")
    @JsonProperty("DAY_MAXIMUM") // 일 최대 요금
    private int dayMaximum;
}