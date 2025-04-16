package com.developia.couponcore.service;


import com.developia.couponcore.domain.Coupon;
import com.developia.couponcore.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponReadService {
    private final CouponJpaRepository couponJpaRepository;

    public Optional<Coupon> findCoupon(Long id) {
        return couponJpaRepository.findById(id);
    }

    public Optional<Coupon> findCouponWithLock(Long id) {
        return couponJpaRepository.findCouponWithLock(id);
    }
}
