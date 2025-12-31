import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

// 获取订单详情
export function getOrderDetail(orderNo) {
  return request({
    url: `/order/detail/${orderNo}`,
    method: 'get'
  })
}

// 获取用户订单列表
export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

// 获取用户订单详情列表（包含商品信息）
export function getOrderListWithDetails(params) {
  return request({
    url: '/order/list-with-details',
    method: 'get',
    params
  })
}

// 取消订单
export function cancelOrder(orderId) {
  return request({
    url: `/order/cancel/${orderId}`,
    method: 'post'
  })
}

// 完成订单
export function finishOrder(orderId) {
  return request({
    url: `/order/finish/${orderId}`,
    method: 'post'
  })
}

// 更新订单状态
export function updateOrderStatus(orderId, status) {
  return request({
    url: `/order/status/${orderId}`,
    method: 'post',
    params: { status }
  })
}
