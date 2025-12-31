import request from '@/utils/request'

// 获取所有订单列表
export function getAllOrders(params) {
  return request({
    url: '/order/all',
    method: 'get',
    params
  })
}

// 发货
export function shipOrder(orderId, trackingNo) {
  return request({
    url: `/order/ship/${orderId}`,
    method: 'post',
    data: { trackingNo }
  })
}

// 获取销售统计
export function getSalesStats() {
  return request({
    url: '/order/stats',
    method: 'get'
  })
}

// 获取销售趋势（最近7天）
export function getSalesTrend() {
  return request({
    url: '/order/sales-trend',
    method: 'get'
  })
}

// 获取热门商品（Top10）
export function getHotProducts() {
  return request({
    url: '/order/hot-products',
    method: 'get'
  })
}

// 获取订单详情
export function getOrderDetail(orderNo) {
  return request({
    url: `/order/detail/${orderNo}`,
    method: 'get'
  })
}
