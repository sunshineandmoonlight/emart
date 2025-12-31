import request from '@/utils/request'

// 用户注册
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

// 用户退出
export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

// 注销账号
export function deleteAccount() {
  return request({
    url: '/user/delete-account',
    method: 'delete'
  })
}
