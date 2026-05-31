import request from '@/utils/request'

export function getAlsoBuy(productId, params) {
  return request({
    url: `/recommend/also-buy/${productId}`,
    method: 'get',
    params
  })
}

export function getUserRecommend(params) {
  return request({
    url: '/recommend/user',
    method: 'get',
    params
  })
}
