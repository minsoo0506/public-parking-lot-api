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
	// searchSpecifically 메소드 작성
	// 가까운 주차장 찾기
	// logger, swagger 적용

	public static void main(String[] args) {
		SpringApplication.run(ParkingLotApplication.class, args);
	}
}
