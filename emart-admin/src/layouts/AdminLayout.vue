<template>
  <div class="admin-layout">
    <!-- 左侧菜单 -->
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <h2>Emart 管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="menu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>首页统计</span>
        </el-menu-item>
        <el-menu-item index="/analysis/dashboard">
          <el-icon><TrendCharts /></el-icon>
          <span>数据分析</span>
        </el-menu-item>
        <el-menu-item index="/product/list">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/category/list">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/order/list">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/user/list">
          <el-icon><User /></el-icon>
          <span>客户管理</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin" index="/sales/list">
          <el-icon><Avatar /></el-icon>
          <span>销售人员</span>
        </el-menu-item>
        <el-menu-item index="/browse/list">
          <el-icon><DataLine /></el-icon>
          <span>浏览日志</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容 -->
    <el-container class="main-container">
      <el-header class="header">
        <div class="header-content">
          <span class="title">{{ pageTitle }}</span>
          <div class="user-info">
            <el-button type="primary" plain @click="goHome">返回主页</el-button>
            <span>{{ username }}（{{ roleLabel }}）</span>
            <el-button type="danger" text @click="logout">退出</el-button>
          </div>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  Odometer, Goods, Document, User, DataLine, Avatar, Menu, TrendCharts
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => route.path)
const role = computed(() => localStorage.getItem('admin_role') || 'ADMIN')
const username = computed(() => localStorage.getItem('admin_username') || 'admin')
const isAdmin = computed(() => role.value === 'ADMIN')
const roleLabel = computed(() => role.value === 'SALES' ? '销售人员' : '管理者')

const goHome = () => {
  const currentHost = window.location.hostname
  const protocol = window.location.protocol
  if (currentHost === 'localhost' || currentHost === '127.0.0.1') {
    window.open(`${protocol}//localhost:5173/`, '_blank')
  } else {
    window.open(`${protocol}//${currentHost}/`, '_blank')
  }
}

const pageTitle = computed(() => {
  const titleMap = {
    '/dashboard': '首页统计',
    '/analysis/dashboard': '数据分析',
    '/product/list': '商品管理',
    '/category/list': '分类管理',
    '/order/list': '订单管理',
    '/user/list': '客户管理',
    '/sales/list': '销售人员管理',
    '/browse/list': '浏览日志'
  }
  return titleMap[route.path] || '管理后台'
})

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_username')
    localStorage.removeItem('admin_role')
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
}

.sidebar {
  background-color: #304156;
  height: 100%;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b3a4b;
}

.menu {
  border: none;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,.08);
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
