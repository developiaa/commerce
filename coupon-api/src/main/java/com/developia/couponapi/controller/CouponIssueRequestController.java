package com.developia.couponapi.controller;

import com.developia.couponapi.controller.dto.CouponIssueRequest;
import com.developia.couponapi.controller.dto.CouponIssueResponse;
import com.developia.couponapi.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons/issue")
@RequiredArgsConstructor
@RestController
public class CouponIssueRequestController {
    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1")
    public CouponIssueResponse issueV1(@RequestBody CouponIssueRequest couponIssueRequest) {
        couponIssueRequestService.issueRequestWithSynchronized(couponIssueRequest.userId(), couponIssueRequest.couponId());
        return new CouponIssueResponse(true, null);
    }

    @PostMapping("/v2")
    public CouponIssueResponse issueV2(@RequestBody CouponIssueRequest couponIssueRequest) {
        couponIssueRequestService.issueRequestWithMysqlLock(couponIssueRequest.userId(), couponIssueRequest.couponId());
        return new CouponIssueResponse(true, null);
    }

}
