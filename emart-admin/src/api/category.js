import request from '@/utils/request'

export function getCategoryList() {
  return request({
    url: '/category/listAll',
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/category/create',
    method: 'post',
    data
  })
}

export function updateCategory(id, data) {
  return request({
    url: `/category/update/${id}`,
    method: 'post',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/category/delete/${id}`,
    method: 'post'
  })
}
