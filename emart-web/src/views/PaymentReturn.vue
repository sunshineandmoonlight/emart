<template>
  <div class="payment-return">
    <el-result
      icon="success"
      title="支付成功"
      sub-title="感谢您的购买，订单正在处理中"
    >
      <template #extra>
        <el-button type="primary" @click="goToOrders">查看订单</el-button>
        <el-button @click="goToHome">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToOrders = () => {
  router.push('/order')
}

const goToHome = () => {
  router.push('/')
}
</script>

<style scoped>
.payment-return {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
</style>
