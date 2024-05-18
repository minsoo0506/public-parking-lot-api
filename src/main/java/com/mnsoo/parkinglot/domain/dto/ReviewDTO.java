package com.mnsoo.parkinglot.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewDTO {

    @JsonProperty("parking_code") // 주차장 코드
    private int parkingCode;

    @JsonProperty("comment") // 후기
    private String comment;

    @JsonProperty("rating")
    private double rating;
}
