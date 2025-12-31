-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: emart
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cms_browse_log`
--

DROP TABLE IF EXISTS `cms_browse_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_browse_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（游客为NULL）',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='浏览日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_browse_log`
--

LOCK TABLES `cms_browse_log` WRITE;
/*!40000 ALTER TABLE `cms_browse_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_browse_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oms_cart`
--

DROP TABLE IF EXISTS `oms_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oms_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int DEFAULT '1' COMMENT '商品数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oms_cart`
--

LOCK TABLES `oms_cart` WRITE;
/*!40000 ALTER TABLE `oms_cart` DISABLE KEYS */;
INSERT INTO `oms_cart` VALUES (18,3,215,2,'2025-12-31 17:40:03','2025-12-31 17:40:03');
/*!40000 ALTER TABLE `oms_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oms_order`
--

DROP TABLE IF EXISTS `oms_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oms_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint DEFAULT '0' COMMENT '订单状态：0->待付款；1->待发货；2->待收货；3->完成；4->取消',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) DEFAULT NULL COMMENT '收货地址',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oms_order`
--

LOCK TABLES `oms_order` WRITE;
/*!40000 ALTER TABLE `oms_order` DISABLE KEYS */;
INSERT INTO `oms_order` VALUES (1,'ORDER20251228181354980',3,'111',20998.00,4,'1','18889855221','1',NULL,NULL,NULL,'2025-12-28 18:13:55','2025-12-28 18:13:54'),(2,'ORDER20251228195615460',3,'111',38997.00,1,'6666','18889855221','6666','2025-12-28 22:32:37',NULL,NULL,'2025-12-28 19:56:15','2025-12-28 19:56:15'),(3,'ORDER20251228212618908',3,'111',12999.00,4,'1','18889855221','1',NULL,NULL,NULL,'2025-12-28 21:26:19','2025-12-28 21:26:18'),(4,'ORDER20251228213254231',3,'111',89.00,4,'1','18889855221','1',NULL,NULL,NULL,'2025-12-28 21:32:54','2025-12-28 21:32:54'),(5,'ORDER20251228220436587',4,'123456',12999.00,4,'1','18889855221','1',NULL,NULL,NULL,'2025-12-28 22:04:37','2025-12-28 22:04:36'),(6,'ORDER20251228221839051',4,'123456',7999.00,0,'1','18889855221','1',NULL,NULL,NULL,'2025-12-28 22:18:39','2025-12-28 22:18:39'),(7,'ORDER20251228225004380',3,'111',89.00,1,'1','18889855221','1','2025-12-28 22:51:16',NULL,NULL,'2025-12-28 22:50:04','2025-12-28 22:50:04'),(8,'ORDER20251228225645571',3,'111',7999.00,1,'1','18889855221','1','2025-12-28 22:56:59',NULL,NULL,'2025-12-28 22:56:46','2025-12-28 22:56:45'),(9,'ORDER20251228225912667',7,'1111',23997.00,1,'1','18889855221','1','2025-12-28 22:59:20',NULL,NULL,'2025-12-28 22:59:13','2025-12-28 22:59:12'),(10,'ORDER20251231160308558',3,'111',15998.00,1,'1','18889855221','1','2026-01-01 01:19:39',NULL,NULL,'2025-12-31 16:03:09','2025-12-31 16:03:08');
/*!40000 ALTER TABLE `oms_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oms_order_item`
--

DROP TABLE IF EXISTS `oms_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oms_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `quantity` int DEFAULT '0' COMMENT '商品数量',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oms_order_item`
--

LOCK TABLES `oms_order_item` WRITE;
/*!40000 ALTER TABLE `oms_order_item` DISABLE KEYS */;
INSERT INTO `oms_order_item` VALUES (1,1,11,'iPhone 15 Pro','/images/iphone15.jpg',7999.00,1,7999.00),(2,1,12,'MacBook Pro','/images/macbook.jpg',12999.00,1,12999.00),(3,2,12,'MacBook Pro','/images/macbook.jpg',12999.00,3,38997.00),(4,3,12,'MacBook Pro','/images/macbook.jpg',12999.00,1,12999.00),(5,4,1,'Java编程思想（第4版）','/uploads/java-book.jpg',89.00,1,89.00),(6,5,12,'MacBook Pro','/images/macbook.jpg',12999.00,1,12999.00),(7,6,11,'iPhone 15 Pro','/images/iphone15.jpg',7999.00,1,7999.00),(8,7,1,'Java编程思想（第4版）','/uploads/java-book.jpg',89.00,1,89.00),(9,8,11,'iPhone 15 Pro','/images/iphone15.jpg',7999.00,1,7999.00),(10,9,11,'iPhone 15 Pro','/images/iphone15.jpg',7999.00,3,23997.00),(11,10,11,'iPhone 15 Pro','/images/iphone15.jpg',7999.00,2,15998.00);
/*!40000 ALTER TABLE `oms_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oms_order_stats`
--

DROP TABLE IF EXISTS `oms_order_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oms_order_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `date` date NOT NULL COMMENT '统计日期',
  `total_orders` int DEFAULT '0' COMMENT '订单总数',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '总销售额',
  `total_products` int DEFAULT '0' COMMENT '销售商品数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oms_order_stats`
--

LOCK TABLES `oms_order_stats` WRITE;
/*!40000 ALTER TABLE `oms_order_stats` DISABLE KEYS */;
/*!40000 ALTER TABLE `oms_order_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oms_payment`
--

DROP TABLE IF EXISTS `oms_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oms_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单号',
  `payment_no` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `status` tinyint(1) DEFAULT '0' COMMENT '支付状态：0->待支付；1->已支付',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oms_payment`
--

LOCK TABLES `oms_payment` WRITE;
/*!40000 ALTER TABLE `oms_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `oms_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pms_category`
--

DROP TABLE IF EXISTS `pms_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pms_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID，0表示一级分类',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `level` int DEFAULT '1' COMMENT '分类级别：0->一级，1->二级',
  `sort` int DEFAULT '0' COMMENT '排序',
  `icon` varchar(200) DEFAULT NULL COMMENT '分类图标',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pms_category`
--

LOCK TABLES `pms_category` WRITE;
/*!40000 ALTER TABLE `pms_category` DISABLE KEYS */;
INSERT INTO `pms_category` VALUES (1,0,'手机数码',1,1,NULL,'2025-12-28 04:03:30'),(2,0,'电脑办公',1,2,NULL,'2025-12-28 04:03:30'),(3,0,'家用电器',1,3,NULL,'2025-12-28 04:03:30'),(4,0,'服装鞋帽',1,4,NULL,'2025-12-28 04:03:30'),(5,1,'美妆护肤',2,1,NULL,'2025-12-28 04:03:30'),(6,1,'食品饮料',2,2,NULL,'2025-12-28 04:03:30'),(7,2,'运动户外',2,1,NULL,'2025-12-28 04:03:30'),(8,2,'家居生活',2,2,NULL,'2025-12-28 04:03:30'),(9,0,'母婴用品',0,0,'string','2025-12-28 15:20:55'),(10,0,'汽车用品',1,0,'electronics','2025-12-28 15:25:17');
/*!40000 ALTER TABLE `pms_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pms_product`
--

DROP TABLE IF EXISTS `pms_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pms_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '副标题',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int DEFAULT '0' COMMENT '库存',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `image` varchar(500) DEFAULT NULL COMMENT '主图URL',
  `images` text COMMENT '轮播图JSON',
  `detail` text COMMENT '商品详情（富文本）',
  `status` tinyint(1) DEFAULT '1' COMMENT '上架状态：0->下架；1->上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `product_code` varchar(50) DEFAULT NULL COMMENT '商品编号',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pms_product`
--

LOCK TABLES `pms_product` WRITE;
/*!40000 ALTER TABLE `pms_product` DISABLE KEYS */;
INSERT INTO `pms_product` VALUES (214,'Apple iPhone 15 Pro Max','A17 Pro芯片 钛金属 6.7英寸',9999.00,100,1,'Apple','http://img.alicdn.com/img/i1/116558136/O1CN01APsVvX29yLUOzVTUx_!!4611686018427382072-2-saturn_solar.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD01214'),(215,'Apple iPhone 15','USB-C接口 48MP相机 6.1英寸',5999.00,150,1,'Apple','http://img.alicdn.com/img/i4/9253385527/O1CN01m83rZm1qhQAL4Yczc_!!4611686018427385143-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD01215'),(216,'Xiaomi 14 Ultra','徕卡相机 骁龙8Gen3 2K屏幕',5999.00,80,1,'小米','http://img.alicdn.com/img/i2/43375068/O1CN01qVUcQ61nJCKohTm7U_!!4611686018427386332-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD01216'),(220,'联想拯救者Y9000P','i9 RTX4060 16寸电竞',9999.00,40,2,'联想','http://img.alicdn.com/img/i1/6966037065/O1CN01EW5eqT223pJsKHsrS_!!4611686018427382345-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD02220'),(221,'戴尔XPS 13 Plus','i7 16GB OLED触控',10999.00,45,2,'戴尔','http://img.alicdn.com/img/i2/119082351/O1CN01czVYp31TEoFZG20ZM_!!4611686018427383151-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD02221'),(222,'罗技MX Master 3S','无线鼠标 8K传感器',699.00,300,2,'罗技','http://img.alicdn.com/img/i4/2071160107/O1CN01ZpGZQB1Cf3RQievMF_!!4611686018427381035-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD02222'),(226,'小米扫地机器人','激光导航 4000Pa吸力',1699.00,180,3,'小米','http://img.alicdn.com/img/i4/131202939/O1CN01v5mkzY1Xa78sT6mlZ_!!4611686018427387771-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD03226'),(227,'戴森V12 Detect','激光探测 轻量化 60分钟',3299.00,90,3,'戴森','http://img.alicdn.com/img/i3/4994177977/O1CN01gHs3q928nWWMVFloN_!!4611686018427386809-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD03227'),(228,'美的1.5匹空调','一级能效 变频',2999.00,150,3,'美的','http://img.alicdn.com/img/i1/6576306589/O1CN01iz7fx91yXon7V1Dmf_!!4611686018427386269-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD03228'),(232,'Nike Air Max 270','气垫缓震 透气网面',1099.00,200,4,'Nike','https://gw.alicdn.com/imgextra/O1CN01qCP7qz1MHpZF2RKUV_!!2549841410-0-tmg_sticker.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD04232'),(233,'Adidas Ultraboost 22','Boost缓震 Primeknit',1299.00,180,4,'Adidas','http://img.alicdn.com/img/i3/172140044/O1CN01RInVuz1CCCXZkmsMq_!!4611686018427381260-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD04233'),(234,'优衣库轻型羽绒服','轻薄便携 防水防风',499.00,500,4,'优衣库','https://g-search2.alicdn.com/img/bao/uploaded/i4/i2/196993935/O1CN01TKAu4J1ewHdEKIPrq_!!4611686018427380623-0-item_pic.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD04234'),(238,'雅诗兰黛小棕瓶','修护精华 抗氧化',899.00,300,5,'雅诗兰黛','http://img.alicdn.com/img/i3/55577118/O1CN01ZElE2022S6JIoZgKd_!!4611686018427382302-2-saturn_solar.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD05238'),(239,'兰蔻粉水','温和卸妆 补水保湿',399.00,400,5,'兰蔻','http://img.alicdn.com/img/i1/1458310108/O1CN01LAgBPS1CfVt667xOF_!!4611686018427382748-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD05239'),(240,'SK-II神仙水','Pitera 提亮肤色',1599.00,200,5,'SK-II','https://gw.alicdn.com/imgextra/O1CN01QGC0au1l4QMp8woWj_!!917264765.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD05240'),(244,'星巴克咖啡豆','阿拉比卡 1kg装',299.00,600,6,'星巴克','http://img.alicdn.com/img/i2/9829347892/O1CN01OBGEiQ28AarXQLoIX_!!4611686018427384372-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD06244'),(245,'茅台飞天53度','酱香型 500ml',2599.00,100,6,'茅台','http://img.alicdn.com/img/i3/1395400010/O1CN01xuJoTy1Bwd3yGJvJA_!!4611686018427387210-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD06245'),(246,'五常有机大米','当季新米 5kg',199.00,800,6,'五常','http://img.alicdn.com/img/i3/17220675/O1CN01zuo1OQ1GrCSGEIHXN_!!4611686018427380803-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD06246'),(250,'迪卡侬公路车','碳纤维 22速变速',3999.00,50,7,'迪卡侬','http://img.alicdn.com/img/i4/15198680/O1CN01juQfPD2DzUwogDj5n_!!4611686018427382232-2-saturn_solar.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD07250'),(251,'Wilson网球拍','专业级 碳素进攻',899.00,200,7,'Wilson','http://img.alicdn.com/img/i1/4076721450/O1CN01EsOJEE1Ma9FVI2eU8_!!4076721450-0-alimamacc.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD07251'),(252,'Nike瑜伽垫','防滑材质 6mm',199.00,500,7,'Nike','https://gw.alicdn.com/imgextra/O1CN013le5aA1mhBVAWlw2Z_!!678664985-0-picasso.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD07252'),(256,'宜家台灯','LED护眼 可调光',199.00,500,8,'宜家','https://g-search2.alicdn.com/img/bao/uploaded/i4/i4/702699447/O1CN01Ty7qSL2Jemi21vSMD_!!702699447.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD08256'),(257,'无印良品收纳箱','简约 3件套',159.00,800,8,'无印良品','http://img.alicdn.com/img/i1/8040630121/O1CN016SgE4I1ClT19T36GS_!!4611686018427381609-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD08257'),(258,'记忆棉枕头','慢回弹 透气',199.00,600,8,'记忆棉','http://img.alicdn.com/img/i3/1753690070/O1CN01FhLX3J1CO6o6LsNHU_!!4611686018427383766-2-saturn_solar.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD08258'),(262,'惠氏启赋奶粉','OPO益生菌 3段800g',398.00,400,9,'惠氏','http://img.alicdn.com/img/i2/57072748/O1CN01kJR49H1WAdTaIgJvO_!!4611686018427386988-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD09262'),(263,'好奇纸尿裤','超薄透气 M码64片',159.00,600,9,'好奇','https://img.alicdn.com/imgextra/i3/2201232546672/O1CN01sADTx31z9pcoalv1C_!!2201232546672-2-alimamacc.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD09263'),(264,'费雪海马安抚','助眠 可水洗',129.00,500,9,'费雪','http://img.alicdn.com/img/i1/29438357/O1CN01Hl9hsg2BbZ2GEtPhK_!!4611686018427384213-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD09264'),(268,'米其林浩悦4代','静音舒适 205/55R16',659.00,300,10,'米其林','https://img.alicdn.com/imgextra/i2/4174161688/O1CN01qjMzgQ1OL9flN6Hdu_!!4174161688-2-alimamacc.png',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD10268'),(269,'3M车蜡','保护车漆 300g',128.00,800,10,'3M','http://img.alicdn.com/img/i1/501990099/O1CN016DmFfJ1CbOCBT82WB_!!0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD10269'),(270,'Garmin导航仪','4.3寸 实时路况',999.00,200,10,'Garmin','http://img.alicdn.com/img/i2/57731293/O1CN015BMT9w1LQFBY5P2MV_!!4611686018427381981-0-saturn_solar.jpg',NULL,NULL,1,'2025-12-31 17:27:23','2025-12-31 17:50:51','SD10270');
/*!40000 ALTER TABLE `pms_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_review`
--

DROP TABLE IF EXISTS `product_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_item_id` bigint NOT NULL COMMENT '订单项ID',
  `rating` tinyint NOT NULL COMMENT '评分 1-5星',
  `content` text COMMENT '评价内容',
  `images` varchar(1000) DEFAULT NULL COMMENT '评价图片，多个图片用逗号分隔',
  `sku_info` varchar(255) DEFAULT NULL COMMENT 'SKU信息',
  `is_anonymous` tinyint DEFAULT '0' COMMENT '是否匿名评价 0-否 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_review`
--

LOCK TABLES `product_review` WRITE;
/*!40000 ALTER TABLE `product_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` varchar(500) DEFAULT NULL COMMENT '配置值',
  `description` varchar(200) DEFAULT NULL COMMENT '配置说明',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'payment_qrcode','/uploads/payment-qrcode.jpg','支付收款二维码','2025-12-28 04:03:30','2025-12-28 04:03:30'),(2,'payment_account','商家微信号/支付宝账号','收款账号说明','2025-12-28 04:03:30','2025-12-28 04:03:30'),(3,'site_name','Emart电商平台','网站名称','2025-12-28 04:03:30','2025-12-28 04:03:30'),(4,'site_description','Emart - 品质生活电商平台','网站描述','2025-12-28 04:03:30','2025-12-28 04:03:30');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_admin`
--

DROP TABLE IF EXISTS `ums_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_admin`
--

LOCK TABLES `ums_admin` WRITE;
/*!40000 ALTER TABLE `ums_admin` DISABLE KEYS */;
INSERT INTO `ums_admin` VALUES (1,'admin','$2a$10$NZR14EBlZRFeEQmbr8ZQAehuKV9HGj4sYQSgQTmHKqPqTzHLxJ3eK',NULL,NULL,'超级管理员',NULL,'2025-12-28 04:03:30',NULL,1),(2,'testuser','$2a$10$61DHTa6ouzxVsO8QDVK2OOfblBY70SLFu99.QhcZuz8PcRYT.dKSC',NULL,'test@example.com','测试用户',NULL,'2025-12-28 15:22:53','2025-12-28 15:43:17',1),(3,'111','$2a$10$tQ9DGPxi3jXrD4t6DTvqsujgfILO1hbZ.Am9MGEP9j267yHTACnfG',NULL,'123@qq.com','111',NULL,'2025-12-28 16:13:44','2026-01-01 01:19:25',1),(4,'123456','$2a$10$4cbB1FM0oCSqnp9ZtPLatuOz19HVYA5JNDIcn0uozJemkcujgx7K6',NULL,'123@qq.com','123456',NULL,'2025-12-28 22:03:47','2025-12-28 22:18:11',1),(7,'1111','$2a$10$eQTfhGhRTs.hX9N2qLWJWuqUQQVEcq9Qg1Vbv/NFBmorMB08QW.lC',NULL,'2264575523@qq.com','test1',NULL,'2025-12-28 22:57:55','2025-12-28 22:58:01',1);
/*!40000 ALTER TABLE `ums_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_admin_role_relation`
--

DROP TABLE IF EXISTS `ums_admin_role_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_admin_role_relation` (
  `admin_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`admin_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_admin_role_relation`
--

LOCK TABLES `ums_admin_role_relation` WRITE;
/*!40000 ALTER TABLE `ums_admin_role_relation` DISABLE KEYS */;
INSERT INTO `ums_admin_role_relation` VALUES (1,1);
/*!40000 ALTER TABLE `ums_admin_role_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_menu`
--

DROP TABLE IF EXISTS `ums_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID，0表示一级菜单',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) NOT NULL COMMENT '菜单标题',
  `level` int DEFAULT '0' COMMENT '菜单级别，0表示一级菜单，1表示二级菜单',
  `sort` int DEFAULT '0' COMMENT '菜单排序',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单名称（前端路由name）',
  `icon` varchar(200) DEFAULT NULL COMMENT '菜单图标',
  `hidden` int DEFAULT '0' COMMENT '是否隐藏：0->不隐藏；1->隐藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_menu`
--

LOCK TABLES `ums_menu` WRITE;
/*!40000 ALTER TABLE `ums_menu` DISABLE KEYS */;
INSERT INTO `ums_menu` VALUES (1,0,'2025-12-28 04:03:30','商品',1,0,'product','product',0),(2,0,'2025-12-28 04:03:30','订单',1,1,'order','order',0),(3,0,'2025-12-28 04:03:30','营销',1,2,'marketing','marketing',0),(4,0,'2025-12-28 04:03:30','系统',1,3,'system','system',0);
/*!40000 ALTER TABLE `ums_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_resource`
--

DROP TABLE IF EXISTS `ums_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(200) NOT NULL COMMENT '资源名称',
  `url` varchar(200) NOT NULL COMMENT '资源URL',
  `description` varchar(500) DEFAULT NULL COMMENT '资源描述',
  `category_id` bigint DEFAULT '0' COMMENT '资源分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_resource`
--

LOCK TABLES `ums_resource` WRITE;
/*!40000 ALTER TABLE `ums_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `ums_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_role`
--

DROP TABLE IF EXISTS `ums_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `description` varchar(500) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_role`
--

LOCK TABLES `ums_role` WRITE;
/*!40000 ALTER TABLE `ums_role` DISABLE KEYS */;
INSERT INTO `ums_role` VALUES (1,'超级管理员','拥有所有权限','2025-12-28 04:03:30',1),(2,'普通管理员','拥有部分权限','2025-12-28 04:03:30',1);
/*!40000 ALTER TABLE `ums_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_role_menu_relation`
--

DROP TABLE IF EXISTS `ums_role_menu_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role_menu_relation` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_role_menu_relation`
--

LOCK TABLES `ums_role_menu_relation` WRITE;
/*!40000 ALTER TABLE `ums_role_menu_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `ums_role_menu_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_role_resource_relation`
--

DROP TABLE IF EXISTS `ums_role_resource_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role_resource_relation` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`role_id`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色资源关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_role_resource_relation`
--

LOCK TABLES `ums_role_resource_relation` WRITE;
/*!40000 ALTER TABLE `ums_role_resource_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `ums_role_resource_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-01  1:35:27
