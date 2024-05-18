package com.mnsoo.parkinglot.exception.impl;

import com.mnsoo.parkinglot.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistReviewException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "이미 해당 주차장에 대해 작성한 리뷰가 있습니다.";
    }
}
