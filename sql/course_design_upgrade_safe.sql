-- Safe, repeatable course-design upgrade script.
-- Target database: emart. This script can be run multiple times.

USE emart;

SET @db_name = DATABASE();

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE ums_admin ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT ''CUSTOMER'' COMMENT ''角色：CUSTOMER/SALES/ADMIN'' AFTER status',
    'SELECT ''skip ums_admin.role''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'ums_admin' AND COLUMN_NAME = 'role'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE ums_admin SET role = 'CUSTOMER' WHERE role IS NULL OR role = '';
UPDATE ums_admin SET role = 'ADMIN', status = 1 WHERE username = 'admin';

CREATE TABLE IF NOT EXISTS sys_login_log (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id BIGINT NULL COMMENT '登录用户ID',
  username VARCHAR(64) NOT NULL COMMENT '登录账号',
  role VARCHAR(20) NOT NULL COMMENT '角色',
  login_time DATETIME NOT NULL COMMENT '登录时间',
  ip VARCHAR(64) NULL COMMENT 'IP地址',
  user_agent VARCHAR(500) NULL COMMENT 'User-Agent',
  success TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功：0失败，1成功',
  message VARCHAR(255) NULL COMMENT '登录结果说明',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_username (username),
  KEY idx_role (role),
  KEY idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

CREATE TABLE IF NOT EXISTS sys_operation_log (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  admin_id BIGINT NULL COMMENT '后台账号ID',
  username VARCHAR(64) NOT NULL COMMENT '操作账号',
  role VARCHAR(20) NOT NULL COMMENT '角色',
  operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
  operation_content VARCHAR(500) NOT NULL COMMENT '操作内容',
  request_uri VARCHAR(255) NULL COMMENT '请求路径',
  ip VARCHAR(64) NULL COMMENT 'IP地址',
  create_time DATETIME NOT NULL COMMENT '操作时间',
  PRIMARY KEY (id),
  KEY idx_username (username),
  KEY idx_role (role),
  KEY idx_operation_type (operation_type),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台操作日志表';

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE cms_browse_log ADD COLUMN category_id BIGINT NULL COMMENT ''商品分类ID'' AFTER product_name',
    'SELECT ''skip cms_browse_log.category_id''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'cms_browse_log' AND COLUMN_NAME = 'category_id'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE cms_browse_log ADD COLUMN category_name VARCHAR(100) NULL COMMENT ''商品分类名称'' AFTER category_id',
    'SELECT ''skip cms_browse_log.category_name''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'cms_browse_log' AND COLUMN_NAME = 'category_name'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE cms_browse_log ADD COLUMN duration_seconds INT NULL COMMENT ''停留时长（秒）'' AFTER ip',
    'SELECT ''skip cms_browse_log.duration_seconds''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'cms_browse_log' AND COLUMN_NAME = 'duration_seconds'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE cms_browse_log ADD COLUMN user_agent VARCHAR(500) NULL COMMENT ''User-Agent'' AFTER duration_seconds',
    'SELECT ''skip cms_browse_log.user_agent''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'cms_browse_log' AND COLUMN_NAME = 'user_agent'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = (
  SELECT IF(COUNT(*) = 0,
    'ALTER TABLE cms_browse_log ADD COLUMN leave_time DATETIME NULL COMMENT ''离开时间'' AFTER user_agent',
    'SELECT ''skip cms_browse_log.leave_time''')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db_name AND TABLE_NAME = 'cms_browse_log' AND COLUMN_NAME = 'leave_time'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Normalize demo accounts for screenshots and reports.
-- admin / admin123
UPDATE ums_admin
SET password = '$2b$10$qXmWuDDAMk6PpnXoF97Ay.NtB7IEqRxSwG09uChneBP2fPc36.oGu',
    role = 'ADMIN',
    status = 1
WHERE username = 'admin';

-- sales01 / 123456
INSERT INTO ums_admin (username, password, email, nick_name, note, create_time, status, role)
SELECT 'sales01',
       '$2b$10$B/70FMvQ.6sdahHUcNahFuiXKmevkjvXFGBDGu3eVcaOXF6mzy5uG',
       'sales01@example.com',
       '销售人员01',
       '课程设计测试销售人员',
       NOW(),
       1,
       'SALES'
WHERE NOT EXISTS (SELECT 1 FROM ums_admin WHERE username = 'sales01');

-- customer01 / 123456
INSERT INTO ums_admin (username, password, email, nick_name, note, create_time, status, role)
SELECT 'customer01',
       '$2b$10$B/70FMvQ.6sdahHUcNahFuiXKmevkjvXFGBDGu3eVcaOXF6mzy5uG',
       'customer01@example.com',
       '测试客户01',
       '课程设计测试客户',
       NOW(),
       1,
       'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM ums_admin WHERE username = 'customer01');
