<template>
  <div class="product-detail" v-loading="loading">
    <el-card v-if="product" class="detail-card">
      <el-row :gutter="40">
        <!-- 左侧图片区域 -->
        <el-col :xs="24" :md="12">
          <div class="image-section">
            <!-- 主图片展示区 - 支持放大镜效果 -->
            <div class="main-image" @mouseenter="showMagnifier = true" @mouseleave="showMagnifier = false" @mousemove="handleMouseMove">
              <img :src="getImageUrl(currentImage)" v-if="currentImage" ref="mainImageRef">
              <div v-else class="placeholder">暂无图片</div>

              <!-- 放大镜效果 -->
              <div v-if="showMagnifier && currentImage" class="magnifier" :style="magnifierStyle">
                <img :src="getImageUrl(currentImage)" :style="magnifierImgStyle">
              </div>
            </div>

            <!-- 缩略图列表 -->
            <div class="thumbnail-list" v-if="productImages.length > 0">
              <div
                v-for="(img, index) in productImages"
                :key="index"
                class="thumbnail"
                :class="{ active: currentImage === img }"
                @click="currentImage = img"
              >
                <img :src="getImageUrl(img)">
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧商品信息 -->
        <el-col :xs="24" :md="12">
          <div class="info-section">
            <h1 class="product-name">{{ product.name }}</h1>
            <p class="product-subtitle">{{ product.subtitle }}</p>

            <div class="price-section">
              <div class="price-row">
                <span class="label">价格</span>
                <span class="price">¥{{ product.price }}</span>
              </div>
              <div class="stock-row">
                <span class="label">库存</span>
                <span :class="['stock', getStock() > 0 ? 'in-stock' : 'out-stock']">
                  {{ getStock() > 0 ? `${getStock()} 件` : '暂时缺货' }}
                </span>
              </div>
            </div>

            <div class="divider"></div>

            <!-- 数量选择 -->
            <div class="quantity-section">
              <span class="label">数量</span>
              <el-input-number
                v-model="quantity"
                :min="1"
                :max="getStock()"
                size="large"
                :disabled="getStock() === 0"
              ></el-input-number>
            </div>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button
                type="warning"
                size="large"
                :disabled="getStock() === 0"
                @click="addToCart"
                :loading="adding"
              >
                <el-icon><ShoppingCart /></el-icon>
                加入购物车
              </el-button>
              <el-button
                type="danger"
                size="large"
                :disabled="getStock() === 0"
                @click="buyNow"
              >
                立即购买
              </el-button>
            </div>

            <!-- 服务保障 -->
            <div class="service-guarantee">
              <div class="guarantee-item">
                <el-icon><CircleCheck /></el-icon>
                <span>正品保障</span>
              </div>
              <div class="guarantee-item">
                <el-icon><CircleCheck /></el-icon>
                <span>极速退款</span>
              </div>
              <div class="guarantee-item">
                <el-icon><CircleCheck /></el-icon>
                <span>售后无忧</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 商品详情 -->
      <div class="product-description">
        <el-divider content-position="left">
          <h3>商品详情</h3>
        </el-divider>
        <div class="description-content">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="商品名称">{{ product.name }}</el-descriptions-item>
            <el-descriptions-item label="商品编号">{{ product.id }}</el-descriptions-item>
            <el-descriptions-item label="商品分类">{{ categoryName }}</el-descriptions-item>
            <el-descriptions-item label="品牌">{{ product.brand || '暂无' }}</el-descriptions-item>
            <el-descriptions-item label="商品价格">¥{{ product.price }}</el-descriptions-item>
            <el-descriptions-item label="库存数量">{{ product.stock }} 件</el-descriptions-item>
          </el-descriptions>

          <div class="detail-text" v-if="product.detail">
            <h4>详细说明</h4>
            <p>{{ product.detail }}</p>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 协同推荐 -->
    <el-card class="recommend-card recommend-card-highlight">
      <template #header>
        <div class="recommend-header">
          <h3>浏览过此商品的人也买了</h3>
          <el-tag type="success">协同过滤推荐</el-tag>
        </div>
      </template>
      <el-row v-if="alsoBuyProducts.length > 0" :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" v-for="item in alsoBuyProducts" :key="item.id">
          <div class="recommend-item" @click="goToProduct(item.id)">
            <img :src="getImageUrl(item.image)" class="recommend-image">
            <div class="recommend-info">
              <h4>{{ item.name }}</h4>
              <span class="recommend-price">¥{{ item.price }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-empty v-else description="暂无共同购买数据，系统会优先展示同分类热门商品">
        <el-button type="primary" @click="$router.push('/products')">查看更多商品</el-button>
      </el-empty>
    </el-card>

    <!-- 同类推荐 -->
    <el-card class="recommend-card">
      <template #header>
        <h3>🔥 同类推荐</h3>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" v-for="item in recommendProducts" :key="item.id">
          <div class="recommend-item" @click="goToProduct(item.id)">
            <img :src="getImageUrl(item.image)" class="recommend-image">
            <div class="recommend-info">
              <h4>{{ item.name }}</h4>
              <span class="recommend-price">¥{{ item.price }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart, CircleCheck, Star } from '@element-plus/icons-vue'
import { getProductDetail } from '@/api/product'
import { addToCart as addToCartApi } from '@/api/cart'
import { getProductList } from '@/api/product'
import { recordBrowse } from '@/api/browse'
import { getAlsoBuy } from '@/api/recommend'
import { getImageUrl } from '@/utils/image'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const product = ref(null)
const currentImage = ref('')
const quantity = ref(1)
const adding = ref(false)
const recommendProducts = ref([])
const alsoBuyProducts = ref([])
const browseStartTime = ref(null)
const browseReported = ref(false)

// 图片放大镜相关
const showMagnifier = ref(false)
const mainImageRef = ref(null)
const magnifierStyle = ref({})
const magnifierImgStyle = ref({})

const productImages = computed(() => {
  if (!product.value) return []
  const images = []
  if (product.value.image) {
    images.push(product.value.image)
  }
  return images
})

// 处理鼠标移动，实现放大镜效果
const handleMouseMove = (e) => {
  if (!mainImageRef.value) return

  const rect = mainImageRef.value.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top

  // 计算放大镜位置
  magnifierStyle.value = {
    left: `${x - 100}px`,
    top: `${y - 100}px`,
    display: 'block'
  }

  // 计算图片放大位置
  const xPercent = x / rect.width
  const yPercent = y / rect.height

  magnifierImgStyle.value = {
    transform: `scale(2) translate(-${xPercent * 50}%, -${yPercent * 50}%)`,
    transformOrigin: 'top left'
  }
}

// 获取库存
const getStock = () => {
  return product.value?.stock || 0
}

const categoryName = computed(() => {
  return product.value?.categoryName || '暂无分类'
})

const fetchProductDetail = async () => {
  const id = route.params.id
  if (!id) {
    ElMessage.error('商品ID不存在')
    router.push('/products')
    return
  }

  loading.value = true
  try {
    const res = await getProductDetail(id)
    product.value = res.data
    currentImage.value = res.data.image || ''
    browseStartTime.value = Date.now()
    browseReported.value = false

    // 获取同类推荐商品（同分类的其他商品，排除当前商品）
    try {
      const alsoBuyRes = await getAlsoBuy(product.value.id, { limit: 4 })
      alsoBuyProducts.value = alsoBuyRes.data || []
    } catch (recError) {
      console.warn('获取协同推荐失败', recError)
      alsoBuyProducts.value = []
    }

    try {
      if (product.value.categoryId) {
        const recommendRes = await getProductList({
          categoryId: product.value.categoryId,
          pageNum: 1,
          pageSize: 4
        })
        // 过滤掉当前商品
        recommendProducts.value = (recommendRes.data.records || [])
          .filter(item => item.id !== product.value.id)
          .slice(0, 4)
      }
    } catch (recError) {
      console.warn('获取推荐商品失败', recError)
      recommendProducts.value = []
    }
  } catch (error) {
    ElMessage.error('获取商品详情失败')
    router.push('/products')
  } finally {
    loading.value = false
  }
}

const reportBrowseDuration = async () => {
  if (!product.value || !browseStartTime.value || browseReported.value) return
  browseReported.value = true
  const durationSeconds = Math.max(1, Math.round((Date.now() - browseStartTime.value) / 1000))
  try {
    await recordBrowse({
      productId: product.value.id,
      durationSeconds
    })
  } catch (error) {
    console.warn('记录浏览行为失败', error)
  }
}

const addToCart = async () => {
  if (!product.value) return

  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  adding.value = true
  try {
    await addToCartApi({
      productId: product.value.id,
      quantity: quantity.value
    })
    ElMessage.success('已添加到购物车')

    // 更新购物车数量
    const currentCount = parseInt(localStorage.getItem('cartCount') || '0')
    localStorage.setItem('cartCount', String(currentCount + quantity.value))
    location.reload() // 刷新页面以更新购物车图标
  } catch (error) {
    ElMessage.error(error.message || '添加失败')
  } finally {
    adding.value = false
  }
}

const buyNow = () => {
  if (!product.value) return

  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 跳转到订单确认页面，传递当前商品信息
  const productData = {
    id: product.value.id,
    productName: product.value.name,
    productPrice: product.value.price,
    productImage: product.value.image,
    quantity: quantity.value
  }

  router.push({
    path: '/order/confirm',
    query: {
      products: JSON.stringify(productData)
    }
  })
}

const goToProduct = (id) => {
  reportBrowseDuration()
  router.push(`/product/${id}`)
  // 重新加载页面
  setTimeout(() => {
    location.reload()
  }, 100)
}

onMounted(() => {
  fetchProductDetail()
  window.addEventListener('beforeunload', reportBrowseDuration)
})

onBeforeUnmount(() => {
  reportBrowseDuration()
  window.removeEventListener('beforeunload', reportBrowseDuration)
})
</script>

<style scoped>
.product-detail {
  max-width: 1400px;
  margin: 0 auto;
}

.detail-card {
  margin-bottom: 20px;
}

/* 图片区域 */
.image-section {
  position: sticky;
  top: 20px;
}

.main-image {
  width: 100%;
  height: 400px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  margin-bottom: 15px;
  position: relative;
  cursor: crosshair;
}

.main-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

/* 放大镜效果 */
.magnifier {
  position: absolute;
  width: 200px;
  height: 200px;
  border: 2px solid #ff6b6b;
  border-radius: 50%;
  pointer-events: none;
  overflow: hidden;
  z-index: 10;
  display: none;
}

.magnifier img {
  position: absolute;
  left: 0;
  top: 0;
  max-width: none;
  width: 100%;
  height: 100%;
}

.placeholder {
  color: #999;
  font-size: 16px;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border: 2px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.thumbnail:hover,
.thumbnail.active {
  border-color: #ff6b6b;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 商品信息区域 */
.info-section {
  padding: 20px 0;
}

.product-name {
  font-size: 28px;
  margin: 0 0 10px 0;
  color: #333;
}

.product-subtitle {
  font-size: 16px;
  color: #666;
  margin: 0 0 20px 0;
}

.price-section {
  background: linear-gradient(135deg, #fff5f5 0%, #fff 100%);
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.price-row,
.stock-row {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.price-row:last-child,
.stock-row:last-child {
  margin-bottom: 0;
}

.label {
  width: 80px;
  font-size: 14px;
  color: #666;
}

.price {
  font-size: 32px;
  color: #ff6b6b;
  font-weight: bold;
}

.stock {
  font-size: 14px;
}

.stock.in-stock {
  color: #67c23a;
}

.stock.out-stock {
  color: #f56c6c;
}

.divider {
  height: 1px;
  background-color: #eee;
  margin: 20px 0;
}

/* 数量选择 */
.quantity-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.quantity-section .label {
  width: 80px;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.action-buttons .el-button {
  flex: 1;
  height: 50px;
  font-size: 16px;
}

/* 服务保障 */
.service-guarantee {
  display: flex;
  gap: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.guarantee-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #666;
}

.guarantee-item .el-icon {
  color: #67c23a;
}

/* 商品详情 */
.product-description {
  margin-top: 30px;
}

.product-description h3 {
  margin: 0;
  color: #333;
}

.description-content {
  padding: 20px 0;
}

.detail-text {
  margin-top: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.detail-text h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.detail-text p {
  color: #666;
  line-height: 1.8;
}

/* 推荐商品 */
.recommend-card {
  margin-bottom: 20px;
}

.recommend-card-highlight {
  border: 2px solid #67c23a;
}

.recommend-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.recommend-header h3 {
  margin: 0;
}

.recommend-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.recommend-item:hover {
  border-color: #ff6b6b;
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.recommend-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}

.recommend-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.recommend-info h4 {
  margin: 0;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.recommend-price {
  font-size: 18px;
  color: #ff6b6b;
  font-weight: bold;
}

/* 响应式 */
@media (max-width: 768px) {
  .product-name {
    font-size: 22px;
  }

  .main-image {
    height: 300px;
  }

  .price {
    font-size: 24px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .service-guarantee {
    flex-wrap: wrap;
  }
}
</style>
