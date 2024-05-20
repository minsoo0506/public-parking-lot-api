package com.mnsoo.parkinglot.exception.impl;

import com.mnsoo.parkinglot.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NO_CONTENT.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 리뷰 입니다.";
    }
}
