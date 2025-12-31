<template>
  <div class="coupon-list">
    <h2>🎫 领券中心</h2>
    <div v-loading="loading" class="coupon-container">
      <el-empty v-if="coupons.length === 0" description="暂无可领取的优惠券" />

      <el-row :gutter="20" v-else>
        <el-col :xs="24" :sm="12" :md="8" v-for="coupon in coupons" :key="coupon.id">
          <div class="coupon-card">
            <div class="coupon-left">
              <div class="coupon-amount">
                <template v-if="coupon.type === 1">
                  <span class="symbol">{{ (coupon.amount * 10).toFixed(1) }}</span>
                  <span class="unit">折</span>
                </template>
                <template v-else>
                  <span class="symbol">¥</span>
                  <span class="amount">{{ coupon.amount }}</span>
                </template>
              </div>
              <div class="coupon-condition">
                {{ coupon.minPoint > 0 ? `满${coupon.minPoint}元可用` : '无门槛' }}
              </div>
            </div>

            <div class="coupon-right">
              <h3 class="coupon-name">{{ coupon.name }}</h3>
              <p class="coupon-validity">
                有效期：{{ formatDate(coupon.startTime) }} 至 {{ formatDate(coupon.endTime) }}
              </p>
              <div class="coupon-stock">
                剩余：{{ coupon.count - coupon.receiveCount }} / {{ coupon.count }}
              </div>

              <el-button
                type="danger"
                :disabled="coupon.count - coupon.receiveCount <= 0"
                @click="handleReceive(coupon)"
                :loading="receiving === coupon.id"
                class="receive-btn"
              >
                {{ coupon.count - coupon.receiveCount <= 0 ? '已领完' : '立即领取' }}
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAvailableCoupons, receiveCoupon } from '@/api/coupon'

const loading = ref(false)
const coupons = ref([])
const receiving = ref(null)

const fetchCoupons = async () => {
  loading.value = true
  try {
    const res = await getAvailableCoupons()
    coupons.value = res.data || []
  } catch (error) {
    console.error('获取优惠券列表失败', error)
    ElMessage.error('获取优惠券列表失败')
  } finally {
    loading.value = false
  }
}

const handleReceive = async (coupon) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }

  receiving.value = coupon.id
  try {
    await receiveCoupon(coupon.id)
    ElMessage.success('领取成功！')
    // 刷新列表
    await fetchCoupons()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '领取失败')
  } finally {
    receiving.value = null
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}.${date.getDate()}`
}

onMounted(() => {
  fetchCoupons()
})
</script>

<style scoped>
.coupon-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.coupon-list h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.coupon-container {
  min-height: 400px;
}

.coupon-card {
  display: flex;
  border: 2px solid #ff6b6b;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
  background: #fff;
  transition: all 0.3s;
}

.coupon-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(255, 107, 107, 0.3);
}

.coupon-left {
  flex: 0 0 120px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8787 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.coupon-amount {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
}

.coupon-amount .symbol {
  font-size: 20px;
}

.coupon-amount .amount {
  font-size: 36px;
}

.coupon-condition {
  font-size: 12px;
  opacity: 0.9;
}

.coupon-right {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.coupon-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 0;
}

.coupon-validity {
  font-size: 14px;
  color: #666;
  margin: 5px 0;
}

.coupon-stock {
  font-size: 12px;
  color: #999;
  margin: 5px 0;
}

.receive-btn {
  margin-top: auto;
  width: 100%;
}

@media (max-width: 768px) {
  .coupon-left {
    flex: 0 0 100px;
  }

  .coupon-amount {
    font-size: 24px;
  }

  .coupon-amount .amount {
    font-size: 28px;
  }
}
</style>
