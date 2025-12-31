-- 优化商品数据和分类
-- 1. 修正分类为正常的电商分类
-- 2. 删除现有商品
-- 3. 插入均衡的商品数据（每个分类5-10个）
-- 4. 添加商品SKU编号

-- 清空现有商品数据
DELETE FROM pms_product;

-- 更新分类为电商分类
UPDATE pms_category SET name = '手机数码' WHERE id = 1;
UPDATE pms_category SET name = '电脑办公' WHERE id = 2;
UPDATE pms_category SET name = '家用电器' WHERE id = 3;
UPDATE pms_category SET name = '服装鞋帽' WHERE id = 4;
UPDATE pms_category SET name = '美妆护肤' WHERE id = 5;
UPDATE pms_category SET name = '食品饮料' WHERE id = 6;
UPDATE pms_category SET name = '运动户外' WHERE id = 7;
UPDATE pms_category SET name = '家居生活' WHERE id = 8;
UPDATE pms_category SET name = '母婴用品' WHERE id = 9;
UPDATE pms_category SET name = '汽车用品' WHERE id = 10;

-- 删除第11个分类（如果存在）
DELETE FROM pms_category WHERE id > 10;

-- 插入均衡的商品数据（每个分类6个，共60个商品）
INSERT INTO pms_product (name, subtitle, price, stock, category_id, brand, image, status, create_time) VALUES

-- ===== 手机数码 (6个) =====
('Apple iPhone 15 Pro Max', 'A17 Pro芯片 钛金属 6.7英寸', 9999.00, 100, 1, 'Apple', 'https://via.placeholder.com/400x400/667eea/ffffff?text=iPhone+15+Pro+Max', 1, NOW()),
('Apple iPhone 15', 'USB-C接口 48MP相机 6.1英寸', 5999.00, 150, 1, 'Apple', 'https://via.placeholder.com/400x400/f093fb/ffffff?text=iPhone+15', 1, NOW()),
('Xiaomi 14 Ultra', '徕卡相机 骁龙8Gen3 2K屏幕', 5999.00, 80, 1, '小米', 'https://via.placeholder.com/400x400/4facfe/ffffff?text=Xiaomi+14', 1, NOW()),
('华为 Mate 60 Pro', '卫星通信 鸿蒙4.0 5000万', 6999.00, 120, 1, '华为', 'https://via.placeholder.com/400x400/00f2fe/ffffff?text=Mate+60', 1, NOW()),
('Apple MacBook Pro 14英寸', 'M3芯片 18小时续航', 12999.00, 50, 1, 'Apple', 'https://via.placeholder.com/400x400/fee140/ffffff?text=MacBook+Pro', 1, NOW()),
('Apple AirPods Pro 2', '主动降噪 空间音频', 1899.00, 200, 1, 'Apple', 'https://via.placeholder.com/400x400/fa709a/ffffff?text=AirPods', 1, NOW()),

-- ===== 电脑办公 (6个) =====
('联想拯救者Y9000P', 'i9 RTX4060 16寸电竞', 9999.00, 40, 2, '联想', 'https://via.placeholder.com/400x400/a8edea/ffffff?text=Lenovo+Y9000P', 1, NOW()),
('戴尔XPS 13 Plus', 'i7 16GB OLED触控', 10999.00, 45, 2, '戴尔', 'https://via.placeholder.com/400x400/fed6e3/ffffff?text=Dell+XPS', 1, NOW()),
('罗技MX Master 3S', '无线鼠标 8K传感器', 699.00, 300, 2, '罗技', 'https://via.placeholder.com/400x400/f368e0/ffffff?text=Logitech+MX', 1, NOW()),
('Keychron K2机械键盘', '蓝牙双模 RGB背光', 499.00, 250, 2, 'Keychron', 'https://via.placeholder.com/400x400/ff9f1a/ffffff?text=Keychron+K2', 1, NOW()),
('惠普战66 Pro', 'R7 16GB 512GB 办公本', 4999.00, 60, 2, '惠普', 'https://via.placeholder.com/400x400/00d2d3/ffffff?text=HP+Laptop', 1, NOW()),
('三星27寸4K显示器', 'IPS面板 Type-C接口', 2499.00, 80, 2, '三星', 'https://via.placeholder.com/400x400/54a0ff/ffffff?text=Samsung+Monitor', 1, NOW()),

-- ===== 家用电器 (6个) =====
('小米扫地机器人', '激光导航 4000Pa吸力', 1699.00, 180, 3, '小米', 'https://via.placeholder.com/400x400/5f27cd/ffffff?text=Xiaomi+Robot', 1, NOW()),
('戴森V12 Detect', '激光探测 轻量化 60分钟', 3299.00, 90, 3, '戴森', 'https://via.placeholder.com/400x400/00d2d3/ffffff?text=Dyson+V12', 1, NOW()),
('美的1.5匹空调', '一级能效 变频', 2999.00, 150, 3, '美的', 'https://via.placeholder.com/400x400/341f97/ffffff?text=Midea+AC', 1, NOW()),
('海尔450L冰箱', '十字门 双变频', 4999.00, 70, 3, '海尔', 'https://via.placeholder.com/400x400/f0932d/ffffff?text=Haier+Fridge', 1, NOW()),
('西门子洗烘一体机', '10kg 智能除菌', 5999.00, 60, 3, '西门子', 'https://via.placeholder.com/400x400/6c5ce7/ffffff?text=Siemens+Washer', 1, NOW()),
('飞利浦空气炸锅', '4.5L 数字触控', 699.00, 200, 3, '飞利浦', 'https://via.placeholder.com/400x400/ffd93d/ffffff?text=Philips+Fryer', 1, NOW()),

