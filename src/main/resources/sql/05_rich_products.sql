-- 丰富的商品测试数据
-- 包含不同分类的商品：手机、电脑、家电、服装、食品、美妆等

-- 清空现有商品数据（可选）
-- TRUNCATE TABLE pms_product;

-- 插入商品数据
INSERT INTO pms_product (name, subtitle, price, stock, category_id, image, detail, status, create_time) VALUES
-- 手机数码类
('iPhone 15 Pro Max 256GB', 'A17 Pro芯片 | 钛金属材质 | 8K视频拍摄', 9999.00, 100, 1, '/uploads/products/iphone15promax.jpg', '<p>最新款iPhone，配备A17 Pro芯片，钛金属边框，支持8K视频拍摄。</p>', 1, NOW()),
('iPhone 15 128GB', 'USB-C接口 | 48MP主摄 | 轻薄设计', 5999.00, 150, 1, '/uploads/products/iphone15.jpg', '<p>iPhone 15采用USB-C接口，48MP主摄系统，轻薄设计。</p>', 1, NOW()),
('Xiaomi 14 Ultra', '徕卡光学 | 骁龙8Gen3 | 2K屏幕', 5999.00, 80, 1, '/uploads/products/xiaomi14ultra.jpg', '<p>小米14 Ultra，徕卡光学镜头，骁龙8Gen3处理器。</p>', 1, NOW()),
('华为 Mate 60 Pro', '卫星通信 | 鸿蒙4.0 | 5000万像素', 6999.00, 120, 1, '/uploads/products/mate60pro.jpg', '<p>华为Mate 60 Pro支持卫星通信，搭载鸿蒙4.0系统。</p>', 1, NOW()),
('MacBook Pro 14英寸 M3', 'M3芯片 | 18小时续航 | Liquid Retina XDR显示屏', 12999.00, 50, 1, '/uploads/products/macbookpro14.jpg', '<p>MacBook Pro搭载M3芯片，18小时续航，Liquid Retina XDR显示屏。</p>', 1, NOW()),
('MacBook Air 13英寸 M2', 'M2芯片 | 轻薄便携 | 13.6英寸Liquid视网膜屏', 8999.00, 70, 1, '/uploads/products/macbookair13.jpg', '<p>MacBook Air M2，轻薄便携，13.6英寸Liquid视网膜显示屏。</p>', 1, NOW()),
('iPad Pro 12.9英寸 M2', 'M2芯片 | Liquid视网膜XDR显示屏 | 120Hz', 8999.00, 60, 1, '/uploads/products/ipadpro129.jpg', '<p>iPad Pro 12.9英寸搭载M2芯片，Liquid视网膜XDR显示屏。</p>', 1, NOW()),
('AirPods Pro 2', '主动降噪 | 空间音频 | USB-C充电盒', 1899.00, 200, 1, '/uploads/products/airpodspro2.jpg', '<p>第二代AirPods Pro，升级主动降噪和空间音频。</p>', 1, NOW()),

-- 电脑办公类
('联想拯救者Y9000P', 'i9-13900HX | RTX 4060 | 16英寸电竞屏', 9999.00, 40, 2, '/uploads/products/lenovo-y9000p.jpg', '<p>联想拯救者Y9000P游戏本，i9处理器，RTX 4060显卡。</p>', 1, NOW()),
('戴尔XPS 13 Plus', 'i7-1360P | 16GB内存 | 13.4英寸OLED触控屏', 10999.00, 45, 2, '/uploads/products/dell-xps13.jpg', '<p>戴尔XPS 13 Plus，轻薄本旗舰，OLED触控屏。</p>', 1, NOW()),
('罗技MX Master 3S', '无线鼠标 | 8K传感器 | 静音点击', 699.00, 300, 2, '/uploads/products/logitech-mx3.jpg', '<p>罗技MX Master 3S无线鼠标，8K传感器，静音点击设计。</p>', 1, NOW()),
('Keychron K2机械键盘', '蓝牙双模 | RGB背光 | 红轴', 499.00, 250, 2, '/uploads/products/keychron-k2.jpg', '<p>Keychron K2机械键盘，蓝牙双模，RGB背光。</p>', 1, NOW()),

