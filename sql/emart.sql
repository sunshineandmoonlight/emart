-- =============================================
-- Emart 电商平台数据库初始化脚本
-- 数据库版本: MySQL 8.0+
-- 创建时间: 2024-12-28
-- =============================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `emart` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `emart`;

-- =============================================
-- 1. 用户权限管理模块
-- =============================================

-- 后台用户表
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(64)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(64)  NOT NULL COMMENT '密码',
    `icon`        VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nick_name`   VARCHAR(200) DEFAULT NULL COMMENT '昵称',
    `note`        VARCHAR(500) DEFAULT NULL COMMENT '备注信息',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `login_time`  DATETIME              DEFAULT NULL COMMENT '最后登录时间',
    `status`      TINYINT(1)            DEFAULT 1 COMMENT '帐号启用状态：0->禁用；1->启用',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='后台用户表';

-- 角色表
DROP TABLE IF EXISTS `ums_role`;
CREATE TABLE `ums_role`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        VARCHAR(100) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '角色描述',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status`      TINYINT(1)            DEFAULT 1 COMMENT '启用状态：0->禁用；1->启用',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- 后台用户角色关系表
DROP TABLE IF EXISTS `ums_admin_role_relation`;
CREATE TABLE `ums_admin_role_relation`
(
    `admin_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id`  BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`admin_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='后台用户角色关系表';

-- 菜单表
DROP TABLE IF EXISTS `ums_menu`;
CREATE TABLE `ums_menu`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   BIGINT                DEFAULT 0 COMMENT '父菜单ID，0表示一级菜单',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `title`       VARCHAR(200) NOT NULL COMMENT '菜单标题',
    `level`       INT(2)                 DEFAULT 0 COMMENT '菜单级别，0表示一级菜单，1表示二级菜单',
    `sort`        INT(4)                 DEFAULT 0 COMMENT '菜单排序',
    `name`        VARCHAR(100) DEFAULT NULL COMMENT '菜单名称（前端路由name）',
    `icon`        VARCHAR(200) DEFAULT NULL COMMENT '菜单图标',
    `hidden`      INT(1)                 DEFAULT 0 COMMENT '是否隐藏：0->不隐藏；1->隐藏',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='后台菜单表';

-- 资源表
DROP TABLE IF EXISTS `ums_resource`;
CREATE TABLE `ums_resource`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '资源ID',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `name`        VARCHAR(200) NOT NULL COMMENT '资源名称',
    `url`         VARCHAR(200) NOT NULL COMMENT '资源URL',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '资源描述',
    `category_id` BIGINT                DEFAULT 0 COMMENT '资源分类ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='后台资源表';

-- 角色菜单关系表
DROP TABLE IF EXISTS `ums_role_menu_relation`;
CREATE TABLE `ums_role_menu_relation`
(
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色菜单关系表';

-- 角色资源关系表
DROP TABLE IF EXISTS `ums_role_resource_relation`;
CREATE TABLE `ums_role_resource_relation`
(
    `role_id`     BIGINT NOT NULL COMMENT '角色ID',
    `resource_id` BIGINT NOT NULL COMMENT '资源ID',
    PRIMARY KEY (`role_id`, `resource_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色资源关系表';

-- =============================================
-- 2. 商品管理模块 (PMS)
-- =============================================

-- 商品分类表
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `parent_id`   BIGINT                DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
    `name`        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `level`       INT(2)                 DEFAULT 1 COMMENT '分类级别：0->一级，1->二级',
    `sort`        INT(4)                 DEFAULT 0 COMMENT '排序',
    `icon`        VARCHAR(200) DEFAULT NULL COMMENT '分类图标',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品分类表';

-- 商品表
DROP TABLE IF EXISTS `pms_product`;
CREATE TABLE `pms_product`
(
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name`        VARCHAR(200)  NOT NULL COMMENT '商品名称',
    `subtitle`    VARCHAR(200)  DEFAULT NULL COMMENT '副标题',
    `price`       DECIMAL(10,2) NOT NULL COMMENT '价格',
    `stock`       INT(11)                DEFAULT 0 COMMENT '库存',
    `category_id` BIGINT                 DEFAULT NULL COMMENT '分类ID',
    `brand`       VARCHAR(100)  DEFAULT NULL COMMENT '品牌',
    `image`       VARCHAR(500)  DEFAULT NULL COMMENT '主图URL',
    `images`      TEXT          DEFAULT NULL COMMENT '轮播图JSON',
    `detail`      TEXT          DEFAULT NULL COMMENT '商品详情（富文本）',
    `status`      TINYINT(1)              DEFAULT 1 COMMENT '上架状态：0->下架；1->上架',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品表';

-- =============================================
-- 3. 订单管理模块 (OMS)
-- =============================================

-- 购物车表
DROP TABLE IF EXISTS `oms_cart`;
CREATE TABLE `oms_cart`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id`     BIGINT      NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT      NOT NULL COMMENT '商品ID',
    `quantity`    INT(11)              DEFAULT 1 COMMENT '商品数量',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='购物车表';

-- 订单表
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`
(
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`         VARCHAR(64)   NOT NULL COMMENT '订单号',
    `user_id`          BIGINT        NOT NULL COMMENT '用户ID',
    `username`         VARCHAR(100)  DEFAULT NULL COMMENT '用户名',
    `total_amount`     DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `status`           TINYINT(2)             DEFAULT 0 COMMENT '订单状态：0->待付款；1->待发货；2->待收货；3->完成；4->取消',
    `receiver_name`    VARCHAR(50)   DEFAULT NULL COMMENT '收货人姓名',
    `receiver_phone`   VARCHAR(20)   DEFAULT NULL COMMENT '收货人电话',
    `receiver_address` VARCHAR(200)  DEFAULT NULL COMMENT '收货地址',
    `pay_time`         DATETIME               DEFAULT NULL COMMENT '付款时间',
    `delivery_time`    DATETIME               DEFAULT NULL COMMENT '发货时间',
    `finish_time`      DATETIME               DEFAULT NULL COMMENT '完成时间',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

-- 订单明细表
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id`      BIGINT        NOT NULL COMMENT '订单ID',
    `product_id`    BIGINT        NOT NULL COMMENT '商品ID',
    `product_name`  VARCHAR(200)  DEFAULT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500)  DEFAULT NULL COMMENT '商品图片',
    `price`         DECIMAL(10,2) DEFAULT NULL COMMENT '商品单价',
    `quantity`      INT(11)                DEFAULT 0 COMMENT '商品数量',
    `total_amount`  DECIMAL(10,2) DEFAULT NULL COMMENT '小计金额',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单明细表';

-- 支付记录表
DROP TABLE IF EXISTS `oms_payment`;
CREATE TABLE `oms_payment`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `order_id`    BIGINT       NOT NULL COMMENT '订单ID',
    `order_no`    VARCHAR(64)  DEFAULT NULL COMMENT '订单号',
    `payment_no`  VARCHAR(64)  DEFAULT NULL COMMENT '支付流水号',
    `amount`      DECIMAL(10,2)         DEFAULT NULL COMMENT '支付金额',
    `status`      TINYINT(1)            DEFAULT 0 COMMENT '支付状态：0->待支付；1->已支付',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付记录表';

-- 订单统计表
DROP TABLE IF EXISTS `oms_order_stats`;
CREATE TABLE `oms_order_stats`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `date`           DATE         NOT NULL COMMENT '统计日期',
    `total_orders`   INT(11)               DEFAULT 0 COMMENT '订单总数',
    `total_amount`   DECIMAL(10,2)         DEFAULT 0.00 COMMENT '总销售额',
    `total_products` INT(11)               DEFAULT 0 COMMENT '销售商品数',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date` (`date`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单统计表';

-- =============================================
-- 4. 内容管理模块 (CMS)
-- =============================================

-- 浏览日志表
DROP TABLE IF EXISTS `cms_browse_log`;
CREATE TABLE `cms_browse_log`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id`      BIGINT               DEFAULT NULL COMMENT '用户ID（游客为NULL）',
    `user_name`    VARCHAR(100)          DEFAULT NULL COMMENT '用户名',
    `product_id`   BIGINT      NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200)          DEFAULT NULL COMMENT '商品名称',
    `ip`           VARCHAR(50)  DEFAULT NULL COMMENT 'IP地址',
    `create_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='浏览日志表';

-- =============================================
-- 5. 系统配置模块
-- =============================================

-- 系统配置表
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key`   VARCHAR(50)  NOT NULL COMMENT '配置键',
    `config_value` VARCHAR(500) DEFAULT NULL COMMENT '配置值',
    `description`  VARCHAR(200) DEFAULT NULL COMMENT '配置说明',
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统配置表';

-- =============================================
-- 6. 初始化数据
-- =============================================

-- 初始化管理员账号：用户名 admin，密码 123456（BCrypt加密）
INSERT INTO `ums_admin` (`id`, `username`, `password`, `nick_name`, `status`)
VALUES (1, 'admin',
        '$2a$10$NZR14EBlZRFeEQmbr8ZQAehuKV9HGj4sYQSgQTmHKqPqTzHLxJ3eK', '超级管理员', 1);

-- 初始化角色
INSERT INTO `ums_role` (`id`, `name`, `description`)
VALUES (1, '超级管理员', '拥有所有权限'),
       (2, '普通管理员', '拥有部分权限');

-- 初始化用户角色关系
INSERT INTO `ums_admin_role_relation` (`admin_id`, `role_id`)
VALUES (1, 1);

-- 初始化菜单
INSERT INTO `ums_menu` (`id`, `parent_id`, `title`, `level`, `sort`, `name`, `icon`)
VALUES (1, 0, '商品', 1, 0, 'product', 'product'),
       (2, 0, '订单', 1, 1, 'order', 'order'),
       (3, 0, '营销', 1, 2, 'marketing', 'marketing'),
       (4, 0, '系统', 1, 3, 'system', 'system');

-- 初始化商品分类
INSERT INTO `pms_category` (`id`, `parent_id`, `name`, `level`, `sort`)
VALUES (1, 0, '编程语言', 1, 1),
       (2, 0, 'Web开发', 1, 2),
       (3, 0, '数据库', 1, 3),
       (4, 0, '移动开发', 1, 4),
       (5, 1, 'Java', 2, 1),
       (6, 1, 'Python', 2, 2),
       (7, 2, '前端', 2, 1),
       (8, 2, '后端', 2, 2);

-- 初始化商品数据（10本虚拟书籍）
INSERT INTO `pms_product` (`id`, `name`, `subtitle`, `price`, `stock`, `category_id`, `brand`, `image`, `detail`, `status`)
VALUES (1, 'Java编程思想（第4版）', 'Java经典必读丛书', 89.00, 100, 5, '机械工业出版社',
        '/uploads/java-book.jpg',
        '<p>本书赢得了全球程序员的广泛赞誉，即使是最晦涩的概念，在Bruce Eckel的文字亲和力和小而清晰的示例面前也变得浅显易懂。</p>',
        1),
       (2, 'SpringBoot实战', '从零到精通SpringBoot', 129.00, 50, 5, '人民邮电出版社',
        '/uploads/springboot.jpg',
        '<p>本书深入浅出地讲解了SpringBoot的核心技术和最佳实践，是SpringBoot学习的必读书籍。</p>',
        1),
       (3, '算法导论（第3版）', '计算机科学丛书', 108.00, 80, 1, '机械工业出版社',
        '/uploads/algorithm.jpg',
        '<p>该书深入浅出地介绍了大量算法，对每一个算法的分析既易于理解又十分有趣。</p>',
        1),
       (4, '深入理解JVM', '高级Java开发必备', 79.00, 60, 5, '机械工业出版社',
        '/uploads/jvm.jpg',
        '<p>本书从Java程序员的角度出发，系统讲解了JVM的各种技术和工作原理。</p>',
        1),
       (5, 'MySQL必知必会', '数据库入门经典', 45.00, 200, 3, '人民邮电出版社',
        '/uploads/mysql.jpg',
        '<p>本书注重实战，通过大量实例讲解MySQL的使用，是MySQL入门的最佳选择。</p>',
        1),
       (6, 'Git权威指南', '版本控制从入门到精通', 55.00, 150, 1, '机械工业出版社',
        '/uploads/git.jpg',
        '<p>本书系统讲解了Git的使用，从基础到高级，全面覆盖Git的各种功能。</p>',
        1),
       (7, '设计模式之美', '架构师进阶之路', 99.00, 40, 1, '电子工业出版社',
        '/uploads/design-pattern.jpg',
        '<p>本书深入讲解了23种设计模式，并结合实际案例，帮助读者掌握设计模式的精髓。</p>',
        1),
       (8, 'Linux就该这么学', '运维实战宝典', 68.00, 90, 1, '人民邮电出版社',
        '/uploads/linux.jpg',
        '<p>本书从零开始讲解Linux，适合初学者入门，也适合有一定基础的读者进阶。</p>',
        1),
       (9, 'Python编程入门', '零基础学Python', 59.00, 120, 6, '清华大学出版社',
        '/uploads/python.jpg',
        '<p>本书适合零基础的读者，从Python的基础语法开始，逐步深入到实战应用。</p>',
        1),
       (10, 'Vue.js实战', '前端开发实战教程', 75.00, 70, 7, '电子工业出版社',
         '/uploads/vue.jpg',
         '<p>本书全面讲解Vue.js框架，包括组件、路由、状态管理等核心技术。</p>',
         1);

-- 初始化系统配置（支付二维码占位）
INSERT INTO `sys_config` (`config_key`, `config_value`, `description`)
VALUES ('payment_qrcode', '/uploads/payment-qrcode.jpg', '支付收款二维码'),
       ('payment_account', '商家微信号/支付宝账号', '收款账号说明'),
       ('site_name', 'Emart电商平台', '网站名称'),
       ('site_description', 'Emart - 品质生活电商平台', '网站描述');

-- =============================================
-- 数据初始化完成
-- =============================================
