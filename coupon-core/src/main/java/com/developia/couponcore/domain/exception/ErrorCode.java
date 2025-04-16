package com.developia.couponcore.domain.exception;

public enum ErrorCode {
    INVALID_ISSUE_QUANTITY("쿠폰 발급 수량이 유효하지 않습니다."),
    INVALID_ISSUE_DATE("쿠폰 발급 기간이 유효하지 않습니다."),
    NOT_FOUND("존재하지 않는 쿠폰입니다."),
    DUPLICATED_COUPON_ISSUE("이미 발급된 쿠폰입니다."),
    ;

    public final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
