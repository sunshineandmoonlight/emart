-- 优惠券表
CREATE TABLE IF NOT EXISTS `sms_coupon` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '优惠券类型；0->满减券；1->折扣券；2->无门槛券',
  `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `platform` TINYINT NOT NULL DEFAULT 0 COMMENT '使用平台；0->全平台；1->移动端；2->PC端',
  `count` INT NOT NULL DEFAULT 0 COMMENT '发行数量',
  `per_limit` INT NOT NULL DEFAULT 1 COMMENT '每人限领数量',
  `min_point` DECIMAL(10,2) DEFAULT 0.00 COMMENT '使用门槛金额；0表示无门槛',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '优惠金额或折扣率',
  `total_count` INT NOT NULL DEFAULT 0 COMMENT '已领取数量',
  `use_count` INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `receive_count` INT NOT NULL DEFAULT 0 COMMENT '已领取数量',
  `enable_time` DATETIME NOT NULL COMMENT '可以领取的时间',
  `disable_time` DATETIME NOT NULL COMMENT '不可领取的时间',
  `validity_type` TINYINT NOT NULL DEFAULT 0 COMMENT '有效期类型；0->固定时间；1->领取后N天内有效',
  `validity_days` INT DEFAULT 0 COMMENT '领取后有效天数',
  `start_time` DATETIME DEFAULT NULL COMMENT '有效期开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '有效期结束时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态；0->禁用；1->启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_enable_time` (`enable_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券记录表
CREATE TABLE IF NOT EXISTS `sms_coupon_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `coupon_code` VARCHAR(64) NOT NULL COMMENT '优惠券编码',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态；0->未使用；1->已使用；2->已过期',
  `use_time` DATETIME DEFAULT NULL COMMENT '使用时间',
  `order_id` BIGINT DEFAULT NULL COMMENT '订单ID',
  `receive_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `validity_start_time` DATETIME NOT NULL COMMENT '有效期开始时间',
  `validity_end_time` DATETIME NOT NULL COMMENT '有效期结束时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_code` (`coupon_code`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券记录表';

-- 插入测试优惠券数据
INSERT INTO sms_coupon (type, name, platform, count, per_limit, min_point, amount, enable_time, disable_time, validity_type, validity_days, start_time, end_time, status) VALUES
(0, '新用户专享券', 0, 1000, 1, 99.00, 20.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 0, 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 1),
(0, '满200减30', 0, 500, 2, 200.00, 30.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 0, 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 1),
(1, '全场9折券', 0, 2000, 3, 50.00, 0.90, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 1, 7, NULL, NULL, 1),
(2, '无门槛10元券', 0, 3000, 5, 0.00, 10.00, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 1, 30, NULL, NULL, 1);
