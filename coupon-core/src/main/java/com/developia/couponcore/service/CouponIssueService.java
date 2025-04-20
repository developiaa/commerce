package com.developia.couponcore.service;

import com.developia.couponcore.domain.Coupon;
import com.developia.couponcore.domain.CouponIssue;
import com.developia.couponcore.domain.exception.CouponIssueException;
import com.developia.couponcore.domain.exception.CouponNotFoundException;
import com.developia.couponcore.domain.exception.ErrorCode;
import com.developia.couponcore.repository.CouponIssueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CouponIssueService {
    private final CouponReadService couponReadService;
    private final CouponIssueJpaRepository couponIssueJpaRepository;


    @Transactional(rollbackFor = Exception.class)
    public void issue(Long couponId, Long userId) {
        // 쿠폰이 존재하는 지 확인
        Coupon findCoupon = couponReadService.findCoupon(couponId)
                .orElseThrow(() -> new CouponNotFoundException(ErrorCode.NOT_FOUND));
        // 쿠폰 차감
        findCoupon.issue(LocalDateTime.now());
        // 쿠폰 발행 적용
        saveCouponIssue(couponId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void issueWithLock(Long couponId, Long userId) {
        // 쿠폰이 존재하는 지 확인
        Coupon findCoupon = couponReadService.findCouponWithLock(couponId)
                .orElseThrow(() -> new CouponNotFoundException(ErrorCode.NOT_FOUND));
        // 쿠폰 차감
        findCoupon.issue(LocalDateTime.now());
        // 쿠폰 발행 적용
        saveCouponIssue(couponId, userId);
    }

    @Transactional
    public void saveCouponIssue(Long couponId, Long userId) {
        checkAlreadyIssuance(couponId, userId);
        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        couponIssueJpaRepository.save(issue);
    }

    private void checkAlreadyIssuance(long couponId, long userId) {
        CouponIssue issue = couponIssueJpaRepository.findFirstByIdAndUserId(couponId, userId);
        if (issue != null) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE,
                    "user_id: %d, coupon_id: %d".formatted(userId, couponId), true);
        }
    }
}
