<template>
  <div class="product-list">
    <h2>商品列表</h2>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchProducts">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品列表 -->
    <el-row :gutter="20" class="product-grid">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in products" :key="product.id">
        <el-card :body-style="{ padding: '0px' }" class="product-card" @click="goToDetail(product.id)">
          <img :src="getImageUrl(product.image)" class="product-image">
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="subtitle">{{ product.subtitle }}</p>
            <div class="price-row">
              <span class="price">¥{{ product.price }}</span>
              <span class="stock">库存: {{ product.stock }}</span>
            </div>
            <div class="button-row">
              <el-button type="primary" size="small" @click="addToCart(product)" :disabled="product.stock === 0">
                加入购物车
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="searchForm.pageNum"
      v-model:page-size="searchForm.pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="fetchProducts"
      @current-change="fetchProducts"
      class="pagination"
    >
    </el-pagination>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductList, getCategoryList } from '@/api/product'
import { addToCart as addToCartApi } from '@/api/cart'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const route = useRoute()

const products = ref([])
const categories = ref([])
const total = ref(0)

const searchForm = reactive({
  name: '',
  categoryId: null,
  status: 1,
  pageNum: 1,
  pageSize: 12
})

const fetchProducts = async () => {
  try {
    const res = await getProductList(searchForm)
    products.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  }
}

const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data || []
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.categoryId = null
  searchForm.pageNum = 1
  fetchProducts()
  // 清除路由参数
  router.push({ path: '/products', query: {} })
}

const addToCart = async (product) => {
  try {
    await addToCartApi({
      productId: product.id,
      quantity: 1
    })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error(error.message || '添加失败')
  }
}

const goToDetail = (productId) => {
  router.push(`/product/${productId}`)
}

// 从路由query参数中提取搜索条件
const updateSearchFromQuery = () => {
  if (route.query.name) {
    searchForm.name = route.query.name
  }
  if (route.query.categoryId) {
    searchForm.categoryId = parseInt(route.query.categoryId)
  }
  if (route.query.pageNum) {
    searchForm.pageNum = parseInt(route.query.pageNum)
  }
}

// 监听路由变化
watch(() => route.query, () => {
  updateSearchFromQuery()
  fetchProducts()
}, { immediate: true })

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.product-list {
  max-width: 1400px;
  margin: 0 auto;
}

.search-card {
  margin-bottom: 20px;
}

.product-grid {
  margin-bottom: 20px;
}

.product-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.subtitle {
  color: #999;
  font-size: 13px;
  margin: 0 0 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

.stock {
  color: #909399;
  font-size: 13px;
}

.button-row {
  display: flex;
  justify-content: center;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
