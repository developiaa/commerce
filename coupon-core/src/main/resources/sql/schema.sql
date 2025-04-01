CREATE TABLE `commerce`.`coupons`
(
    `id`                   BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `title`                VARCHAR(255) NOT NULL COMMENT '쿠폰명',
    `coupon_type`          VARCHAR(255) NOT NULL COMMENT '쿠폰 타입',
    `total_quantity`       INT NULL COMMENT '쿠폰 발급 최대 수량',
    `issued_quantity`      INT          NOT NULL COMMENT '발급된 쿠폰 수량',
    `discount_amount`      INT          NOT NULL COMMENT '할인 금액',
    `min_available_amount` INT          NOT NULL COMMENT '최소 사용 금액',
    `issue_started_at`     datetime(6)  NOT NULL COMMENT '발급 시작일',
    `issue_ended_at`       datetime(6)  NOT NULL COMMENT '발급 종료일',
    `created_at`           datetime(6)  NOT NULL COMMENT '생성일',
    `updated_at`           datetime(6)  NOT NULL COMMENT '수정일',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '쿠폰';
