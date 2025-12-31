import request from '@/utils/request'

// 获取商品列表
export function getProductList(params) {
  return request({
    url: '/product/list',
    method: 'post',
    data: params
  })
}

// 创建商品
export function createProduct(data) {
  return request({
    url: '/product/create',
    method: 'post',
    data
  })
}

// 更新商品
export function updateProduct(id, data) {
  return request({
    url: `/product/update/${id}`,
    method: 'post',
    data
  })
}

// 删除商品
export function deleteProduct(id) {
  return request({
    url: `/product/delete/${id}`,
    method: 'post'
  })
}

// 获取商品详情
export function getProductDetail(id) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

// 更新库存
export function updateStock(id, quantity) {
  return request({
    url: `/product/stock/${id}`,
    method: 'post',
    params: { quantity }
  })
}
