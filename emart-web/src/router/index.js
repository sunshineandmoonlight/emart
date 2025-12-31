import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomePage.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/products',
    name: 'ProductList',
    component: () => import('@/views/ProductList.vue')
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: () => import('@/views/ProductDetail.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/views/Cart.vue')
  },
  {
    path: '/order/confirm',
    name: 'OrderConfirm',
    component: () => import('@/views/OrderConfirm.vue')
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('@/views/Order.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue')
  },
  {
    path: '/payment/return',
    name: 'PaymentReturn',
    component: () => import('@/views/PaymentReturn.vue')
  },
  {
    path: '/payment/simulate',
    name: 'PaymentSimulate',
    component: () => import('@/views/PaymentSimulate.vue')
  },
  {
    path: '/favorite',
    name: 'Favorite',
    component: () => import('@/views/Favorite.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 不需要登录的页面
  const publicPages = ['/', '/login', '/register', '/products', '/payment/return']

  // 商品详情页也不需要登录
  if (to.path.startsWith('/product/')) {
    next()
    return
  }

  if (publicPages.includes(to.path)) {
    next()
  } else {
    // 需要登录的页面
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
