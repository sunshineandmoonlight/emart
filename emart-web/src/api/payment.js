import request from '@/utils/request'

/**
 * 发起支付
 */
export function payOrder(orderId) {
  return request({
    url: `/payment/pay/${orderId}`,
    method: 'post'
  })
}
