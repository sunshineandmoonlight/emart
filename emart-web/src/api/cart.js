import request from '@/utils/request'

// 添加商品到购物车
export function addToCart(data) {
  return request({
    url: '/cart/add',
    method: 'post',
    data
  })
}

// 获取购物车列表
export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

// 更新购物车项数量
export function updateCartItem(productId, data) {
  return request({
    url: `/cart/update/${productId}`,
    method: 'post',
    data
  })
}

// 删除购物车项
export function deleteCartItem(productId) {
  return request({
    url: `/cart/delete/${productId}`,
    method: 'post'
  })
}

// 清空购物车
export function clearCart() {
  return request({
    url: '/cart/clear',
    method: 'post'
  })
}
