package com.developia.couponcore.domain.exception;

import lombok.Getter;

@Getter
public class CouponNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CouponNotFoundException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public CouponNotFoundException(ErrorCode errorCode) {
        this(errorCode, errorCode.message);
    }
}
