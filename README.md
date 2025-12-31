# Emart 电商平台 🛒

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)
![Vue.js](https://img.shields.io/badge/Vue.js-3.5.24-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Redis](https://img.shields.io/badge/Redis-7.x-red)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)

一个功能完整的现代化B2C电商平台，采用前后端分离架构，支持商品管理、购物车、订单支付、用户管理等核心功能。

[在线演示](http://106.53.173.173) • [API文档](http://106.53.173.173/swagger-ui.html) • [实验报告](./实验报告.md)

</div>

---

## 📋 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [系统架构](#系统架构)
- [快速开始](#快速开始)
- [本地开发](#本地开发)
- [服务器部署](#服务器部署)
- [项目截图](#项目截图)
- [API文档](#api文档)
- [常见问题](#常见问题)
- [许可证](#许可证)

---

## ✨ 功能特性

### 🛍️ 用户端功能
- **用户认证**：注册、登录、JWT令牌认证
- **商品浏览**：商品列表、分类筛选、关键词搜索
- **商品详情**：规格选择、库存显示、商品评价
- **购物车**：添加商品、修改数量、删除商品
- **订单管理**：创建订单、优惠券抵扣、在线支付
- **个人中心**：订单查询、收货地址管理

### 👔 管理后台功能
- **数据统计**：销售额、订单数、用户数可视化看板
- **商品管理**：商品CRUD、分类管理、库存管理
- **订单管理**：订单查询、状态更新、发货处理
- **用户管理**：用户列表、权限管理
- **营销管理**：优惠券创建、使用记录

### 🔧 核心技术特性
- **Redis缓存**：购物车数据缓存，提升查询性能
- **异步支付**：集成支付宝沙箱环境
- **邮件通知**：支付成功邮件提醒
- **响应式设计**：支持桌面、平板、手机
- **RESTful API**：标准化的API接口设计

---

## 🛠️ 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis-Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0 | 关系数据库 |
| Redis | 7.x | 缓存数据库 |
| Spring Security | - | 安全框架 |
| JWT | jjwt 0.9.1 | 令牌认证 |
| Swagger | SpringDoc 1.7.0 | API文档 |
| Druid | 1.2.20 | 数据库连接池 |

### 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.5.24 | 前端框架 |
| Element Plus | 2.13.0 | UI组件库 |
| Vite | 7.2.4 | 构建工具 |
| Axios | 1.13.2 | HTTP客户端 |
| ECharts | 5.x | 数据可视化 |

### 部署技术
- **Docker** + **Docker Compose**：容器化部署
- **Nginx**：反向代理、静态文件托管

---

## 🏗️ 系统架构

```
┌─────────────────────────────────────────┐
│         前端（Vue 3 + Element Plus）      │
│   用户端(emart-web) + 管理端(emart-admin) │
└─────────────────────────────────────────┘
                    ↕ HTTP/AJAX
┌─────────────────────────────────────────┐
│         后端（Spring Boot + JWT）         │
│        Controller → Service → Mapper      │
└─────────────────────────────────────────┘
                    ↕
┌─────────────────────────────────────────┐
│       数据层（MySQL 8.0 + Redis 7.x）     │
│    持久化存储 + 缓存（购物车、浏览记录）     │
└─────────────────────────────────────────┘
```

### 核心模块划分
- **UMS**：用户管理模块（User Management System）
- **PMS**：商品管理模块（Product Management System）
- **OMS**：订单管理模块（Order Management System）
- **CMS**：内容管理模块（Content Management System）
- **SMS**：营销模块（Sales Management System）

---

## 🚀 快速开始

### 环境要求
- **JDK** 17+
- **Node.js** 18+
- **MySQL** 8.0
- **Redis** 7.x
- **Maven** 3.8+

### 一键启动（使用Docker）

```bash
# 1. 克隆项目
git clone https://github.com/sunshineandmoonlight/emart.git
cd emart

# 2. 启动MySQL和Redis
docker-compose up -d mysql redis

# 3. 导入数据库
docker exec -i emart-mysql mysql -uroot -pC20041109 emart < emart_clean_backup.sql

# 4. 启动后端
./mvnw spring-boot:run

# 5. 启动前端用户端
cd emart-web && npm run dev

# 6. 启动前端管理端
cd emart-admin && npm run dev
```

### 访问地址
- **用户端**：http://localhost:5173
- **管理后台**：http://localhost:5174
- **API文档**：http://localhost:8080/swagger-ui.html

---

## 💻 本地开发

### 详细步骤

请参考 👉 [**本地运行指南.md**](./本地运行指南.md)

**主要内容包括**：
- 环境准备（JDK、Maven、Node.js安装）
- 数据库和Redis配置
- 后端启动（3种方式）
- 前端启动步骤
- 8个常见问题解决方案

### 核心配置文件

**后端配置**：`src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emart
    username: root
    password: C20041109  # 修改为您的密码
  redis:
    host: localhost
    port: 6379
```

**前端配置**：`emart-web/src/utils/request.js`
```javascript
const request = axios.create({
  baseURL: '/api',  // 使用Nginx代理或本地开发代理
  timeout: 5000
})
```

---

## 🌐 服务器部署

### 完整部署指南

请参考 👉 [**服务器完整部署指南.md**](./服务器完整部署指南.md)

**部署方式**：
1. **补充数据库**（适合已有容器运行）
2. **完整重新部署**（从头开始）

**生产环境架构**：
```
Internet
    ↓
Nginx (80端口)
    ├─→ / → 前端静态文件 (Vue dist)
    ├─→ /api → 后端API (8080端口)
    └─→ /swagger-ui → API文档
```

### 快速部署命令

```bash
# 1. 克隆代码到服务器
git clone https://github.com/sunshineandmoonlight/emart.git /opt/emart
cd /opt/emart

# 2. 配置环境变量
cp .env.example .env
nano .env  # 修改密码等配置

# 3. 启动所有服务
docker-compose up -d --build

# 4. 导入数据库
docker exec -i emart-mysql mysql -uroot -p密码 emart < emart_clean_backup.sql

# 5. 构建前端
cd emart-web && npm install && npm run build

# 6. 配置Nginx（已配置好）
# 重启Nginx: sudo nginx -s reload
```

**在线演示**：http://106.53.173.173

---

## 📸 项目截图

### 用户端
- **首页**：商品列表展示，分类筛选
- **商品详情**：规格选择、库存显示
- **购物车**：商品数量修改、总价计算
- **订单确认**：优惠券使用、地址选择
- **支付页面**：支付宝沙箱支付

### 管理后台
- **数据看板**：销售趋势图、热门商品排行
- **商品管理**：商品列表、新增/编辑商品
- **订单管理**：订单列表、状态更新

---

## 📚 API文档

### Swagger UI

启动后端后访问：http://localhost:8080/swagger-ui.html

### 核心API接口

#### 用户认证
```http
POST /user/register    # 用户注册
POST /user/login       # 用户登录（返回JWT令牌）
GET  /user/info        # 获取用户信息
```

#### 商品管理
```http
GET  /product/list           # 获取商品列表（分页）
GET  /product/{id}           # 获取商品详情
GET  /category/listAll       # 获取所有分类
GET  /product/search         # 搜索商品
```

#### 购物车
```http
POST /cart/add           # 添加到购物车
GET  /cart/list          # 获取购物车列表
PUT  /cart/update/{id}   # 更新购物车
DELETE /cart/delete/{id} # 删除购物车商品
```

#### 订单管理
```http
POST /order/create       # 创建订单
GET  /order/list         # 获取订单列表
GET  /order/{id}         # 获取订单详情
```

### API响应格式

**成功响应**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

**失败响应**：
```json
{
  "code": 500,
  "message": "错误描述",
  "data": null
}
```

---

## ❓ 常见问题

### 1. 后端启动失败 - 连接MySQL失败
**错误**：`Communications link failure`

**解决方案**：
- 检查MySQL是否启动：`docker-compose ps`
- 确认密码是否正确
- 测试连接：`mysql -hlocalhost -uroot -p`

### 2. 前端无法调用后端API（跨域）
**错误**：`Blocked by CORS policy`

**解决方案**：
- 确保 `request.js` 中 `baseURL` 使用 `/api`
- 检查后端 `CorsConfig.java` 配置
- 或使用Vite代理（`vite.config.js`）

### 3. 支付功能无法使用
**说明**：支付宝沙箱需要配置真实的AppId和密钥

**临时方案**：直接在数据库修改订单状态为"已支付"

### 4. 数据库为空，没有商品数据
**解决方案**：
```bash
docker exec -i emart-mysql mysql -uroot -pC20041109 emart < emart_clean_backup.sql
```

更多问题请查看 [本地运行指南.md](./本地运行指南.md) 或 [服务器完整部署指南.md](./服务器完整部署指南.md)

---

## 📖 相关文档

- [**本地运行指南**](./本地运行指南.md) - 详细的环境搭建和开发步骤
- [**服务器部署指南**](./服务器完整部署指南.md) - 生产环境完整部署教程
- [**实验报告**](./实验报告.md) - 系统设计、代码实现、功能测试
- [**CLAUDE.md**](./CLAUDE.md) - 项目开发指导（Claude Code使用）

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👨‍💻 作者

** sunshineandmoonlight**

- GitHub：[@sunshineandmoonlight](https://github.com/sunshineandmoonlight)
- 项目链接：[https://github.com/sunshineandmoonlight/emart](https://github.com/sunshineandmoonlight/emart)

---

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java后端框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - 优秀的Vue 3组件库
- [MyBatis-Plus](https://baomidou.com/) - 强大的MyBatis增强工具

---

## 📊 项目统计

![GitHub Stars](https://img.shields.io/github/stars/sunshineandmoonlight/emart?style=social)
![GitHub Forks](https://img.shields.io/github/forks/sunshineandmoonlight/emart?style=social)
![GitHub Issues](https://img.shields.io/github/issues/sunshineandmoonlight/emart)

---

<div align="center">

**如果觉得项目不错，请给个 ⭐️ Star 支持一下！**

Made with ❤️ by sunshineandmoonlight

</div>
