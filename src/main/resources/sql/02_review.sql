-- 商品评价表
CREATE TABLE IF NOT EXISTS `product_review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_item_id` BIGINT NOT NULL COMMENT '订单项ID',
  `rating` TINYINT NOT NULL COMMENT '评分 1-5星',
  `content` TEXT COMMENT '评价内容',
  `images` VARCHAR(1000) DEFAULT NULL COMMENT '评价图片，多个图片用逗号分隔',
  `sku_info` VARCHAR(255) DEFAULT NULL COMMENT 'SKU信息',
  `is_anonymous` TINYINT DEFAULT 0 COMMENT '是否匿名评价 0-否 1-是',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';
