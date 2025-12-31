import request from '@/utils/request'

// 添加收藏
export function addFavorite(productId) {
  return request({
    url: `/favorite/add/${productId}`,
    method: 'post'
  })
}

// 取消收藏
export function removeFavorite(productId) {
  return request({
    url: `/favorite/remove/${productId}`,
    method: 'post'
  })
}

// 检查是否已收藏
export function checkFavorite(productId) {
  return request({
    url: `/favorite/check/${productId}`,
    method: 'get'
  })
}

// 获取收藏列表
export function getFavoriteList(params) {
  return request({
    url: '/favorite/list',
    method: 'get',
    params
  })
}

// 获取收藏的商品ID列表
export function getFavoriteProductIds() {
  return request({
    url: '/favorite/product-ids',
    method: 'get'
  })
}
