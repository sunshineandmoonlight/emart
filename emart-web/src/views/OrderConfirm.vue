<template>
  <div class="order-confirm">
    <h2>确认订单</h2>

    <el-card class="address-card" v-if="!showAddressForm">
      <template #header>
        <div class="card-header">
          <h3>收货信息</h3>
          <el-button type="text" @click="showAddressForm = true">修改地址</el-button>
        </div>
      </template>
      <div class="address-info" v-if="address">
        <p><strong>收货人：</strong>{{ address.receiverName }}</p>
        <p><strong>联系电话：</strong>{{ address.receiverPhone }}</p>
        <p><strong>收货地址：</strong>{{ address.receiverAddress }}</p>
      </div>
      <div class="address-info" v-else>
        <el-empty description="暂无收货地址" :image-size="100"></el-empty>
      </div>
    </el-card>

    <el-card class="address-form-card" v-if="showAddressForm">
      <template #header>
        <h3>填写收货信息</h3>
      </template>
      <el-form :model="addressForm" :rules="addressRules" ref="addressFormRef" label-width="100px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input
            v-model="addressForm.receiverAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入详细收货地址"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveAddress">保存地址</el-button>
          <el-button @click="cancelAddress" v-if="address">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="goods-card">
      <template #header>
        <h3>商品信息</h3>
      </template>
      <el-table :data="cartItems" style="width: 100%">
        <el-table-column label="商品信息" width="400">
          <template #default="scope">
            <div class="product-info">
              <img :src="getImageUrl(scope.row.productImage)" class="product-img">
              <div>
                <div class="product-name">{{ scope.row.productName }}</div>
                <div class="product-price">¥{{ scope.row.productPrice }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="数量" prop="quantity" width="120"></el-table-column>

        <el-table-column label="小计">
          <template #default="scope">
            <span class="subtotal">¥{{ (scope.row.productPrice * scope.row.quantity).toFixed(2) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="summary-card">
      <div class="summary-content">
        <div class="summary-row">
          <span class="label">商品总额：</span>
          <span class="value">¥{{ totalPrice }}</span>
        </div>
        <div class="summary-row">
          <span class="label">运费：</span>
          <span class="value">¥{{ shippingFee }}</span>
        </div>
        <div class="summary-row total">
          <span class="label">应付总额：</span>
          <span class="value">¥{{ totalAmount }}</span>
        </div>
      </div>
    </el-card>

    <div class="submit-bar">
      <div class="submit-content">
        <div class="total-info">
          <span class="label">共 {{ totalQuantity }} 件商品</span>
          <span class="separator">|</span>
          <span class="label">合计：</span>
          <span class="total">¥{{ totalAmount }}</span>
        </div>
        <el-button type="danger" size="large" @click="submitOrder" :loading="submitting">
          提交订单
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList } from '@/api/cart'
import { createOrder } from '@/api/order'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const route = useRoute()

const cartItems = ref([])
const address = ref(null)
const showAddressForm = ref(false)
const submitting = ref(false)
const addressFormRef = ref()

const addressForm = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: ''
})

const addressRules = {
  receiverName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
}

const shippingFee = ref(0) // 运费，暂时为0

const totalQuantity = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

const totalPrice = computed(() => {
  return cartItems.value
    .reduce((sum, item) => sum + item.productPrice * item.quantity, 0)
    .toFixed(2)
})

const totalAmount = computed(() => {
  return (parseFloat(totalPrice.value) + shippingFee.value).toFixed(2)
})

const fetchCart = async () => {
  try {
    const res = await getCartList()
    cartItems.value = (res.data || []).map(item => ({
      ...item,
      productName: item.product?.name || '商品',
      productPrice: item.product?.price || 0,
      productImage: item.product?.image || ''
    }))

    if (cartItems.value.length === 0) {
      ElMessage.warning('购物车为空，请先添加商品')
      router.push('/products')
    }
  } catch (error) {
    ElMessage.error('获取购物车失败')
  }
}

const saveAddress = () => {
  addressFormRef.value.validate((valid) => {
    if (valid) {
      address.value = { ...addressForm }
      showAddressForm.value = false
      ElMessage.success('地址保存成功')
    }
  })
}

const cancelAddress = () => {
  showAddressForm.value = false
}

const submitOrder = async () => {
  if (!address.value) {
    ElMessage.warning('请先填写收货地址')
    showAddressForm.value = true
    return
  }

  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车为空')
    return
  }

  try {
    await ElMessageBox.confirm('确认提交订单？', '提示', {
      type: 'warning'
    })

    submitting.value = true

    const orderData = {
      receiverName: address.value.receiverName,
      receiverPhone: address.value.receiverPhone,
      receiverAddress: address.value.receiverAddress
    }

    const res = await createOrder(orderData)

    ElMessage.success('订单创建成功！订单确认邮件已发送至您的邮箱')

    // 跳转到订单页面
    router.push('/order')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '订单创建失败')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  // 从路由参数获取商品信息（如果有）
  const products = route.query.products
  if (products) {
    // 如果是从商品详情页立即购买来的
    try {
      const productData = JSON.parse(products)
      cartItems.value = [productData]
    } catch (e) {
      console.error('解析商品数据失败', e)
    }
  } else {
    // 否则从购物车获取
    fetchCart()
  }

  // 从localStorage获取默认地址
  const savedAddress = localStorage.getItem('defaultAddress')
  if (savedAddress) {
    try {
      address.value = JSON.parse(savedAddress)
      Object.assign(addressForm, address.value)
    } catch (e) {
      console.error('解析地址数据失败', e)
    }
  }
})
</script>

<style scoped>
.order-confirm {
  max-width: 1000px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.order-confirm h2 {
  margin-bottom: 20px;
  color: #333;
}

.address-card,
.goods-card,
.summary-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #333;
}

.address-info {
  padding: 20px;
}

.address-info p {
  margin: 10px 0;
  font-size: 14px;
  color: #666;
}

.product-info {
  display: flex;
  gap: 15px;
  align-items: center;
}

.product-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.product-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
}

.product-price {
  font-size: 14px;
  color: #999;
}

.subtotal {
  font-size: 16px;
  color: #ff6b6b;
  font-weight: bold;
}

.summary-content {
  padding: 20px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 14px;
}

.summary-row.total {
  padding-top: 15px;
  border-top: 2px solid #eee;
  font-size: 18px;
  font-weight: bold;
}

.summary-row .label {
  color: #666;
}

.summary-row .value {
  color: #333;
}

.summary-row.total .value {
  color: #ff6b6b;
  font-size: 24px;
}

.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  padding: 15px 0;
  z-index: 100;
}

.submit-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
}

.total-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.total-info .label {
  color: #666;
  font-size: 14px;
}

.total-info .separator {
  color: #ddd;
}

.total-info .total {
  color: #ff6b6b;
  font-size: 24px;
  font-weight: bold;
}

@media (max-width: 768px) {
  .order-confirm {
    padding: 10px;
    padding-bottom: 140px;
  }

  .submit-content {
    flex-direction: column;
    gap: 10px;
  }

  .total-info {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
