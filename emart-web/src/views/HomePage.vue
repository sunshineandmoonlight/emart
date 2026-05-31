<template>
  <div class="home-page">
    <!-- 轮播图区域 - 改为商品推荐 -->
    <el-card class="banner-card" :body-style="{ padding: 0 }">
      <el-carousel height="400px" :interval="5000" arrow="always">
        <el-carousel-item v-for="product in featuredProducts" :key="product.id">
          <div class="banner-item product-banner" @click="goToProduct(product.id)">
            <img :src="getImageUrl(product.image)" class="banner-image">
            <div class="banner-overlay">
              <div class="banner-content">
                <h2>{{ product.name }}</h2>
                <p class="product-subtitle">{{ product.subtitle }}</p>
                <div class="price-tag">
                  <span class="price">¥{{ product.price }}</span>
                  <el-button type="danger" size="large">立即抢购</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <!-- 个性化推荐 -->
    <el-card class="recommend-card-highlight">
      <template #header>
        <div class="card-header">
          <h3>猜你喜欢</h3>
          <el-tag type="success">个性化推荐</el-tag>
        </div>
      </template>
      <el-row v-if="recommendProducts.length > 0" :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in recommendProducts" :key="product.id">
          <div class="product-card" @click="goToProduct(product.id)">
            <img :src="getImageUrl(product.image)" class="product-image">
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <p class="product-subtitle">{{ product.subtitle }}</p>
              <div class="product-footer">
                <span class="price">¥{{ product.price }}</span>
                <el-tag size="small">推荐</el-tag>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-empty v-else description="暂无个性化推荐，浏览或购买商品后会生成推荐">
        <el-button type="primary" @click="$router.push('/products')">去浏览商品</el-button>
      </el-empty>
    </el-card>

    <!-- 分类导航 -->
    <el-card class="category-card">
      <template #header>
        <div class="card-header">
          <h3>商品分类</h3>
        </div>
      </template>
      <div class="category-grid">
        <div
          v-for="category in categories"
          :key="category.id"
          class="category-item"
          @click="goToCategory(category.id)"
        >
          <el-icon class="category-icon"><Goods /></el-icon>
          <span>{{ category.name }}</span>
        </div>
      </div>
    </el-card>

    <!-- 热门商品 -->
    <el-card class="hot-products-card">
      <template #header>
        <div class="card-header">
          <h3>🔥 热门商品</h3>
          <el-button type="text" @click="$router.push('/products')">查看更多 →</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in hotProducts" :key="product.id">
          <div class="product-card" @click="goToProduct(product.id)">
            <img :src="getImageUrl(product.image)" class="product-image">
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <p class="product-subtitle">{{ product.subtitle }}</p>
              <div class="product-footer">
                <span class="price">¥{{ product.price }}</span>
                <span class="sales">已售 {{ product.sales || 0 }} 件</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 新品上架 -->
    <el-card class="new-products-card">
      <template #header>
        <div class="card-header">
          <h3>✨ 新品上架</h3>
          <el-button type="text" @click="$router.push('/products')">查看更多 →</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in newProducts" :key="product.id">
          <div class="product-card" @click="goToProduct(product.id)">
            <img :src="getImageUrl(product.image)" class="product-image">
            <div class="product-info">
              <h4 class="product-name">{{ product.name }}</h4>
              <p class="product-subtitle">{{ product.subtitle }}</p>
              <div class="product-footer">
                <span class="price">¥{{ product.price }}</span>
                <el-tag size="small" type="success">新品</el-tag>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 服务优势 -->
    <el-card class="features-card">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="feature-item">
            <el-icon class="feature-icon" color="#ff6b6b"><Van /></el-icon>
            <h4>正品保障</h4>
            <p>官方直供 品质保证</p>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="feature-item">
            <el-icon class="feature-icon" color="#ff6b6b"><Box /></el-icon>
            <h4>极速物流</h4>
            <p>次日送达 时效保证</p>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="feature-item">
            <el-icon class="feature-icon" color="#ff6b6b"><Stamp /></el-icon>
            <h4>售后无忧</h4>
            <p>7天退换 购物无忧</p>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="feature-item">
            <el-icon class="feature-icon" color="#ff6b6b"><Medal /></el-icon>
            <h4>特色服务</h4>
            <p>私人定制 专属服务</p>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Goods, Van, Box, Stamp, Medal } from '@element-plus/icons-vue'
