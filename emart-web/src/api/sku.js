import request from '@/utils/request'

/**
 * 获取商品SKU列表
 */
export function getProductSkus(productId) {
  return request({
    url: `/sku/product/${productId}`,
    method: 'get'
  })
}

/**
 * 根据ID获取SKU
 */
export function getSkuById(id) {
  return request({
    url: `/sku/${id}`,
    method: 'get'
  })
}

/**
 * 创建SKU
 */
export function createSku(data) {
  return request({
    url: '/sku/create',
    method: 'post',
    data
  })
}

/**
 * 更新SKU
 */
export function updateSku(data) {
  return request({
    url: '/sku/update',
    method: 'put',
    data
  })
}

/**
 * 删除SKU
 */
export function deleteSku(id) {
  return request({
    url: `/sku/${id}`,
    method: 'delete'
  })
}

/**
 * 更新SKU库存
 */
export function updateSkuStock(skuId, quantity) {
  return request({
    url: `/sku/stock/${skuId}/${quantity}`,
    method: 'put'
  })
}
