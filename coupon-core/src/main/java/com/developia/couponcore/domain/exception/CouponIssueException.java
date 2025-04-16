package com.developia.couponcore.domain.exception;

import lombok.Getter;

@Getter
public class CouponIssueException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CouponIssueException(ErrorCode errorCode, String message, boolean hasAppendCustomMessage) {
        this.errorCode = errorCode;
        if (hasAppendCustomMessage) {
            this.message = errorCode.message+ " : " + message;
        } else {
            this.message = message;
        }
    }

    public CouponIssueException(ErrorCode errorCode, String message) {
        this(errorCode, message, false);
    }

    public CouponIssueException(ErrorCode errorCode) {
       this(errorCode, errorCode.message, false);
    }




}