-- ===== 服装鞋帽 (6个) =====
('Nike Air Max 270', '气垫缓震 透气网面', 1099.00, 200, 4, 'Nike', 'https://via.placeholder.com/400x400/00cec9/ffffff?text=Nike+AirMax', 1, NOW()),
('Adidas Ultraboost 22', 'Boost缓震 Primeknit', 1299.00, 180, 4, 'Adidas', 'https://via.placeholder.com/400x400/ff7675/ffffff?text=Ultraboost', 1, NOW()),
('优衣库轻型羽绒服', '轻薄便携 防水防风', 499.00, 500, 4, '优衣库', 'https://via.placeholder.com/400x400/ffa502/ffffff?text=Uniqlo+Down', 1, NOW()),
('Levi\'s 501牛仔裤', '经典直筒 纯棉', 699.00, 400, 4, 'Levi\'s', 'https://via.placeholder.com/400x400/1dd1a1/ffffff?text=Levis+501', 1, NOW()),
('The North Face冲锋衣', '防水透气 Gore-Tex', 1899.00, 150, 4, 'The North Face', 'https://via.placeholder.com/400x400/a29bfe/ffffff?text=TNF+Jacket', 1, NOW()),
('Converse匡威帆布鞋', '经典款 百搭舒适', 399.00, 300, 4, 'Converse', 'https://via.placeholder.com/400x400/5f27cd/ffffff?text=Converse', 1, NOW()),

-- ===== 美妆护肤 (6个) =====
('雅诗兰黛小棕瓶', '修护精华 抗氧化', 899.00, 300, 5, '雅诗兰黛', 'https://via.placeholder.com/400x400/667eea/ffffff?text=ANR+Serum', 1, NOW()),
('兰蔻粉水', '温和卸妆 补水保湿', 399.00, 400, 5, '兰蔻', 'https://via.placeholder.com/400x400/f093fb/ffffff?text=Lancome', 1, NOW()),
('SK-II神仙水', 'Pitera 提亮肤色', 1599.00, 200, 5, 'SK-II', 'https://via.placeholder.com/400x400/4facfe/ffffff?text=SK2', 1, NOW()),
('雅诗兰黛口红', '滋润保湿 显色饱满', 320.00, 500, 5, '雅诗兰黛', 'https://via.placeholder.com/400x400/00f2fe/ffffff?text=Lipstick', 1, NOW()),
('资生堂红腰子', '抗皱精华 紧致肌肤', 1299.00, 250, 5, '资生堂', 'https://via.placeholder.com/400x400/fee140/ffffff?text=Shiseido', 1, NOW()),
('科颜氏保湿面霜', '高效保湿 不油腻', 520.00, 350, 5, '科颜氏', 'https://via.placeholder.com/400x400/fa709a/ffffff?text=Kiehls', 1, NOW()),

-- ===== 食品饮料 (6个) =====
('星巴克咖啡豆', '阿拉比卡 1kg装', 299.00, 600, 6, '星巴克', 'https://via.placeholder.com/400x400/a8edea/ffffff?text=Starbucks', 1, NOW()),
('茅台飞天53度', '酱香型 500ml', 2599.00, 100, 6, '茅台', 'https://via.placeholder.com/400x400/fed6e3/ffffff?text=Moutai', 1, NOW()),
('五常有机大米', '当季新米 5kg', 199.00, 800, 6, '五常', 'https://via.placeholder.com/400x400/f368e0/ffffff?text=Wuchang+Rice', 1, NOW()),
('费列罗巧克力', '榛果威化 礼盒', 89.00, 1000, 6, '费列罗', 'https://via.placeholder.com/400x400/ff9f1a/ffffff?text=Ferrero', 1, NOW()),
('旺旺大礼包', '经典牛奶味 400g', 59.00, 2000, 6, '旺旺', 'https://via.placeholder.com/400x400/00d2d3/ffffff?text=WangWang', 1, NOW()),
('乐事薯片大礼包', '多口味组合 600g', 39.90, 1500, 6, '乐事', 'https://via.placeholder.com/400x400/54a0ff/ffffff?text=Lays', 1, NOW()),

