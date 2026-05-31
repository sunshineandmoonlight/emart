import request from '@/utils/request'

export function getSalesList(params) {
  return request({
    url: '/admin/sales/list',
    method: 'get',
    params
  })
}

export function createSales(data) {
  return request({
    url: '/admin/sales/create',
    method: 'post',
    data
  })
}

export function deleteSales(id) {
  return request({
    url: `/admin/sales/delete/${id}`,
    method: 'post'
  })
}

export function resetSalesPassword(id, password) {
  return request({
    url: `/admin/sales/reset-password/${id}`,
    method: 'post',
    data: { password }
  })
}
