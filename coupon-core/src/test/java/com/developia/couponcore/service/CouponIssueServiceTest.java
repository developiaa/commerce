package com.developia.couponcore.service;

import com.developia.couponcore.domain.Coupon;
import com.developia.couponcore.domain.CouponIssue;
import com.developia.couponcore.domain.CouponType;
import com.developia.couponcore.domain.exception.CouponIssueException;
import com.developia.couponcore.domain.exception.CouponNotFoundException;
import com.developia.couponcore.domain.exception.ErrorCode;
import com.developia.couponcore.repository.CouponIssueJpaRepository;
import com.developia.couponcore.repository.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceTest {
    @InjectMocks
    CouponIssueService sut;

    @Mock
    CouponReadService couponReadService;

    @Mock
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Mock
    CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void setUp() {
        couponIssueJpaRepository.deleteAllInBatch();
        couponJpaRepository.deleteAllInBatch();
    }

    @Nested
    class issue {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issueDateBefore = now.minusDays(1);
        LocalDateTime issueDateAfter = now.plusDays(1);
        Long userId = 1L;
        Long couponId = 1L;

        Coupon savedCoupon = Coupon.builder()
                .id(couponId)
                .title("테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .discountAmount(10_000)
                .minAvailableAmount(10_000)
                .issueStartedAt(issueDateBefore)
                .issueEndedAt(issueDateAfter)
                .build();

        CouponIssue savedCouponIssue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        @DisplayName("정상적으로 발행되는지 확인 한다")
        @Test
        void issue() {
            given(couponReadService.findCoupon(savedCoupon.getId()))
                    .willReturn(Optional.of(savedCoupon));

            assertDoesNotThrow(() -> sut.issue(savedCoupon.getId(), userId));
        }

        @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환한다.")
        @Test
        void saveCouponIssue1() {
            given(couponIssueJpaRepository.findFirstByIdAndUserId(savedCouponIssue.getCouponId(),
                    savedCouponIssue.getUserId())).willReturn(savedCouponIssue);

            CouponIssueException couponIssueException = assertThrows(CouponIssueException.class,
                    () -> sut.saveCouponIssue(savedCouponIssue.getCouponId(), savedCouponIssue.getUserId()));

            assertEquals(ErrorCode.DUPLICATED_COUPON_ISSUE, couponIssueException.getErrorCode());
        }

        @DisplayName("쿠폰 발급 내역이 존재하지 않는 경우 쿠폰을 발급한다.")
        @Test
        void saveCouponIssue2() {
            given(couponIssueJpaRepository.findFirstByIdAndUserId(savedCouponIssue.getCouponId(),
                    savedCouponIssue.getUserId())).willReturn(null);

            assertDoesNotThrow(() -> sut.saveCouponIssue(savedCouponIssue.getCouponId(), savedCouponIssue.getUserId()));
        }

        @DisplayName("발급 수량에 문제가 있다면 예외를 반환한다")
        @Test
        void saveCouponIssue3() {
            Coupon coupon = Coupon.builder()
                    .id(couponId)
                    .title("테스트 쿠폰")
                    .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                    .totalQuantity(10)
                    .issuedQuantity(10)
                    .discountAmount(10_000)
                    .minAvailableAmount(10_000)
                    .issueStartedAt(issueDateBefore)
                    .issueEndedAt(issueDateAfter)
                    .build();
            given(couponReadService.findCoupon(savedCoupon.getId()))
                    .willReturn(Optional.of(coupon));


            CouponIssueException couponIssueException =
                    assertThrows(CouponIssueException.class, () -> sut.issue(coupon.getId(), userId));


            assertEquals(ErrorCode.INVALID_ISSUE_QUANTITY, couponIssueException.getErrorCode());
        }

        @DisplayName("발급 기한에 문제가 있다면 예외를 반환한다")
        @Test
        void saveCouponIssue4() {
            Coupon coupon = Coupon.builder()
                    .id(couponId)
                    .title("테스트 쿠폰")
                    .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                    .totalQuantity(10)
                    .issuedQuantity(0)
                    .discountAmount(10_000)
                    .minAvailableAmount(10_000)
                    .issueStartedAt(issueDateAfter)
                    .issueEndedAt(issueDateAfter.plusDays(1))
                    .build();
            given(couponReadService.findCoupon(savedCoupon.getId()))
                    .willReturn(Optional.of(coupon));

            CouponIssueException couponIssueException =
                    assertThrows(CouponIssueException.class, () -> sut.issue(coupon.getId(), userId));


            assertEquals(ErrorCode.INVALID_ISSUE_DATE, couponIssueException.getErrorCode());
        }


        @DisplayName("쿠폰이 존재하지 않는다면 예외를 반환한다.")
        @Test
        void saveCouponIssue5() {
            CouponNotFoundException couponNotFoundException =
                    assertThrows(CouponNotFoundException.class, () -> sut.issue(couponId, userId));

            assertEquals(ErrorCode.NOT_FOUND, couponNotFoundException.getErrorCode());
        }

    }
}
