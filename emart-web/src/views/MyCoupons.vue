<template>
  <div class="my-coupons">
    <h2>🎫 我的优惠券</h2>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="未使用" name="0">
        <coupon-list :coupons="unusedCoupons" :loading="loading" />
      </el-tab-pane>
      <el-tab-pane label="已使用" name="1">
        <coupon-list :coupons="usedCoupons" :loading="loading" />
      </el-tab-pane>
      <el-tab-pane label="已过期" name="2">
        <coupon-list :coupons="expiredCoupons" :loading="loading" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyCoupons } from '@/api/coupon'
import CouponList from '@/components/CouponList.vue'

const loading = ref(false)
const activeTab = ref('0')
const unusedCoupons = ref([])
const usedCoupons = ref([])
const expiredCoupons = ref([])

const fetchCoupons = async (status) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }

  loading.value = true
  try {
    const res = await getMyCoupons(status)
    const coupons = res.data || []

    // 为每个优惠券添加优惠券信息（这里简化处理，实际应该从后端返回完整信息）
    if (status === 0) {
      unusedCoupons.value = coupons
    } else if (status === 1) {
      usedCoupons.value = coupons
    } else {
      expiredCoupons.value = coupons
    }
  } catch (error) {
    console.error('获取优惠券列表失败', error)
    ElMessage.error('获取优惠券列表失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = (tabName) => {
  fetchCoupons(parseInt(tabName))
}

onMounted(() => {
  fetchCoupons(0)
})
</script>

<style scoped>
.my-coupons {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.my-coupons h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>
