package com.developia.couponcore.domain;

import com.developia.couponcore.domain.exception.CouponIssueException;
import com.developia.couponcore.domain.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupons")
public class Coupon extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    @Column(nullable = false)
    private int issuedQuantity;

    @Column(nullable = false)
    private Integer discountAmount;

    @Column(nullable = false)
    private Integer minAvailableAmount;

    @Column(nullable = false)
    private Integer maxAvailableAmount;

    @Column(nullable = false)
    private LocalDateTime issueStartedAt;

    @Column(nullable = false)
    private LocalDateTime issueEndedAt;

    public boolean isIssueQuantityAvailable() {
        if (totalQuantity == null) {
            // 발급에 제한이 없는 경우
            return false;
        }
        return totalQuantity > issuedQuantity;
    }

    public boolean isIssueDateAvailable(LocalDateTime date) {
        return issueStartedAt.isBefore(date) && issueEndedAt.isAfter(date);
    }

    public void issue(LocalDateTime now) {
        if (!isIssueDateAvailable(now)) {
            throw new CouponIssueException(ErrorCode.INVALID_ISSUE_DATE,
                    "발급 가능한 일자가 아닙니다. 발급 기간: %s ~ %s".formatted(issueStartedAt, issueEndedAt));
        }

        if (!isIssueQuantityAvailable()) {
            throw new CouponIssueException(ErrorCode.INVALID_ISSUE_QUANTITY,
                    "발급 가능한 수량을 초과합니다. 전체 : %s / 발행: %s".formatted(totalQuantity, issuedQuantity));
        }

        issuedQuantity++;
    }
}
