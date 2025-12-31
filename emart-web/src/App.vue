<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header height="70px" class="header">
        <div class="header-content">
          <!-- Logo -->
          <div class="logo" @click="$router.push('/')" style="cursor: pointer;">
            <h1 style="margin: 0; font-size: 24px; color: #fff;">Emart</h1>
          </div>

          <!-- 导航菜单 -->
          <el-menu
            mode="horizontal"
            :default-active="activeMenu"
            class="nav-menu"
            router
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/products">商品列表</el-menu-item>
            <el-menu-item index="/cart">
              <el-badge :value="cartCount" :hidden="cartCount === 0" class="cart-badge">
                购物车
              </el-badge>
            </el-menu-item>
            <el-menu-item index="/order">我的订单</el-menu-item>
            <el-menu-item index="/profile">个人中心</el-menu-item>
          </el-menu>

          <!-- 搜索框 -->
          <div class="search-bar">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索商品"
              prefix-icon="Search"
              @keyup.enter="handleSearch"
              class="search-input"
            ></el-input>
            <el-button type="warning" @click="handleSearch">搜索</el-button>
          </div>

          <!-- 用户信息 -->
          <div class="user-info">
            <template v-if="!username">
              <el-button type="primary" @click="$router.push('/login')">登录</el-button>
              <el-button @click="$router.push('/register')">注册</el-button>
            </template>
            <template v-else>
              <el-dropdown>
                <span class="username">
                  {{ username }} <el-icon><ArrowDown /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                    <el-dropdown-item @click="$router.push('/order')">我的订单</el-dropdown-item>
                    <el-dropdown-item @click="$router.push('/favorite')">我的收藏</el-dropdown-item>
                    <el-dropdown-item @click="$router.push('/my-coupons')">我的优惠券</el-dropdown-item>
                    <el-dropdown-item divided @click="goToAdmin">管理员后台</el-dropdown-item>
                    <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </div>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>

      <!-- 底部 -->
      <el-footer height="200px" class="footer">
        <div class="footer-content">
          <div class="footer-section">
            <h3>关于我们</h3>
            <p>Emart - 您的购物首选</p>
          </div>
          <div class="footer-section">
            <h3>客户服务</h3>
            <p>帮助中心</p>
            <p>售后服务</p>
          </div>
          <div class="footer-section">
            <h3>联系我们</h3>
            <p>电话: 400-123-4567</p>
            <p>邮箱: service@emart.com</p>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2025 Emart. All rights reserved.</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const username = ref('')
const searchKeyword = ref('')
const cartCount = ref(0)

const activeMenu = computed(() => route.path)

onMounted(() => {
  const user = localStorage.getItem('username')
  if (user) {
    username.value = user
  }
  updateCartCount()

  // 监听购物车更新事件
  window.addEventListener('cart-updated', updateCartCount)
})

const updateCartCount = () => {
  // 从localStorage获取购物车数量，如果没有或无效则设为0
  const count = localStorage.getItem('cartCount')
  const numCount = count ? parseInt(count) : 0
  // 如果数量为0或负数，清除localStorage
  if (!numCount || numCount < 0) {
    localStorage.removeItem('cartCount')
    cartCount.value = 0
  } else {
    cartCount.value = numCount
  }
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/products',
      query: { name: searchKeyword.value }
    })
  }
}

const goToAdmin = () => {
  // 跳转到管理员后台（在5175端口）
  window.location.href = 'http://localhost:5175'
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  username.value = ''
  cartCount.value = 0
  router.push('/login')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-container {
  min-height: 100vh;
}

/* 顶部导航栏样式 */
.header {
  background: linear-gradient(90deg, #ff6b6b 0%, #ff8e53 100%);
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  gap: 20px;
}

.logo {
  flex-shrink: 0;
}

.nav-menu {
  flex: 1;
  border: none;
  background: transparent !important;
}

.nav-menu .el-menu-item {
  color: #fff !important;
  border-bottom: none !important;
}

.nav-menu .el-menu-item:hover,
.nav-menu .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.2) !important;
  border-bottom: none !important;
}

.cart-badge {
  margin-top: 10px;
}

.search-bar {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.user-info {
  display: flex;
  gap: 10px;
  align-items: center;
}

.username {
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: 500;
}

/* 主内容区 */
.main-content {
  background-color: #f5f5f5;
  padding: 20px;
  min-height: calc(100vh - 270px);
}

/* 底部样式 */
.footer {
  background-color: #2c3e50;
  color: #fff;
  padding: 40px 20px 20px;
  margin-top: auto;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 30px;
  margin-bottom: 30px;
}

.footer-section {
  flex: 1;
  min-width: 200px;
}

.footer-section h3 {
  margin-bottom: 15px;
  color: #ff8e53;
}

.footer-section p {
  margin: 8px 0;
  color: #bdc3c7;
  font-size: 14px;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #34495e;
  color: #95a5a6;
  font-size: 14px;
}
</style>
