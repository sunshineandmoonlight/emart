import request from '@/utils/request'

// 获取浏览日志
export function getBrowseLogs(params) {
  return request({
    url: '/browse/logs',
    method: 'get',
    params
  })
}

// 获取热门商品
export function getHotProducts() {
  return request({
    url: '/browse/hot',
    method: 'get'
  })
}
