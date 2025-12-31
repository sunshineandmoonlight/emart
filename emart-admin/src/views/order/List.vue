<template>
  <div class="order-list">
    <el-card>
      <template #header>
        <span>订单管理</span>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
        <el-table-column prop="receiverName" label="收货人" width="100"></el-table-column>
        <el-table-column prop="receiverPhone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleViewDetail(row)">查看详情</el-button>
            <el-button
              size="small"
              type="primary"
              v-if="row.status === 1"
              @click="handleShip(row)"
            >
              发货
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: center"
        @size-change="fetchList"
        @current-change="fetchList"
      ></el-pagination>
    </el-card>

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="800px">
      <el-descriptions :column="2" border v-if="currentOrderDetail">
        <el-descriptions-item label="订单号">{{ currentOrderDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrderDetail.status)">
            {{ getStatusText(currentOrderDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrderDetail.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrderDetail.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrderDetail.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrderDetail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ currentOrderDetail.createTime }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">商品列表</el-divider>
      <el-table :data="currentOrderDetail?.orderItems || []" border>
        <el-table-column prop="productName" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
        <el-table-column prop="totalAmount" label="小计" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog v-model="shipDialogVisible" title="订单发货" width="500px">
      <el-form :model="shipForm" label-width="100px">
        <el-form-item label="快递单号">
          <el-input v-model="shipForm.trackingNo" placeholder="请输入快递单号"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllOrders, shipOrder, getOrderDetail } from '@/api/order'

const loading = ref(false)
const tableData = ref([])
const shipDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentOrder = ref(null)
const currentOrderDetail = ref(null)

const shipForm = reactive({
  trackingNo: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getAllOrders({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'success',
    2: 'info',
    3: 'success',
    4: 'info',
    5: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '已付款',
    2: '已发货',
    3: '已完成',
    4: '已取消',
    5: '已退款'
  }
  return texts[status] || '未知'
}

const handleViewDetail = async (row) => {
  try {
    const res = await getOrderDetail(row.orderNo)
    currentOrderDetail.value = res.data
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}

const handleShip = (row) => {
  currentOrder.value = row
  shipForm.trackingNo = ''
  shipDialogVisible.value = true
}

const confirmShip = async () => {
  if (!shipForm.trackingNo) {
    ElMessage.warning('请输入快递单号')
    return
  }

  try {
    await shipOrder(currentOrder.value.id, shipForm.trackingNo)
    ElMessage.success('发货成功')
    shipDialogVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('发货失败')
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.order-list {
  padding: 20px;
}
</style>
