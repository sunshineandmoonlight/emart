# Emart 部署指南

本文档介绍如何将 Emart 项目推送到 GitHub 并使用 Docker 部署到云服务器。

## 目录
1. [推送到 GitHub](#推送到-github)
2. [服务器准备](#服务器准备)
3. [Docker 部署](#docker-部署)
4. [常用命令](#常用命令)
5. [故障排查](#故障排查)

---

## 推送到 GitHub

### 1. 在 GitHub 上创建新仓库

1. 访问 [GitHub](https://github.com)
2. 点击右上角的 `+` → `New repository`
3. 填写仓库名称（例如：`emart`）
4. 选择 Public 或 Private
5. **不要**勾选 "Initialize this repository with a README"
6. 点击 "Create repository"

### 2. 配置本地 Git 并推送

在项目根目录执行以下命令：

```bash
# 添加所有文件到暂存区
git add .

# 提交
git commit -m "Initial commit: Emart e-commerce platform"

# 添加远程仓库（替换 YOUR_USERNAME 为你的 GitHub 用户名）
git remote add origin https://github.com/YOUR_USERNAME/emart.git

# 推送到 GitHub（首次推送）
git push -u origin master

# 或者使用 main 分支
git push -u origin main
```

### 3. 配置 .env 文件（不要提交）

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，填入实际的配置值
# 重要：.env 文件已在 .gitignore 中，不会被提交到 Git
```

---

## 服务器准备

### 系统要求

- **操作系统**: Linux (推荐 Ubuntu 20.04+ 或 CentOS 7+)
- **内存**: 至少 2GB RAM
- **磁盘**: 至少 20GB 可用空间
- **端口**: 确保以下端口未被占用
  - `8080` - 应用端口
  - `3306` - MySQL（可选，如果不对外暴露）
  - `6379` - Redis（可选，如果不对外暴露）

### 安装 Docker 和 Docker Compose

#### Ubuntu/Debian

```bash
# 更新包索引
sudo apt-get update

# 安装依赖
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# 添加 Docker 官方 GPG 密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 设置 Docker 仓库
echo \
  "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装 Docker
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version

# 将当前用户添加到 docker 组（可选，避免每次使用 sudo）
sudo usermod -aG docker $USER
newgrp docker
```

#### CentOS/RHEL

```bash
# 安装 Docker
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo yum install -y docker-ce docker-ce-cli containerd.io

# 启动 Docker
sudo systemctl start docker
sudo systemctl enable docker

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version
```

### 配置防火墙

```bash
# Ubuntu (UFW)
sudo ufw allow 8080/tcp
sudo ufw allow 22/tcp
sudo ufw enable

# CentOS (firewalld)
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=22/tcp
sudo firewall-cmd --reload
```

---

## Docker 部署

### 1. 克隆代码到服务器

```bash
# 使用 HTTPS（推荐）
cd /opt
sudo git clone https://github.com/YOUR_USERNAME/emart.git
cd emart

# 或使用 SSH（需要配置 SSH 密钥）
# sudo git clone git@github.com:YOUR_USERNAME/emart.git
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量（重要：修改默认密码！）
nano .env
```

**必须修改的配置项：**
- `MYSQL_ROOT_PASSWORD` - MySQL root 密码
- `DB_PASSWORD` - 数据库用户密码
- `JWT_SECRET` - JWT 密钥（使用随机字符串）

**可选配置项：**
- `MAIL_USERNAME` / `MAIL_PASSWORD` - 邮件服务配置
- `APP_PORT` - 应用端口（默认 8080）

### 3. 准备数据库初始化脚本

如果您的数据库表结构已经创建，可以将其 SQL 脚本放到 `docker/mysql/init/` 目录：

```bash
# 示例：创建初始化脚本
nano docker/mysql/init/01-init.sql
```

示例 SQL 脚本内容：
```sql
-- 创建用户表
CREATE TABLE IF NOT EXISTS ums_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128),
    status INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 添加更多表...
```

### 4. 构建并启动服务

```bash
# 构建镜像并启动所有服务（后台运行）
docker-compose up -d --build

# 查看日志
docker-compose logs -f

# 查看特定服务的日志
docker-compose logs -f app
docker-compose logs -f mysql
```

### 5. 验证部署

```bash
# 检查容器状态
docker-compose ps

# 应该看到以下服务都在运行：
# - emart-app
# - emart-mysql
# - emart-redis

# 测试应用健康检查
curl http://localhost:8080/actuator/health

# 测试 API（在另一台机器或浏览器访问）
# http://YOUR_SERVER_IP:8080/swagger-ui.html
```

---

## 常用命令

### 服务管理

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose down

# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart app

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f [service_name]

# 进入容器
docker-compose exec app bash
docker-compose exec mysql mysql -uemart -p
```

### 更新部署

```bash
# 拉取最新代码
git pull origin master

# 重新构建并启动
docker-compose up -d --build

# 查看新日志
docker-compose logs -f
```

### 数据备份

```bash
# 备份 MySQL 数据
docker-compose exec mysql mysqldump -uemart -pemart123 emart > backup_$(date +%Y%m%d_%H%M%S).sql

# 恢复 MySQL 数据
docker-compose exec -T mysql mysql -uemart -pemart123 emart < backup_20250101_120000.sql
```

### 清理和重置

```bash
# 停止并删除所有容器、网络、匿名卷
docker-compose down -v

# 删除所有镜像
docker rmi $(docker images -q emart-*)

# 完全清理（包括数据卷，慎用！）
docker-compose down -v
docker system prune -a
```

---

## 故障排查

### 1. 容器无法启动

```bash
# 查看详细日志
docker-compose logs [service_name]

# 检查容器状态
docker-compose ps

# 检查资源使用
docker stats
```

### 2. 数据库连接失败

```bash
# 检查 MySQL 容器是否健康
docker-compose ps mysql

# 进入 MySQL 容器测试连接
docker-compose exec mysql mysql -uemart -p

# 检查网络连接
docker-compose exec app ping mysql
```

### 3. 端口冲突

```bash
# 检查端口占用
sudo netstat -tulpn | grep :8080

# 修改 .env 文件中的 APP_PORT
nano .env

# 重启服务
docker-compose up -d
```

### 4. 应用启动失败

```bash
# 查看应用日志
docker-compose logs app

# 常见问题：
# - 内存不足：增加服务器内存或减少 JVM 堆内存
# - 数据库未就绪：确保 MySQL 容器健康后再启动应用
# - 配置错误：检查 .env 文件配置

# 进入应用容器检查
docker-compose exec app bash
```

### 5. 性能问题

```bash
# 查看容器资源使用
docker stats

# 查看 MySQL 性能
docker-compose exec mysql mysql -e "SHOW PROCESSLIST;"

# 清理未使用的 Docker 资源
docker system prune
```

---

## 生产环境建议

### 1. 安全加固

- ✅ 修改所有默认密码
- ✅ 使用强密码和随机 JWT_SECRET
- ✅ 不要暴露 MySQL 和 Redis 端口到公网
- ✅ 配置 HTTPS（使用 Nginx 反向代理）
- ✅ 定期更新系统和 Docker 版本

### 2. 数据备份

- 设置定时任务自动备份数据库
- 备份到云存储或远程服务器
- 定期测试恢复流程

### 3. 监控

- 配置日志收集（如 ELK Stack）
- 设置容器健康检查
- 配置监控告警（如 Prometheus + Grafana）

### 4. 扩展性

- 使用 Docker Swarm 或 Kubernetes 进行多实例部署
- 配置负载均衡
- 使用外部云数据库服务

---

## 配置 Nginx 反向代理（推荐）

如果您的服务器有域名，可以配置 Nginx 提供 HTTPS 访问：

```bash
# 安装 Nginx
sudo apt-get install -y nginx

# 创建配置文件
sudo nano /etc/nginx/sites-available/emart
```

Nginx 配置示例：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

启用配置并重启 Nginx：

```bash
sudo ln -s /etc/nginx/sites-available/emart /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## 获取帮助

如遇到问题：
1. 查看本文档的故障排查部分
2. 检查 Docker 日志：`docker-compose logs`
3. 查看 GitHub Issues
4. 联系技术支持

---

**祝部署顺利！** 🚀
