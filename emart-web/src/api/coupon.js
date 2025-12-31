import request from '@/utils/request'

/**
 * 获取可领取的优惠券列表
 */
export function getAvailableCoupons() {
  return request({
    url: '/coupon/list',
    method: 'get'
  })
}

/**
 * 领取优惠券
 */
export function receiveCoupon(couponId) {
  return request({
    url: `/coupon/receive/${couponId}`,
    method: 'post'
  })
}

/**
 * 获取我的优惠券列表
 */
export function getMyCoupons(status) {
  return request({
    url: '/coupon/my',
    method: 'get',
    params: { status }
  })
}

/**
 * 根据ID获取优惠券
 */
export function getCouponById(id) {
  return request({
    url: `/coupon/${id}`,
    method: 'get'
  })
}

/**
 * 创建优惠券（管理端）
 */
export function createCoupon(data) {
  return request({
    url: '/coupon/create',
    method: 'post',
    data
  })
}

/**
 * 更新优惠券（管理端）
 */
export function updateCoupon(data) {
  return request({
    url: '/coupon/update',
    method: 'put',
    data
  })
}

/**
 * 删除优惠券（管理端）
 */
export function deleteCoupon(id) {
  return request({
    url: `/coupon/${id}`,
    method: 'delete'
  })
}
