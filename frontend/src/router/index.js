import { createRouter, createWebHistory } from 'vue-router';
import ProductsView from '../views/ProductView.vue';
import OrderView from '../views/OrderView.vue';
import CompleteView from '../views/CompleteView.vue';
import AdminLoginView from '../views/AdminLoginView.vue';
import AdminDashboard from '../views/AdminDashboard.vue';

const routes = [
  { path: '/', redirect: '/products' },
  { path: '/products', component: ProductsView },
  { path: '/order', component: OrderView },
  { path: '/complete', component: CompleteView },
  { path: '/admin/login', component: AdminLoginView },
  {
    path: '/admin/dashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true },
  },
  {
    path: '/admin/dashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/admin/dashboard/orders' },
      {
        path: 'orders',
        component: () => import('../views/AdminOrdersView.vue'),
      },
      { path: 'stats', component: () => import('../views/AdminStatsView.vue') },
      {
        path: 'products',
        component: () => import('../views/AdminProductsView.vue'),
      },
      { path: 'users', component: () => import('../views/AdminUsersView.vue') },
      {
        path: '/admin/dashboard/order-cards',
        name: 'OrderCards',
        component: () => import('@/views/OrderCardsView.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 라우트 가드 (관리자용)
router.beforeEach((to, from, next) => {
  if (
    to.meta.requiresAuth &&
    localStorage.getItem('isAdminLoggedIn') !== 'true'
  ) {
    next('/admin/login');
  } else {
    next();
  }
});

export default router;
