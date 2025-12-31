<template>
  <div class="favorite-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的收藏</span>
          <el-button type="text" @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-empty v-if="products.length === 0 && !loading" description="暂无收藏商品" />

      <div v-loading="loading" class="product-grid">
        <div
          v-for="product in products"
          :key="product.id"
          class="product-card"
        >
          <div class="product-image" @click="goToDetail(product.id)">
            <img :src="product.image || '/default-product.jpg'" :alt="product.name" />
          </div>

          <div class="product-info">
            <h3 class="product-name" @click="goToDetail(product.id)">{{ product.name }}</h3>
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-actions">
              <el-button
                type="primary"
                size="small"
                @click="addToCart(product)"
                :icon="ShoppingCart"
              >
                加入购物车
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="removeFavorite(product.id)"
                :icon="Delete"
              >
                取消收藏
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <el-pagination
        v-if="total > 0"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="total"
        :page-sizes="[12, 24, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 30px; justify-content: center"
        @size-change="fetchFavorites"
        @current-change="fetchFavorites"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ShoppingCart, Delete } from '@element-plus/icons-vue'
import { getFavoriteList, removeFavorite as removeFavoriteApi } from '@/api/favorite'
import { addToCart as addToCartApi } from '@/api/cart'

const router = useRouter()
const loading = ref(false)
const products = ref([])

const pagination = reactive({
  pageNum: 1,
  pageSize: 12
})

const total = ref(0)

const fetchFavorites = async () => {
  loading.value = true
  try {
    const res = await getFavoriteList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    products.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

const goToDetail = (productId) => {
  router.push(`/product/${productId}`)
}

const addToCart = async (product) => {
  try {
    await addToCartApi({
      productId: product.id,
      quantity: 1
    })
    ElMessage.success('已加入购物车')
  } catch (error) {
    ElMessage.error('加入购物车失败')
  }
}

const removeFavorite = async (productId) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    await removeFavoriteApi(productId)
    ElMessage.success('已取消收藏')
    fetchFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消收藏失败')
    }
  }
}

onMounted(() => {
  fetchFavorites()
})
</script>

<style scoped>
.favorite-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  background: #fff;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.product-image {
  width: 100%;
  height: 220px;
  overflow: hidden;
  cursor: pointer;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-image:hover img {
  transform: scale(1.05);
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  cursor: pointer;
}

.product-name:hover {
  color: #409eff;
}

.product-price {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 15px;
}

.product-actions {
  display: flex;
  gap: 8px;
}

.product-actions .el-button {
  flex: 1;
}
</style>