-- 家用电器类
('小米扫地机器人', '激光导航 | 4000Pa吸力 | 智能规划', 1699.00, 180, 3, '/uploads/products/mijia-robot.jpg', '<p>小米扫地机器人，激光导航，4000Pa大吸力。</p>', 1, NOW()),
('戴森V12 Detect Slim', '激光探测 | 轻量化 | 60分钟续航', 3299.00, 90, 3, '/uploads/products/dyson-v12.jpg', '<p>戴森V12吸尘器，激光探测灰尘，轻量化设计。</p>', 1, NOW()),
('美的1.5匹空调', '一级能效 | 变频 | 智能控制', 2999.00, 150, 3, '/uploads/products/midea-ac.jpg', '<p>美的1.5匹空调，一级能效，变频技术。</p>', 1, NOW()),
('海尔450L十字门冰箱', '双变频 | 净味杀菌 | 智能控温', 4999.00, 70, 3, '/uploads/products/haier-fridge.jpg', '<p>海尔450L冰箱，双变频技术，净味杀菌。</p>', 1, NOW()),
('西门子10kg洗烘一体机', '智能除菌 | 热风除菌 | 1200转', 5999.00, 60, 3, '/uploads/products/siemens-washer.jpg', '<p>西门子洗烘一体机，智能除菌，热风除菌功能。</p>', 1, NOW()),

-- 服装鞋帽类
('Nike Air Max 270', '气垫缓震 | 透气网面 | 时尚百搭', 1099.00, 200, 4, '/uploads/products/nike-airmax270.jpg', '<p>Nike Air Max 270，气垫缓震技术，透气网面设计。</p>', 1, NOW()),
('Adidas Ultraboost 22', 'Boost缓震 | Primeknit鞋面 | 跑步鞋', 1299.00, 180, 4, '/uploads/products/adidas-ultraboost.jpg', '<p>Adidas Ultraboost 22跑步鞋，Boost缓震科技。</p>', 1, NOW()),
('优衣库轻型羽绒服', '轻薄便携 | 保暖防风 | 多色可选', 499.00, 500, 4, '/uploads/products/uniqlo-down.jpg', '<p>优衣库轻型羽绒服，轻薄便携，保暖防风。</p>', 1, NOW()),
('Levi\'s 501牛仔裤', '经典直筒 | 纯棉面料 | 耐穿耐磨', 699.00, 400, 4, '/uploads/products/levis-501.jpg', '<p>Levi\'s 501经典牛仔裤，直筒剪裁。</p>', 1, NOW()),
('The North Face冲锋衣', '防水透气 | Gore-Tex面料 | 户外必备', 1899.00, 150, 4, '/uploads/products/tnf-jacket.jpg', '<p>The North Face冲锋衣，Gore-Tex防水面料。</p>', 1, NOW()),

-- 美妆护肤类
('雅诗兰黛小棕瓶精华', '修护肌肤 | 抗氧化 | 淡化细纹', 899.00, 300, 5, '/uploads/products/estee-lauder-anr.jpg', '<p>雅诗兰黛ANR小棕瓶精华，修护肌肤，抗氧化。</p>', 1, NOW()),
('兰蔻粉水', '温和卸妆 | 补水保湿 | 敏感肌适用', 399.00, 400, 5, '/uploads/products/lancome-toner.jpg', '<p>兰蔻粉水，温和卸妆补水，敏感肌适用。</p>', 1, NOW()),
('SK-II神仙水', 'Pitera成分 | 提亮肤色 | 改善肤质', 1599.00, 200, 5, '/uploads/products/skii-essence.jpg', '<p>SK-II神仙水，含Pitera成分，提亮肤色。</p>', 1, NOW()),
('雅诗兰黛口红', '滋润保湿 | 显色饱满 | 多色可选', 320.00, 500, 5, '/uploads/products/estee-lauder-lipstick.jpg', '<p>雅诗兰黛口红，滋润保湿，显色饱满。</p>', 1, NOW()),

