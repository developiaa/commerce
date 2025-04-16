package com.developia.couponcore.repository;

import com.developia.couponcore.domain.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    CouponIssue findFirstByIdAndUserId(Long couponId, Long userId);
}
