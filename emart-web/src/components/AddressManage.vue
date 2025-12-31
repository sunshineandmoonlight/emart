<template>
  <div class="address-manage">
    <div class="address-header">
      <h3>收货地址</h3>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        添加新地址
      </el-button>
    </div>

    <el-row :gutter="20" v-if="addressList.length > 0">
      <el-col :xs="24" :sm="12" :md="8" v-for="address in addressList" :key="address.id">
        <el-card class="address-card" :class="{ default: address.isDefault }">
          <div class="address-content">
            <div class="address-header-line">
              <span class="name">{{ address.receiverName }}</span>
              <span class="phone">{{ address.receiverPhone }}</span>
              <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
            </div>
            <div class="address-detail">{{ address.receiverAddress }}</div>
            <div class="address-actions">
              <el-button type="text" @click="setDefault(address)" v-if="!address.isDefault">
                设为默认
              </el-button>
              <el-button type="text" @click="editAddress(address)">编辑</el-button>
              <el-button type="text" @click="deleteAddress(address)" class="delete-btn">
                删除
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-else description="暂无收货地址，请添加"></el-empty>

    <!-- 添加/编辑地址对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑地址' : '添加地址'"
      width="500px"
    >
      <el-form :model="addressForm" :rules="rules" ref="formRef" label-width="100px">
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
        <el-form-item label="设为默认">
          <el-switch v-model="addressForm.isDefault"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const emit = defineEmits(['address-selected'])

const addressList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref()

const addressForm = reactive({
  id: null,
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  isDefault: false
})

const rules = {
  receiverName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
}

// 从localStorage加载地址列表
const loadAddresses = () => {
  const saved = localStorage.getItem('userAddresses')
  if (saved) {
    try {
      addressList.value = JSON.parse(saved)
    } catch (e) {
      console.error('解析地址数据失败', e)
      addressList.value = []
    }
  }
}

// 保存地址列表到localStorage
const saveAddresses = () => {
  localStorage.setItem('userAddresses', JSON.stringify(addressList.value))
  // 如果有默认地址，保存到单独的key
  const defaultAddr = addressList.value.find(addr => addr.isDefault)
  if (defaultAddr) {
    localStorage.setItem('defaultAddress', JSON.stringify(defaultAddr))
  }
}

// 显示添加对话框
const showAddDialog = () => {
  isEdit.value = false
  Object.assign(addressForm, {
    id: Date.now(),
    receiverName: '',
    receiverPhone: '',
    receiverAddress: '',
    isDefault: false
  })
  dialogVisible.value = true
}

// 编辑地址
const editAddress = (address) => {
  isEdit.value = true
  Object.assign(addressForm, address)
  dialogVisible.value = true
}

// 保存地址
const saveAddress = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      saving.value = true

      setTimeout(() => {
        if (isEdit.value) {
          // 编辑模式
          const index = addressList.value.findIndex(addr => addr.id === addressForm.id)
          if (index > -1) {
            addressList.value[index] = { ...addressForm }

            // 如果设置为默认，取消其他默认地址
            if (addressForm.isDefault) {
              addressList.value.forEach((addr, i) => {
                if (i !== index) addr.isDefault = false
              })
            }
          }
        } else {
          // 添加模式
          if (addressForm.isDefault) {
            // 取消其他默认地址
            addressList.value.forEach(addr => addr.isDefault = false)
          }
          addressList.value.push({ ...addressForm })
        }

        saveAddresses()
        dialogVisible.value = false
        saving.value = false
        ElMessage.success(isEdit.value ? '地址修改成功' : '地址添加成功')
      }, 300)
    }
  })
}

// 删除地址
const deleteAddress = (address) => {
  ElMessageBox.confirm('确定要删除这个地址吗？', '提示', {
    type: 'warning'
  }).then(() => {
    addressList.value = addressList.value.filter(addr => addr.id !== address.id)
    saveAddresses()
    ElMessage.success('地址删除成功')
  }).catch(() => {})
}

// 设置默认地址
const setDefault = (address) => {
  addressList.value.forEach(addr => {
    addr.isDefault = addr.id === address.id
  })
  saveAddresses()
  ElMessage.success('已设为默认地址')
}

onMounted(() => {
  loadAddresses()
})

// 暴露方法供父组件调用
defineExpose({
  loadAddresses
})
</script>

<style scoped>
.address-manage {
  padding: 20px 0;
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.address-header h3 {
  margin: 0;
  color: #333;
}

.address-card {
  margin-bottom: 20px;
  border: 2px solid #eee;
  transition: all 0.3s;
}

.address-card:hover {
  border-color: #ff6b6b;
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.address-card.default {
  border-color: #67c23a;
  background: linear-gradient(135deg, #f0fff4 0%, #fff 100%);
}

.address-content {
  padding: 10px;
}

.address-header-line {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.phone {
  font-size: 14px;
  color: #666;
}

.address-detail {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.address-actions {
  display: flex;
  gap: 10px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.address-actions .el-button {
  padding: 5px 10px;
}

.delete-btn {
  color: #f56c6c;
}

.delete-btn:hover {
  color: #f56c6c;
  background-color: #fef0f0;
}

@media (max-width: 768px) {
  .address-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>
