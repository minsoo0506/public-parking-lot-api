package com.mnsoo.parkinglot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

// 서울시 공영 주차장 자리 확인 서비스
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ParkingLotApplication {
	// 토
	// 리뷰 기능 추가

	// 일
	// logger, swagger 적용

	public static void main(String[] args) {
		SpringApplication.run(ParkingLotApplication.class, args);
	}
}
