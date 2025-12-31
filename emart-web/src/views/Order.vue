<template>
  <div class="order">
    <h2>我的订单</h2>

    <el-card v-if="orders.length === 0" class="empty-order">
      <el-empty description="暂无订单"></el-empty>
      <el-button type="primary" @click="$router.push('/products')">去购物</el-button>
    </el-card>

    <div v-else>
      <el-card v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <div class="header-actions">
            <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
            <el-button type="primary" size="small" @click="viewDetail(order)" style="margin-left: 10px">
              查看详情
            </el-button>
          </div>
        </div>

        <div class="order-content">
          <div class="order-info">
            <p><strong>创建时间：</strong>{{ order.createTime }}</p>
            <p><strong>收货人：</strong>{{ order.receiverName }}</p>
            <p><strong>联系电话：</strong>{{ order.receiverPhone }}</p>
            <p><strong>收货地址：</strong>{{ order.receiverAddress }}</p>
          </div>

          <div class="order-items">
            <div v-for="item in order.orderItems" :key="item.id" class="order-item">
              <img :src="item.productImage || '/placeholder.jpg'" class="item-image">
              <div class="item-info">
                <div class="item-name">{{ item.productName }}</div>
                <div class="item-detail">¥{{ item.price }} × {{ item.quantity }}</div>
              </div>
              <div class="item-total">¥{{ item.totalAmount }}</div>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-total">
              <span>订单总额：</span>
              <span class="total-amount">¥{{ order.totalAmount }}</span>
            </div>
            <div class="order-actions">
              <el-button
                v-if="order.status === 0"
                type="primary"
                size="small"
                @click="payOrder(order)"
              >
                去支付
              </el-button>
              <el-button
                v-if="order.status === 2"
                type="success"
                size="small"
                @click="confirmReceipt(order)"
              >
                确认收货
              </el-button>
              <el-button
                v-if="order.status === 0"
                type="danger"
                size="small"
                @click="cancelOrder(order)"
              >
                取消订单
              </el-button>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchOrders"
        @current-change="fetchOrders"
        class="pagination"
      >
      </el-pagination>

      <!-- 订单详情弹窗 -->
      <el-dialog v-model="orderDetailVisible" title="订单详情" width="60%" top="5vh">
        <div v-if="currentOrderDetail" class="order-detail-dialog">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ currentOrderDetail.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <el-tag :type="getStatusType(currentOrderDetail.status)">
                {{ getStatusText(currentOrderDetail.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentOrderDetail.createTime }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">
              {{ currentOrderDetail.payTime || '未支付' }}
            </el-descriptions-item>
            <el-descriptions-item label="收货人">{{ currentOrderDetail.receiverName }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentOrderDetail.receiverPhone }}</el-descriptions-item>
            <el-descriptions-item label="收货地址" :span="2">{{ currentOrderDetail.receiverAddress }}</el-descriptions-item>
          </el-descriptions>

          <el-divider content-position="left">商品信息</el-divider>

          <div class="detail-items">
            <div v-for="item in currentOrderDetail.orderItems" :key="item.id" class="detail-item">
              <img :src="item.productImage || '/placeholder.jpg'" class="detail-item-image">
              <div class="detail-item-info">
                <div class="detail-item-name">{{ item.productName }}</div>
                <div class="detail-item-detail">¥{{ item.price }} × {{ item.quantity }}</div>
              </div>
              <div class="detail-item-total">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>

          <el-divider />

          <div class="detail-footer">
            <span class="detail-total-label">订单总额：</span>
            <span class="detail-total-amount">¥{{ currentOrderDetail.totalAmount }}</span>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderListWithDetails, cancelOrder as cancelOrderApi, finishOrder, getOrderDetail } from '@/api/order'
import { payOrder as payOrderApi } from '@/api/payment'

const router = useRouter()

const orders = ref([])
const orderDetailVisible = ref(false)
const currentOrderDetail = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const fetchOrders = async () => {
  try {
    const res = await getOrderListWithDetails({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    orders.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  }
}

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

const payOrder = async (order) => {
  try {
    ElMessage.info('正在跳转到支付页面...')

    // 跳转到模拟支付页面
    router.push({
      path: '/payment/simulate',
      query: {
        orderId: order.id,
        orderNo: order.orderNo,
        totalAmount: order.totalAmount
      }
    })
  } catch (error) {
    ElMessage.error('支付发起失败')
  }
}

const confirmReceipt = (order) => {
  ElMessageBox.confirm('确认收货？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await finishOrder(order.id)
      ElMessage.success('确认收货成功')
      fetchOrders()
    } catch (error) {
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

const cancelOrder = (order) => {
  ElMessageBox.confirm('确定要取消这个订单吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await cancelOrderApi(order.id)
      ElMessage.success('订单已取消')
      fetchOrders()
    } catch (error) {
      ElMessage.error('取消失败')
    }
  }).catch(() => {})
}

// 查看订单详情
const viewDetail = async (order) => {
  try {
    const res = await getOrderDetail(order.orderNo)
    currentOrderDetail.value = res.data
    orderDetailVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order {
  max-width: 1000px;
  margin: 0 auto;
}

.empty-order {
  text-align: center;
  padding: 40px;
}

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #EBEEF5;
  margin-bottom: 15px;
}

.order-no {
  font-size: 16px;
  font-weight: bold;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.order-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-info p {
  margin: 5px 0;
  color: #606266;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 15px;
  background-color: #F5F7FA;
  border-radius: 4px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  margin-bottom: 8px;
}

.item-detail {
  color: #909399;
  font-size: 13px;
}

.item-total {
  color: #f56c6c;
  font-size: 16px;
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #EBEEF5;
}

.order-total {
  font-size: 16px;
}

.total-amount {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
  margin-left: 10px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* 订单详情弹窗样式 */
.order-detail-dialog {
  padding: 10px 0;
}

.detail-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background-color: #F5F7FA;
  border-radius: 4px;
}

.detail-item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.detail-item-info {
  flex: 1;
}

.detail-item-name {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
}

.detail-item-detail {
  color: #909399;
  font-size: 13px;
}

.detail-item-total {
  color: #f56c6c;
  font-size: 16px;
  font-weight: bold;
}

.detail-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20px 0;
  font-size: 16px;
}

.detail-total-label {
  color: #606266;
  margin-right: 10px;
}

.detail-total-amount {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
}
</style>
