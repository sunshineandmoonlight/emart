import request from '@/utils/request'

export function getAnalysisOverview() {
  return request({
    url: '/analysis/overview',
    method: 'get'
  })
}

export function getAnalysisSalesTrend(params) {
  return request({
    url: '/analysis/sales-trend',
    method: 'get',
    params
  })
}

export function getSalesForecast() {
  return request({
    url: '/analysis/sales-forecast',
    method: 'get'
  })
}

export function getProductRanking(params) {
  return request({
    url: '/analysis/product-ranking',
    method: 'get',
    params
  })
}

export function getCategorySales() {
  return request({
    url: '/analysis/category-sales',
    method: 'get'
  })
}

export function getStockReport() {
  return request({
    url: '/analysis/stock-report',
    method: 'get'
  })
}

export function getOrderStatusReport() {
  return request({
    url: '/analysis/order-status-report',
    method: 'get'
  })
}

export function getAnomalies() {
  return request({
    url: '/analysis/anomalies',
    method: 'get'
  })
}

export function getUserProfiles() {
  return request({
    url: '/analysis/user-profiles',
    method: 'get'
  })
}
