<template>
  <div class="payment-simulate">
    <el-card class="payment-card">
      <h2>模拟支付</h2>

      <el-descriptions :column="1" border class="order-info">
        <el-descriptions-item label="订单号">{{ orderInfo.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="支付金额">
          <span class="amount">¥{{ orderInfo.totalAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(orderInfo.status)">{{ getStatusText(orderInfo.status) }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider></el-divider>

      <div class="qr-code-container">
        <h3>扫码支付</h3>
        <div class="qr-code">
          <img :src="qrCodeUrl" alt="支付宝收款码" class="qr-image">
        </div>
        <p class="tip">请使用支付宝扫一扫完成支付（模拟环境）</p>
      </div>

      <el-divider></el-divider>

      <div class="actions">
        <el-button type="success" size="large" @click="confirmPayment" :loading="loading">
          模拟支付成功
        </el-button>
        <el-button size="large" @click="cancelPayment">取消支付</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { updateOrderStatus } from '@/api/order'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
// 支付宝收款码图片
const qrCodeUrl = ref('https://maas-log-prod.cn-wlcb.ufileos.com/anthropic/b57c2e2c-603c-4caf-9500-3dc77b273661/11c9b75bcc70290bf90d3e42edd37319.jpg?UCloudPublicKey=TOKEN_e15ba47a-d098-4fbd-9afc-a0dcf0e4e621&Expires=1767176889&Signature=2/BTQ63vVknnN1D6AOprtQlsNjM=')
const orderInfo = ref({
  orderNo: '',
  totalAmount: '0.00',
  status: 0
})

onMounted(() => {
  // 从路由参数获取订单信息
  const { orderId, orderNo, totalAmount } = route.query
  if (orderId && orderNo && totalAmount) {
    orderInfo.value = {
      orderNo,
      totalAmount,
      status: 0
    }
  } else {
    ElMessage.error('订单信息不完整')
    router.push('/order')
  }
})

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info',
    4: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待发货',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const confirmPayment = async () => {
  loading.value = true
  try {
    // 调用后端接口更新订单状态为已付款
    await updateOrderStatus(route.query.orderId, 1)
    ElMessage.success('支付成功！')
    // 跳转到订单列表
    setTimeout(() => {
      router.push('/order')
    }, 1000)
  } catch (error) {
    ElMessage.error('支付失败')
  } finally {
    loading.value = false
  }
}

const cancelPayment = () => {
  router.push('/order')
}
</script>

<style scoped>
.payment-simulate {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
}

.payment-card {
  text-align: center;
}

.payment-card h2 {
  color: #409EFF;
  margin-bottom: 20px;
}

.order-info {
  margin: 20px 0;
  text-align: left;
}

.amount {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
}

.qr-code-container {
  margin: 30px 0;
}

.qr-code-container h3 {
  margin-bottom: 20px;
  color: #606266;
}

.qr-code {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.qr-image {
  width: 200px;
  height: 200px;
  border: 2px solid #DCDFE6;
  border-radius: 8px;
}

.tip {
  color: #909399;
  font-size: 14px;
  margin-top: 10px;
}

.actions {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 20px;
}
</style>
