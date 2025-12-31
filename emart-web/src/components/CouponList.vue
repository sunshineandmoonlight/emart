<template>
  <div v-loading="loading">
    <el-empty v-if="coupons.length === 0" description="暂无优惠券" />

    <el-row :gutter="20" v-else>
      <el-col :xs="24" :sm="12" :md="8" v-for="item in coupons" :key="item.id">
        <div class="my-coupon-card" :class="{ disabled: item.status !== 0 }">
          <div class="coupon-left">
            <div class="coupon-amount">
              <template v-if="item.coupon && item.coupon.type === 1">
                <span class="symbol">{{ (item.coupon.amount * 10).toFixed(1) }}</span>
                <span class="unit">折</span>
              </template>
              <template v-else>
                <span class="symbol">¥</span>
                <span class="amount">{{ item.coupon ? item.coupon.amount : '0' }}</span>
              </template>
            </div>
            <div class="coupon-condition">
              {{ item.coupon && item.coupon.minPoint > 0 ? `满${item.coupon.minPoint}元可用` : '无门槛' }}
            </div>
          </div>

          <div class="coupon-right">
            <h3 class="coupon-name">{{ item.coupon ? item.coupon.name : '优惠券' }}</h3>
            <p class="coupon-code">
              编码：{{ item.couponCode }}
            </p>
            <p class="coupon-validity">
              有效期至：{{ formatDateTime(item.validityEndTime) }}
            </p>
            <div class="coupon-status">
              <el-tag v-if="item.status === 0" type="success">未使用</el-tag>
              <el-tag v-else-if="item.status === 1" type="info">已使用</el-tag>
              <el-tag v-else type="warning">已过期</el-tag>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
defineProps({
  coupons: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.my-coupon-card {
  display: flex;
  border: 2px solid #ff6b6b;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
  background: #fff;
  transition: all 0.3s;
}

.my-coupon-card:not(.disabled):hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(255, 107, 107, 0.3);
}

.my-coupon-card.disabled {
  border-color: #ccc;
  opacity: 0.7;
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

.my-coupon-card.disabled .coupon-left {
  background: linear-gradient(135deg, #ccc 0%, #ddd 100%);
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

.coupon-code {
  font-size: 12px;
  color: #999;
  margin: 5px 0;
}

.coupon-validity {
  font-size: 14px;
  color: #666;
  margin: 5px 0;
}

.coupon-status {
  margin-top: auto;
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
