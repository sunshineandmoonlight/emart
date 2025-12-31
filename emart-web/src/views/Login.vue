<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>用户登录</h2>
      </template>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%;">
            登录
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="text" @click="$router.push('/register')">
            还没有账号？立即注册
          </el-button>
        </el-form-item>

        <el-form-item style="text-align: center; border-top: 1px solid #eee; padding-top: 15px;">
          <a :href="adminUrl" target="_blank" style="color: #909399; text-decoration: none; font-size: 14px;">
            管理员入口
          </a>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

// 动态生成管理员入口URL
const adminUrl = computed(() => {
  const currentHost = window.location.hostname
  const adminPort = '5175'

  if (currentHost === 'localhost' || currentHost === '127.0.0.1') {
    return `http://localhost:${adminPort}/login`
  } else {
    return `http://${currentHost}:${adminPort}/login`
  }
})

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  await loginFormRef.value.validate()

  loading.value = true
  try {
    const res = await login(loginForm)
    // 保存token和用户信息
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', loginForm.username)

    ElMessage.success('登录成功')
    router.push('/products')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
}

h2 {
  margin: 0;
  text-align: center;
}
</style>
