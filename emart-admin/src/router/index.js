import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: 'product/list',
        name: 'ProductList',
        component: () => import('@/views/product/List.vue')
      },
      {
        path: 'product/create',
        name: 'ProductCreate',
        component: () => import('@/views/product/Form.vue')
      },
      {
        path: 'product/edit/:id',
        name: 'ProductEdit',
        component: () => import('@/views/product/Form.vue')
      },
      {
        path: 'order/list',
        name: 'OrderList',
        component: () => import('@/views/order/List.vue')
      },
      {
        path: 'user/list',
        name: 'UserList',
        component: () => import('@/views/user/List.vue')
      },
      {
        path: 'browse/list',
        name: 'BrowseList',
        component: () => import('@/views/browse/List.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('admin_token')

  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