import { getProductList, getCategoryList } from '@/api/product'
import { getUserRecommend } from '@/api/recommend'
import { getImageUrl } from '@/utils/image'

const router = useRouter()

const categories = ref([])
const featuredProducts = ref([]) // 轮播图推荐商品
const recommendProducts = ref([])
const hotProducts = ref([])
const newProducts = ref([])

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data || []
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

const fetchProducts = async () => {
  try {
    // 获取轮播图推荐商品（前5个）
    const featuredRes = await getProductList({ pageNum: 1, pageSize: 5, status: 1 })
    featuredProducts.value = featuredRes.data.records || []

    // 获取热门商品（第6-9个商品）
    const hotRes = await getProductList({ pageNum: 2, pageSize: 4, status: 1 })
    hotProducts.value = hotRes.data.records || []

    // 获取新品（第10-13个商品）
    const newRes = await getProductList({ pageNum: 3, pageSize: 4, status: 1 })
    newProducts.value = newRes.data.records || []
  } catch (error) {
    console.error('获取商品失败', error)
  }
}

const fetchRecommendProducts = async () => {
  try {
    const res = await getUserRecommend({ limit: 4 })
    recommendProducts.value = res.data || []
  } catch (error) {
    recommendProducts.value = []
  }
}

const goToCategory = (categoryId) => {
  router.push({
    path: '/products',
    query: { categoryId }
  })
}

const goToProduct = (productId) => {
  router.push(`/product/${productId}`)
}

onMounted(() => {
  fetchCategories()
  fetchProducts()
  fetchRecommendProducts()
})
</script>

<style scoped>
.home-page {
  max-width: 1400px;
  margin: 0 auto;
}

/* 轮播图样式 */
.banner-card {
  margin-bottom: 20px;
  overflow: hidden;
}

.banner-item {
  height: 100%;
  position: relative;
  cursor: pointer;
  overflow: hidden;
}

.product-banner .banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-banner:hover .banner-image {
  transform: scale(1.05);
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.3) 50%, transparent 100%);
  padding: 60px 40px 40px;
  color: white;
}

.banner-content {
  text-align: left;
  z-index: 2;
  max-width: 600px;
}

.banner-content h2 {
  font-size: 36px;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  color: white;
}

.product-subtitle {
  font-size: 16px;
  margin-bottom: 20px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  color: rgba(255, 255, 255, 0.9);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price-tag {
  display: flex;
  align-items: center;
  gap: 20px;
}

.price-tag .price {
  font-size: 32px;
  font-weight: bold;
  color: #ff6b6b;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

/* 分类导航 */
.category-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover {
  border-color: #ff6b6b;
  background-color: #fff5f5;
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.2);
}

.category-icon {
  font-size: 32px;
  color: #ff6b6b;
  margin-bottom: 10px;
}

.category-item span {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

/* 商品卡片 */
.recommend-card-highlight,
.hot-products-card,
.new-products-card {
  margin-bottom: 20px;
}

.recommend-card-highlight {
  border: 2px solid #67c23a;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
  margin-bottom: 20px;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  border-color: #ff6b6b;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #333;
}

.product-subtitle {
  font-size: 13px;
  color: #999;
  margin: 0 0 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #ff6b6b;
  font-size: 20px;
  font-weight: bold;
}

.sales {
  font-size: 12px;
  color: #999;
}

/* 服务优势 */
.features-card {
  background: linear-gradient(135deg, #fff5f5 0%, #fff 100%);
  border: none;
}

.feature-item {
  text-align: center;
  padding: 20px;
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-item h4 {
  font-size: 18px;
  margin: 10px 0;
  color: #333;
}

.feature-item p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .banner-content h2 {
    font-size: 32px;
  }

  .banner-content p {
    font-size: 18px;
  }

  .category-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 10px;
  }

  .category-item {
    padding: 15px;
  }
}
</style>
