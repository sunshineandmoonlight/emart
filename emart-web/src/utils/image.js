/**
 * 获取完整的图片URL
 * @param {string} imagePath - 图片相对路径
 * @returns {string} 完整的图片URL
 */
export function getImageUrl(imagePath) {
  if (!imagePath) {
    return '/placeholder.jpg'
  }

  // 如果已经是完整URL，直接返回
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }

  // 拼接后端URL
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  return `${baseUrl}${imagePath.startsWith('/') ? '' : '/'}${imagePath}`
}