-- 食品饮料类
('星巴克咖啡豆', '阿拉比卡豆 | 深度烘焙 | 1kg装', 299.00, 600, 6, '/uploads/products/starbucks-beans.jpg', '<p>星巴克咖啡豆，100%阿拉比卡，深度烘焙。</p>', 1, NOW()),
('茅台飞天53度', '酱香型 | 53度 | 500ml', 2599.00, 100, 6, '/uploads/products/maotai.jpg', '<p>茅台飞天53度，酱香型白酒，500ml装。</p>', 1, NOW()),
('五常大米有机', '有机种植 | 当季新米 | 5kg装', 199.00, 800, 6, '/uploads/products/wuchang-rice.jpg', '<p>五常有机大米，当季新米，5公斤装。</p>', 1, NOW()),
('费列罗巧克力', '榛果威化 | 进口原料 | 礼盒装', 89.00, 1000, 6, '/uploads/products/ferrero.jpg', '<p>费列罗巧克力，榛果威化，礼盒装。</p>', 1, NOW()),

-- 运动户外类
('迪卡侬公路车', '碳纤维车架 | 22速变速 | 轻量化', 3999.00, 50, 7, '/uploads/products/decathlon-bike.jpg', '<p>迪卡侬公路车，碳纤维车架，22速变速系统。</p>', 1, NOW()),
('Wilson网球拍', '专业级 | 碳素材质 | 进攻型', 899.00, 200, 7, '/uploads/products/wilton-racket.jpg', '<p>Wilson专业网球拍，碳素材质，进攻型设计。</p>', 1, NOW()),
('Nike瑜伽垫', '防滑材质 | 6mm厚度 | 环保无味', 199.00, 500, 7, '/uploads/products/nike-yogamat.jpg', '<p>Nike瑜伽垫，防滑材质，6mm厚度。</p>', 1, NOW()),

-- 图书文具类
('iPad电子书', 'Kindle Paperwhite', '6英寸墨水屏 | 防水设计 | 8GB存储', 998.00, 300, 8, '/uploads/products/kindle.jpg', '<p>Kindle Paperwhite电子书，6英寸墨水屏。</p>', 1, NOW()),
('Lamy钢笔', '德国工艺 | 书写流畅 | 礼盒装', 499.00, 400, 8, '/uploads/products/lamy-pen.jpg', '<p>Lamy钢笔，德国工艺，书写流畅。</p>', 1, NOW()),
('得力订书机', '省力设计 | 加厚底座 | 办公必备', 29.90, 2000, 8, '/uploads/products/deli-stapler.jpg', '<p>得力订书机，省力设计，加厚底座。</p>', 1, NOW()),

-- 母婴用品类
('惠氏启赋奶粉', 'OPO结构脂 | 益生菌 | 3段800g', 398.00, 400, 9, '/uploads/products/wyeth-milk.jpg', '<p>惠氏启赋奶粉3段，OPO结构脂，添加益生菌。</p>', 1, NOW()),
('好奇纸尿裤', '超薄透气 | 柔软亲肤 | M码64片', 159.00, 600, 9, '/uploads/products/huggies.jpg', '<p>好奇纸尿裤，超薄透气，柔软亲肤。</p>', 1, NOW()),
('费雪海马安抚玩偶', '安抚助眠 | 柔顺材质 | 可水洗', 129.00, 500, 9, '/uploads/products/fisher-horse.jpg', '<p>费雪海马安抚玩偶，帮助宝宝安眠。</p>', 1, NOW()),

-- 汽车用品类
('米其林浩悦4代轮胎', '静音舒适 | 耐磨耐用 | 205/55R16', 659.00, 300, 10, '/uploads/products/michelin-tire.jpg', '<p>米其林浩悦4代轮胎，静音舒适，耐磨耐用。</p>', 1, NOW()),
('3M车蜡', '保护车漆 | 提亮光泽 | 300g', 128.00, 800, 10, '/uploads/products/3m-wax.jpg', '<p>3M车蜡，保护车漆，提亮光泽。</p>', 1, NOW()),
(' Garmin导航仪', '4.3英寸屏 | 实时路况 | 语音导航', 999.00, 200, 10, '/uploads/products/garmin-gps.jpg', '<p>Garmin导航仪，4.3英寸屏幕，实时路况。</p>', 1, NOW());

-- 创建商品搜索优化索引
ALTER TABLE pms_product ADD FULLTEXT INDEX ft_name_subtitle (name, subtitle);
ALTER TABLE pms_product ADD INDEX idx_category_status (category_id, status);
ALTER TABLE pms_product ADD INDEX idx_price_range (price);
