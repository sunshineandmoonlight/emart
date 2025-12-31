<template>
  <div class="product-form">
    <el-card>
      <template #header>
        <span>{{ isEdit ? '编辑商品' : '添加商品' }}</span>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>

        <el-form-item label="副标题" prop="subTitle">
          <el-input v-model="form.subTitle" placeholder="请输入副标题"></el-input>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>

        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0"></el-input-number>
        </el-form-item>

        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option label="图书" :value="1"></el-option>
            <el-option label="电子产品" :value="2"></el-option>
            <el-option label="服装" :value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌"></el-input>
        </el-form-item>

        <el-form-item label="商品图片" prop="image">
          <el-upload
            class="upload-demo"
            action="/api/file/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png文件，且不超过2MB
              </div>
            </template>
          </el-upload>
          <div v-if="form.image" class="image-preview">
            <img :src="getImageUrl(form.image)" style="width: 200px; height: 200px; object-fit: cover;">
            <el-button type="danger" size="small" @click="form.image = ''">删除</el-button>
          </div>
        </el-form-item>

        <el-form-item label="商品详情" prop="detail">
          <el-input
            v-model="form.detail"
            type="textarea"
            :rows="4"
            placeholder="请输入商品详情"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">提交</el-button>
          <el-button @click="router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createProduct, updateProduct, getProductDetail } from '@/api/product'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const loading = ref(false)

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  name: '',
  subTitle: '',
  price: 0,
  stock: 0,
  categoryId: null,
  brand: '',
  detail: '',
  image: ''
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是JPG/PNG格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过2MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    form.image = response.data
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const getImageUrl = (imagePath) => {
  if (!imagePath) return ''
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }
  return 'http://localhost:8080' + imagePath
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit.value) {
          await updateProduct(route.params.id, form)
          ElMessage.success('更新成功')
        } else {
          await createProduct(form)
          ElMessage.success('创建成功')
        }
        router.push('/product/list')
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getProductDetail(route.params.id)
      const product = res.data
      form.name = product.name
      form.subTitle = product.subTitle || ''
      form.price = product.price
      form.stock = product.stock
      form.categoryId = product.categoryId
      form.brand = product.brand || ''
      form.detail = product.detail || ''
      form.image = product.image || ''
    } catch (error) {
      ElMessage.error('加载商品数据失败')
    }
  }
})
</script>

<style scoped>
.product-form {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.image-preview {
  margin-top: 10px;
  position: relative;
  display: inline-block;
}

.image-preview img {
  border: 1px solid #ddd;
  border-radius: 4px;
}

.image-preview .el-button {
  position: absolute;
  bottom: 5px;
  right: 5px;
}
</style>
