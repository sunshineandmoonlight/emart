# 快速开始 - 推送到 GitHub 并部署

## 第一步：推送到 GitHub

### 1. 在 GitHub 创建新仓库
1. 访问 https://github.com/new
2. 仓库名称：`emart`
3. 选择 Public 或 Private
4. **不要勾选** "Initialize this repository with a README"
5. 点击 "Create repository"

### 2. 添加远程仓库并推送
```bash
# 替换 YOUR_USERNAME 为你的 GitHub 用户名
git remote add origin https://github.com/YOUR_USERNAME/emart.git

# 推送到 GitHub（首次推送）
git push -u origin master

# 或者如果 GitHub 默认分支是 main
git push -u origin main
```

---

## 第二步：部署到云服务器

### 1. 连接到服务器
```bash
ssh root@your_server_ip
```

### 2. 安装 Docker（如果没有安装）
```bash
# Ubuntu/Debian
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 3. 克隆代码
```bash
cd /opt
sudo git clone https://github.com/YOUR_USERNAME/emart.git
cd emart
```

### 4. 配置环境变量
```bash
# 复制环境变量模板
cp .env.example .env

# 编辑配置（必须修改默认密码！）
nano .env
```

**重要配置项：**
```env
# 数据库密码（必须修改）
MYSQL_ROOT_PASSWORD=your_secure_password
DB_PASSWORD=your_secure_password

# JWT 密钥（必须修改）
JWT_SECRET=your-random-secret-key

# 可选：邮件配置
MAIL_USERNAME=your-email@qq.com
MAIL_PASSWORD=your-mail-auth-code
```

### 5. 启动服务
```bash
# 构建并启动（后台运行）
docker-compose up -d --build

# 查看日志
docker-compose logs -f

# 等待所有服务启动完成（约 2-3 分钟）
# 按 Ctrl+C 退出日志查看
```

### 6. 验证部署
```bash
# 检查容器状态
docker-compose ps

# 应该看到以下服务都是 Up 状态：
# emart-app
# emart-mysql
# emart-redis

# 测试应用
curl http://localhost:8080/actuator/health

# 访问 Swagger 文档（在浏览器中）
# http://your_server_ip:8080/swagger-ui.html
```

---

## 常用命令

### 服务管理
```bash
# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看日志
docker-compose logs -f app

# 进入容器
docker-compose exec app bash
```

### 更新代码
```bash
# 拉取最新代码
git pull origin master

# 重新构建并启动
docker-compose up -d --build
```

### 数据备份
```bash
# 备份数据库
docker-compose exec mysql mysqldump -uemart -p emart > backup_$(date +%Y%m%d).sql
```

---

## 访问应用

部署成功后，可以通过以下方式访问：

- **应用主页**: http://your_server_ip:8080
- **Swagger API 文档**: http://your_server_ip:8080/swagger-ui.html
- **健康检查**: http://your_server_ip:8080/actuator/health

---

## 需要帮助？

查看完整部署文档：`DEPLOYMENT.md`

包含：
- 详细的故障排查指南
- Nginx 反向代理配置
- 安全加固建议
- 性能优化建议

---

## 下一步

1. ✅ 修改所有默认密码
2. ✅ 配置防火墙规则
3. ✅ 设置数据库定期备份
4. ✅ 配置域名和 HTTPS（可选）
5. ✅ 设置监控告警（可选）

---

**祝部署顺利！** 🚀
