<template>
  <el-dialog
    v-model="visible"
    title="商品评价"
    width="600px"
    @close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="商品名称">
        <span>{{ productName }}</span>
      </el-form-item>

      <el-form-item label="评分" prop="rating">
        <el-rate v-model="form.rating" :texts="['极差', '失望', '一般', '满意', '惊喜']" show-text />
      </el-form-item>

      <el-form-item label="评价内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="5"
          placeholder="分享您的使用体验，帮助更多人了解该商品"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="SKU信息">
        <el-input v-model="form.skuInfo" placeholder="如：颜色:红色 尺寸:L" />
      </el-form-item>

      <el-form-item label="匿名评价">
        <el-switch v-model="isAnonymous" active-text="是" inactive-text="否" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="submitReview" :loading="submitting">
        提交评价
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { createReview } from '@/api/review'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  productId: {
    type: Number,
    required: true
  },
  productName: {
    type: String,
    default: ''
  },
  orderId: {
    type: Number,
    required: true
  },
  orderItemId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref(null)
const submitting = ref(false)
const isAnonymous = ref(false)

const form = reactive({
  rating: 5,
  content: '',
  skuInfo: '',
  isAnonymous: 0
})

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 5, message: '评价内容至少5个字', trigger: 'blur' }
  ]
}

const visible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const resetForm = () => {
  form.rating = 5
  form.content = ''
  form.skuInfo = ''
  isAnonymous.value = false
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleClose = () => {
  resetForm()
  visible.value = false
}

const submitReview = async () => {
  try {
    await formRef.value.validate()

    submitting.value = true

    const reviewData = {
      productId: props.productId,
      orderId: props.orderId,
      orderItemId: props.orderItemId,
      rating: form.rating,
      content: form.content,
      skuInfo: form.skuInfo || null,
      isAnonymous: isAnonymous.value ? 1 : 0
    }

    await createReview(reviewData)
    ElMessage.success('评价提交成功')
    emit('success')
    handleClose()
  } catch (error) {
    if (error !== false) { // 表单验证失败时会返回false
      ElMessage.error(error.message || '提交失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
:deep(.el-rate__text) {
  color: #ff9900;
}
</style>
