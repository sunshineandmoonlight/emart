import request from '@/utils/request'

export function recordBrowse(data) {
  return request({
    url: '/browse/record',
    method: 'post',
    data
  })
}
