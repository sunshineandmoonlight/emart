import request from '@/utils/request'

// 创建商品评价
export function createReview(data) {
  return request({
    url: '/review/create',
    method: 'post',
    data
  })
}

// 获取商品评价列表（分页）
export function getProductReviews(productId, params) {
  return request({
    url: `/review/product/${productId}`,
    method: 'get',
    params
  })
}

// 获取用户评价列表（分页）
export function getUserReviews(params) {
  return request({
    url: '/review/user',
    method: 'get',
    params
  })
}

// 获取商品平均评分
export function getAverageRating(productId) {
  return request({
    url: `/review/average/${productId}`,
    method: 'get'
  })
}

// 获取商品评价总数
export function getReviewCount(productId) {
  return request({
    url: `/review/count/${productId}`,
    method: 'get'
  })
}

// 检查用户是否已评价该订单项
export function hasReviewed(orderItemId) {
  return request({
    url: `/review/check/${orderItemId}`,
    method: 'get'
  })
}
