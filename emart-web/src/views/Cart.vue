<template>
  <div class="cart">
    <h2>购物车</h2>

    <el-card v-if="cartItems.length === 0" class="empty-cart">
      <el-empty description="购物车是空的"></el-empty>
      <el-button type="primary" @click="$router.push('/products')">去逛逛</el-button>
    </el-card>

    <div v-else>
      <!-- 购物车列表 -->
      <el-card class="cart-list">
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

          <el-table-column label="数量" width="200">
            <template #default="scope">
              <el-input-number
                v-model="scope.row.quantity"
                :min="1"
                :max="99"
                @change="updateQuantity(scope.row)"
              ></el-input-number>
            </template>
          </el-table-column>

          <el-table-column label="小计">
            <template #default="scope">
              <span class="subtotal">¥{{ (scope.row.productPrice * scope.row.quantity).toFixed(2) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removeItem(scope.row.productId)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 结算栏 -->
      <el-card class="checkout-bar">
        <div class="checkout-content">
          <div class="total-info">
            <span class="label">已选商品</span>
            <span class="count">{{ totalQuantity }}件</span>
            <span class="separator">|</span>
            <span class="label">合计：</span>
            <span class="total">¥{{ totalPrice }}</span>
          </div>
          <el-button type="primary" size="large" @click="checkout">结算</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartItem, deleteCartItem } from '@/api/cart'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const cartItems = ref([])

const totalQuantity = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

const totalPrice = computed(() => {
  return cartItems.value
    .reduce((sum, item) => sum + item.productPrice * item.quantity, 0)
    .toFixed(2)
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
    // 更新localStorage中的购物车数量
    const totalCount = cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
    if (totalCount > 0) {
      localStorage.setItem('cartCount', totalCount.toString())
    } else {
      localStorage.removeItem('cartCount')
    }
    // 触发window事件通知App.vue更新
    window.dispatchEvent(new Event('cart-updated'))
  } catch (error) {
    ElMessage.error('获取购物车失败')
  }
}

const updateQuantity = async (item) => {
  try {
    await updateCartItem(item.productId, { quantity: item.quantity })
    ElMessage.success('更新成功')
  } catch (error) {
    ElMessage.error('更新失败')
    fetchCart()
  }
}

const removeItem = async (productId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      type: 'warning'
    })
    await deleteCartItem(productId)
    ElMessage.success('删除成功')
    fetchCart()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const checkout = () => {
  router.push('/order/confirm')
}

onMounted(() => {
  fetchCart()
})
</script>

<style scoped>
.cart {
  max-width: 1200px;
  margin: 0 auto;
}

.empty-cart {
  text-align: center;
  padding: 40px;
}

.cart-list {
  margin-bottom: 20px;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.product-name {
  font-size: 14px;
  margin-bottom: 8px;
}

.product-price {
  color: #f56c6c;
  font-size: 16px;
}

.subtotal {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.checkout-bar {
  position: sticky;
  bottom: 20px;
}

.checkout-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-info {
  font-size: 16px;
}

.total-info .label {
  margin-right: 10px;
}

.total-info .count {
  color: #409EFF;
  font-weight: bold;
  margin-right: 15px;
}

.total-info .separator {
  margin: 0 15px;
  color: #DCDFE6;
}

.total-info .total {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}
</style>
