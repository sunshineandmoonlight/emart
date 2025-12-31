import request from '@/utils/request'

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

// 禁用/启用用户
export function updateUserStatus(userId, status) {
  return request({
    url: `/user/status/${userId}`,
    method: 'post',
    params: { status }
  })
}
