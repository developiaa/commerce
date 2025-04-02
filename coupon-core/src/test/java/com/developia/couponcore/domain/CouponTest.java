package com.developia.couponcore.domain;

import com.developia.couponcore.domain.exception.CouponIssueException;
import com.developia.couponcore.domain.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @DisplayName("발급 수량")
    @Nested
    class totalQuantity {
        Integer totalQuantity = 10;
        Integer issuedQuantity = 9;

        @DisplayName("남아있다면 true")
        @Test
        void test() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(totalQuantity)
                    .issuedQuantity(issuedQuantity)
                    .build();

            boolean issueAvailable = coupon.isIssueQuantityAvailable();

            assertTrue(issueAvailable);
        }

        @DisplayName("소진되었다면 false")
        @Test
        void test2() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(totalQuantity)
                    .issuedQuantity(totalQuantity)
                    .build();

            boolean issueAvailable = coupon.isIssueQuantityAvailable();

            assertFalse(issueAvailable);
        }

        @DisplayName("제한이 없는 경우 false")
        @Test
        void test3() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(null)
                    .issuedQuantity(issuedQuantity)
                    .build();

            boolean issueAvailable = coupon.isIssueQuantityAvailable();

            assertFalse(issueAvailable);
        }
    }

    @DisplayName("발급 기간")
    @Nested
    class issueDate {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issueDate2dayBefore = now.minusDays(2);
        LocalDateTime issueDateBefore = now.minusDays(1);
        LocalDateTime issueDateAfter = now.plusDays(1);
        LocalDateTime issueDate2dayAfter = issueDateAfter.plusDays(2);

        @DisplayName("시작되지 않았다면 false")
        @Test
        void test() {
            Coupon coupon = Coupon.builder()
                    .issueStartedAt(issueDateAfter)
                    .issueEndedAt(issueDate2dayAfter)
                    .build();

            boolean issueDateAvailable = coupon.isIssueDateAvailable(now);

            assertFalse(issueDateAvailable);
        }

        @DisplayName("해당되면 true")
        @Test
        void test2() {
            Coupon coupon = Coupon.builder()
                    .issueStartedAt(issueDateBefore)
                    .issueEndedAt(issueDateAfter)
                    .build();

            boolean issueDateAvailable = coupon.isIssueDateAvailable(now);

            assertTrue(issueDateAvailable);
        }

        @DisplayName("지났다면 false")
        @Test
        void test3() {
            Coupon coupon = Coupon.builder()
                    .issueStartedAt(issueDate2dayBefore)
                    .issueEndedAt(issueDateBefore)
                    .build();

            boolean issueDateAvailable = coupon.isIssueDateAvailable(now);

            assertFalse(issueDateAvailable);
        }
    }

    @DisplayName("발행")
    @Nested
    class issue {
        Integer totalQuantity = 10;
        Integer issuedQuantity = 9;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issueDateBefore = now.minusDays(1);
        LocalDateTime issueDateAfter = now.plusDays(1);
        LocalDateTime issueDate2dayAfter = issueDateAfter.plusDays(2);

        @DisplayName("발급 수량과 발급 기간이 유효하다면 발급에 성공한다")
        @Test
        void test1() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(totalQuantity)
                    .issuedQuantity(issuedQuantity)
                    .issueStartedAt(issueDateBefore)
                    .issueEndedAt(issueDateAfter)
                    .build();

            coupon.issue(now);

            assertEquals(coupon.getIssuedQuantity(), totalQuantity);
        }

        @DisplayName("발급 기간이 아니면 예외를 반환한다")
        @Test
        void test2() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(totalQuantity)
                    .issuedQuantity(issuedQuantity)
                    .issueStartedAt(issueDateAfter)
                    .issueEndedAt(issueDate2dayAfter)
                    .build();

            CouponIssueException couponIssueException = assertThrows(CouponIssueException.class,
                    () -> coupon.issue(now));


            assertAll(
                    () -> assertEquals(ErrorCode.INVALID_ISSUE_DATE, couponIssueException.getErrorCode()),
                    () -> assertEquals(issuedQuantity, coupon.getIssuedQuantity())
            );
        }

        @DisplayName("발급 수량을 초과하면 예외를 반환한다")
        @Test
        void test3() {
            Coupon coupon = Coupon.builder()
                    .totalQuantity(totalQuantity)
                    .issuedQuantity(totalQuantity)
                    .issueStartedAt(issueDateBefore)
                    .issueEndedAt(issueDate2dayAfter)
                    .build();

            CouponIssueException couponIssueException = assertThrows(CouponIssueException.class,
                    () -> coupon.issue(now));

            assertAll(
                    () -> assertEquals(ErrorCode.INVALID_ISSUE_QUANTITY, couponIssueException.getErrorCode()),
                    () -> assertEquals(totalQuantity, coupon.getIssuedQuantity())
            );
        }
    }
}
