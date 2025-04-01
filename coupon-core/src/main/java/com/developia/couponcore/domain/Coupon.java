package com.developia.couponcore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer issuedQuantity;

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

}
