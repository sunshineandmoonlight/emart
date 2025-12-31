<template>
  <div class="profile-page">
    <h2>个人中心</h2>

    <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
      <!-- 个人信息 -->
      <el-tab-pane label="个人信息" name="info">
        <el-card class="info-card">
          <el-form :model="userInfo" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="userInfo.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userInfo.nickname" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userInfo.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateInfo">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 我的订单 -->
      <el-tab-pane label="我的订单" name="orders">
        <el-card class="orders-card">
          <div v-loading="loading">
            <el-empty v-if="orders.length === 0" description="暂无订单"></el-empty>

            <div v-else class="order-list">
              <el-card
                v-for="order in orders"
                :key="order.id"
                class="order-item"
                shadow="hover"
              >
                <div class="order-header">
                  <div class="order-info">
                    <span class="order-no">订单号：{{ order.orderNo }}</span>
                    <span class="order-time">{{ order.createTime }}</span>
                  </div>
                  <el-tag :type="getStatusType(order.status)">
                    {{ getStatusText(order.status) }}
                  </el-tag>
                </div>

                <div class="order-content">
                  <div class="order-items">
                    <div
                      v-for="item in order.items"
                      :key="item.id"
                      class="order-item"
                    >
                      <img :src="getImageUrl(item.productImage)" class="item-image">
                      <div class="item-info">
                        <div class="item-name">{{ item.productName }}</div>
                        <div class="item-price">¥{{ item.productPrice }} × {{ item.quantity }}</div>
                      </div>
                    </div>
                  </div>

                  <div class="order-total">
                    <div class="total-label">订单总额：</div>
                    <div class="total-amount">¥{{ order.totalAmount }}</div>
                  </div>
                </div>

                <div class="order-actions">
                  <el-button size="small" @click="viewOrderDetail(order)">查看详情</el-button>
                  <el-button
                    size="small"
                    type="danger"
                    v-if="order.status === 0"
                    @click="cancelOrder(order)"
                  >
                    取消订单
                  </el-button>
                  <el-button
                    size="small"
                    type="primary"
                    v-if="order.status === 0"
                    @click="payOrder(order)"
                  >
                    去支付
                  </el-button>
                </div>
              </el-card>
            </div>
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 收货地址 -->
      <el-tab-pane label="收货地址" name="address">
        <AddressManage ref="addressManageRef" />
      </el-tab-pane>

      <!-- 修改密码 -->
      <el-tab-pane label="账号安全" name="password">
        <el-card class="password-card">
          <h3>修改密码</h3>
          <el-form :model="passwordForm" label-width="100px" :rules="passwordRules" ref="passwordFormRef">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePassword">修改密码</el-button>
            </el-form-item>
          </el-form>

          <el-divider />

          <h3>危险操作</h3>
          <el-alert
            title="注销账号"
            type="warning"
            description="注销账号是不可逆的操作，注销后您将无法恢复账号及所有相关数据。请谨慎操作！"
            :closable="false"
            show-icon
            style="margin-bottom: 20px"
          >
          </el-alert>
          <el-button type="danger" @click="handleDeleteAccount">注销账号</el-button>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import { getOrderList, cancelOrder as cancelOrderApi } from '@/api/order'
import { logout, deleteAccount as deleteAccountApi } from '@/api/user'
import { getImageUrl } from '@/utils/image'
import AddressManage from '@/components/AddressManage.vue'

const router = useRouter()
const activeTab = ref('info')
const loading = ref(false)
const addressManageRef = ref()

const userInfo = ref({
  username: '',
  nickname: '',
  email: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const orders = ref([])

onMounted(() => {
  const username = localStorage.getItem('username')
  if (username) {
    userInfo.value.username = username
    userInfo.value.nickname = username
  }
})

const updateInfo = () => {
  localStorage.setItem('nickname', userInfo.value.nickname)
  ElMessage.success('保存成功')
}

const updatePassword = () => {
  ElMessage.success('密码修改成功')
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList({ pageNum: 1, pageSize: 20 })
    orders.value = (res.data.records || []).map(order => ({
      ...order,
      items: order.orderItems || []
    }))
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
    4: 'success',
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

const viewOrderDetail = (order) => {
  ElMessage.info('订单详情功能开发中...')
}

const cancelOrder = async (order) => {
  try {
    await cancelOrderApi(order.id)
    ElMessage.success('订单已取消')
    fetchOrders()
  } catch (error) {
    ElMessage.error('取消订单失败')
  }
}

const payOrder = (order) => {
  ElMessage.info('支付功能开发中...')
}

// 监听tab切换，加载订单数据
const handleTabChange = (tabName) => {
  if (tabName === 'orders' && orders.value.length === 0) {
    fetchOrders()
  }
}

// 注销账号
const handleDeleteAccount = () => {
  ElMessageBox.confirm(
    '注销账号是不可逆的操作，注销后您将无法恢复账号及所有相关数据（包括订单、收藏、购物车等）。确定要注销吗？',
    '警告',
    {
      confirmButtonText: '确定注销',
      cancelButtonText: '取消',
      type: 'warning',
      distinguishCancelAndClose: true,
      beforeClose: (action, instance, done) => {
        if (action === 'confirm') {
          instance.confirmButtonLoading = true
          instance.confirmButtonText = '注销中...'

          // 再次确认
          ElMessageBox.confirm(
            '最后确认！注销后您的所有数据将被永久删除且无法恢复！',
            '最终确认',
            {
              confirmButtonText: '我已了解，确认注销',
              cancelButtonText: '我再想想',
              type: 'error'
            }
          ).then(async () => {
            try {
              await deleteAccountApi()

              // 清除本地存储
              localStorage.removeItem('token')
              localStorage.removeItem('userInfo')

              ElNotification({
                title: '账号已注销',
                message: '感谢您的使用，期待再次相遇！',
                type: 'success',
                duration: 3000
              })

              // 跳转到首页
              setTimeout(() => {
                router.push('/login')
              }, 1000)

              done()
            } catch (error) {
              ElMessage.error(error.response?.data?.message || '注销失败，请稍后重试')
              instance.confirmButtonLoading = false
              done()
            }
          }).catch(() => {
            instance.confirmButtonLoading = false
            done()
          })
        } else {
          done()
        }
      }
    }
  ).catch(() => {})
}
</script>

<style scoped>
.profile-page {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-page h2 {
  margin-bottom: 20px;
  color: #333;
}

.info-card,
.password-card {
  max-width: 600px;
  margin: 0 auto;
}

.orders-card {
  min-height: 400px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-item {
  border: 1px solid #eee;
  margin-bottom: 0;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.order-info {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.order-no {
  font-weight: 500;
}

.order-content {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.order-items {
  flex: 1;
}

.order-item {
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
}

.item-price {
  font-size: 13px;
  color: #999;
}

.order-total {
  text-align: right;
  min-width: 150px;
}

.total-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.total-amount {
  font-size: 20px;
  color: #ff6b6b;
  font-weight: bold;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

@media (max-width: 768px) {
  .order-content {
    flex-direction: column;
  }

  .order-total {
    text-align: left;
    margin-top: 15px;
  }
}
</style>
