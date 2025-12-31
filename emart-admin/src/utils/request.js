import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('admin_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端返回的是CommonResult格式: {code: 200, message: "success", data: {...}}
    if (res.code === 200) {
      return { data: res.data, message: res.message }
    } else if (res.code === 401) {
      ElMessage.error('请先登录')
      localStorage.removeItem('admin_token')
      window.location.href = '/login'
      return Promise.reject(new Error('未授权'))
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
