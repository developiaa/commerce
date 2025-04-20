package com.developia.couponapi.service;

import com.developia.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponIssueRequestService {
    private final CouponIssueService couponIssueService;

    // synchronized
    public void issueRequestWithSynchronized(Long couponId, Long userId) {
        synchronized (this) {
            couponIssueService.issue(couponId, userId);
        }
    }

    // db 락 적용
    public void issueRequestWithMysqlLock(Long couponId, Long userId) {
        couponIssueService.issueWithLock(couponId, userId);
    }

    // 분산락 적용
    public void issueRequestWithRedisLock() {
    }
}
