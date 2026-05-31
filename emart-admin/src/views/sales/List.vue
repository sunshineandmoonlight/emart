<template>
  <div class="sales-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售人员管理</span>
          <el-button type="primary" @click="openCreateDialog">新增销售人员</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="账号" width="140" />
        <el-table-column prop="nickName" label="姓名/昵称" width="140" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="loginTime" label="最后登录" width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openResetDialog(row)">重置密码</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="fetchList"
        @current-change="fetchList"
      />
    </el-card>

    <el-dialog v-model="createDialogVisible" title="新增销售人员" width="480px">
      <el-form ref="createFormRef" :model="createForm" :rules="createRules" label-width="90px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="createForm.username" placeholder="请输入销售人员账号" />
        </el-form-item>
        <el-form-item label="初始密码" prop="password">
          <el-input v-model="createForm.password" type="password" show-password placeholder="请输入初始密码" />
        </el-form-item>
        <el-form-item label="姓名/昵称" prop="nickName">
          <el-input v-model="createForm.nickName" placeholder="请输入姓名或昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="createForm.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleCreate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="resetDialogVisible" title="重置密码" width="420px">
      <el-form ref="resetFormRef" :model="resetForm" :rules="resetRules" label-width="90px">
        <el-form-item label="销售账号">
          <el-input :model-value="currentSales?.username" disabled />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetForm.password" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleResetPassword">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createSales, deleteSales, getSalesList, resetSalesPassword } from '@/api/sales'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const createDialogVisible = ref(false)
const resetDialogVisible = ref(false)
const createFormRef = ref()
const resetFormRef = ref()
const currentSales = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const createForm = reactive({
  username: '',
  password: '',
  nickName: '',
  email: ''
})

const resetForm = reactive({
  password: ''
})

const createRules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }]
}

const resetRules = {
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getSalesList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('获取销售人员列表失败')
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  Object.assign(createForm, {
    username: '',
    password: '',
    nickName: '',
    email: ''
  })
  createDialogVisible.value = true
}

const handleCreate = () => {
  createFormRef.value.validate(async valid => {
    if (!valid) return
    submitting.value = true
    try {
      await createSales(createForm)
      ElMessage.success('销售人员添加成功')
      createDialogVisible.value = false
      fetchList()
    } catch (error) {
      ElMessage.error('销售人员添加失败')
    } finally {
      submitting.value = false
    }
  })
}

const openResetDialog = (row) => {
  currentSales.value = row
  resetForm.password = ''
  resetDialogVisible.value = true
}

const handleResetPassword = () => {
  resetFormRef.value.validate(async valid => {
    if (!valid || !currentSales.value) return
    submitting.value = true
    try {
      await resetSalesPassword(currentSales.value.id, resetForm.password)
      ElMessage.success('密码重置成功')
      resetDialogVisible.value = false
    } catch (error) {
      ElMessage.error('密码重置失败')
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除销售人员 ${row.username} 吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await deleteSales(row.id)
    ElMessage.success('销售人员删除成功')
    fetchList()
  }).catch(() => {})
}

onMounted(fetchList)
</script>

<style scoped>
.sales-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>