-- ===== 运动户外 (6个) =====
('迪卡侬公路车', '碳纤维 22速变速', 3999.00, 50, 7, '迪卡侬', 'https://via.placeholder.com/400x400/5f27cd/ffffff?text=Road+Bike', 1, NOW()),
('Wilson网球拍', '专业级 碳素进攻', 899.00, 200, 7, 'Wilson', 'https://via.placeholder.com/400x400/00cec9/ffffff?text=Tennis', 1, NOW()),
('Nike瑜伽垫', '防滑材质 6mm', 199.00, 500, 7, 'Nike', 'https://via.placeholder.com/400x400/ff7675/ffffff?text=Yoga+Mat', 1, NOW()),
('Northface登山包', '50L 防水大容量', 799.00, 150, 7, 'The North Face', 'https://via.placeholder.com/400x400/ffa502/ffffff?text=Backpack', 1, NOW()),
('Adidas运动水壶', '1.5L 便携式', 99.00, 800, 7, 'Adidas', 'https://via.placeholder.com/400x400/1dd1a1/ffffff?text=Water+Bottle', 1, NOW()),
('斯伯恩篮球', '7号 室外耐磨', 129.00, 600, 7, '斯伯恩', 'https://via.placeholder.com/400x400/a29bfe/ffffff?text=Basketball', 1, NOW()),

-- ===== 家居生活 (6个) =====
('宜家台灯', 'LED护眼 可调光', 199.00, 500, 8, '宜家', 'https://via.placeholder.com/400x400/6c5ce7/ffffff?text=Lamp', 1, NOW()),
('无印良品收纳箱', '简约 3件套', 159.00, 800, 8, '无印良品', 'https://via.placeholder.com/400x400/f0932d/ffffff?text=Storage', 1, NOW()),
('记忆棉枕头', '慢回弹 透气', 199.00, 600, 8, '记忆棉', 'https://via.placeholder.com/400x400/ffd93d/ffffff?text=Pillow', 1, NOW()),
('乳胶床垫', '1.8m 双人 20cm厚', 2999.00, 50, 8, '雅兰', 'https://via.placeholder.com/400x400/341f97/ffffff?text=Mattress', 1, NOW()),
('博洋四件套', '纯棉床上用品', 499.00, 400, 8, '博洋', 'https://via.placeholder.com/400x400/00d2d3/ffffff?text=Bedding', 1, NOW()),
('苏泊尔电饭煲', '4L 智能预约', 399.00, 300, 8, '苏泊尔', 'https://via.placeholder.com/400x400/54a0ff/ffffff?text=Rice+Cooker', 1, NOW()),

-- ===== 母婴用品 (6个) =====
('惠氏启赋奶粉', 'OPO益生菌 3段800g', 398.00, 400, 9, '惠氏', 'https://via.placeholder.com/400x400/ffa502/ffffff?text=Wyeth', 1, NOW()),
('好奇纸尿裤', '超薄透气 M码64片', 159.00, 600, 9, '好奇', 'https://via.placeholder.com/400x400/5f27cd/ffffff?text=Huggies', 1, NOW()),
('费雪海马安抚', '助眠 可水洗', 129.00, 500, 9, '费雪', 'https://via.placeholder.com/400x400/00cec9/ffffff?text=Fisher+Horse', 1, NOW()),
('美德乐吸奶器', '双边电动 便携', 599.00, 200, 9, '美德乐', 'https://via.placeholder.com/400x400/ff7675/ffffff?text=Pump', 1, NOW()),
('贝亲安抚奶嘴', '硅胶材质 0-6月', 89.00, 1000, 9, '贝亲', 'https://via.placeholder.com/400x400/1dd1a1/ffffff?text=Pacifier', 1, NOW()),
('好孩子婴儿床', '多功能 可折叠', 1599.00, 80, 9, '好孩子', 'https://via.placeholder.com/400x400/a29bfe/ffffff?text=Crib', 1, NOW()),

-- ===== 汽车用品 (6个) =====
('米其林浩悦4代', '静音舒适 205/55R16', 659.00, 300, 10, '米其林', 'https://via.placeholder.com/400x400/ff9f1a/ffffff?text=Michelin', 1, NOW()),
('3M车蜡', '保护车漆 300g', 128.00, 800, 10, '3M', 'https://via.placeholder.com/400x400/667eea/ffffff?text=3M+Wax', 1, NOW()),
('Garmin导航仪', '4.3寸 实时路况', 999.00, 200, 10, 'Garmin', 'https://via.placeholder.com/400x400/f093fb/ffffff?text=GPS', 1, NOW()),
('3M脚垫', '全包围 TPR材质', 199.00, 500, 10, '3M', 'https://via.placeholder.com/400x400/4facfe/ffffff?text=Mat', 1, NOW()),
('固特异轮胎', '185/65R15 耐用', 459.00, 400, 10, '固特异', 'https://via.placeholder.com/400x400/00f2fe/ffffff?text=Goodyear', 1, NOW()),
('博世雨刮器', '无骨静音 一对', 89.00, 1000, 10, '博世', 'https://via.placeholder.com/400x400/fee140/ffffff?text=Wiper', 1, NOW());

-- 为所有商品生成商品编号（格式：分类码-序号）
-- 注意：product_code字段已存在，直接更新即可
UPDATE pms_product SET product_code = CONCAT('SD', LPAD(category_id, 2, '0'), LPAD(id, 3, '0')) WHERE product_code IS NULL;
