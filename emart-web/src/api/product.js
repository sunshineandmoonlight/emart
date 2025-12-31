import request from '@/utils/request'

// 获取商品列表
export function getProductList(data) {
  return request({
    url: '/product/list',
    method: 'post',
    data
  })
}

// 获取商品详情
export function getProductDetail(id) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

// 获取商品分类列表
export function getCategoryList() {
  return request({
    url: '/category/listAll',
    method: 'get'
  })
}

// 获取商品分类树
export function getCategoryTree() {
  return request({
    url: '/category/tree',
    method: 'get'
  })
}
