CREATE TABLE `commerce`.`coupons`
(
    `id`                   BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `title`                VARCHAR(255) NOT NULL COMMENT '쿠폰명',
    `coupon_type`          VARCHAR(255) NOT NULL COMMENT '쿠폰 타입',
    `total_quantity`       INT          NULL COMMENT '쿠폰 발급 최대 수량',
    `issued_quantity`      INT          NOT NULL COMMENT '발급된 쿠폰 수량',
    `discount_amount`      INT          NOT NULL COMMENT '할인 금액',
    `min_available_amount` INT          NOT NULL COMMENT '최소 사용 금액',
    `issue_started_at`     datetime(6)  NOT NULL COMMENT '발급 시작일',
    `issue_ended_at`       datetime(6)  NOT NULL COMMENT '발급 종료일',
    `created_at`           TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
    `updated_at`           TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '쿠폰';

CREATE TABLE `commerce`.`coupon_issues`
(
    `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `coupon_id`  BIGINT(20)   NOT NULL COMMENT '쿠폰 ID',
    `user_id`    BIGINT(20)   NOT NULL COMMENT '유저 ID',
    `issue_at`   datetime(6)  NOT NULL COMMENT '발급 일시',
    `used_at`     datetime(6)  NULL COMMENT '사용 일시',
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일',
    `updated_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '쿠폰 발행';


insert into `commerce`.`coupons`(title, coupon_type, total_quantity,
                                 issued_quantity, discount_amount, min_available_amount,
                                 issue_started_at, issue_ended_at)
values ('선착순 쿠폰', 'FIRST_COME_FIRST_SERVED',
        '500', '0', '10000', '0',
        '2025-04-05 00:00:00', '2025-05:01 00:00:00')
