<template>
  <div class="admin-layout">
    <!-- 사이드 메뉴 -->
    <aside class="admin-sidebar">
      <img
        src="@/assets/gs1.jpg"
        alt="GS THE FRESH 로고"
        class="sidebar-logo"
      />
      <nav>
        <button @click="goTo('orders')" :class="{ active: isActive('orders') }">
          주문 현황
        </button>
        <button
          @click="goTo('order-cards')"
          :class="{ active: isActive('order-cards') }"
        >
          주문 상세
        </button>
        <button @click="goTo('stats')" :class="{ active: isActive('stats') }">
          주문 통계
        </button>
        <button
          @click="goTo('products')"
          :class="{ active: isActive('products') }"
        >
          상품 등록
        </button>
        <button @click="goTo('users')" :class="{ active: isActive('users') }">
          고객 관리
        </button>
      </nav>
    </aside>

    <!-- 메인 영역 -->
    <div class="admin-main">
      <!-- 공통 Header 적용 -->
      <Header />

      <!-- 콘텐츠 영역 -->
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import Header from '@/components/Header.vue';

const router = useRouter();
const route = useRoute();

const goTo = (path) => {
  router.push(`/admin/dashboard/${path}`);
};

const isActive = (path) => {
  return route.path.includes(`/admin/dashboard/${path}`);
};
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  font-family: 'Noto Sans KR', sans-serif;
}

/* 사이드바 */
.admin-sidebar {
  width: 220px;
  background-color: rgb(0, 87, 63);
  color: white;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.sidebar-logo {
  width: 150px;
  margin-bottom: 30px;
}

.admin-sidebar nav button {
  width: 100%;
  background: none;
  border: none;
  color: white;
  font-size: 16px;
  padding: 12px 0;
  cursor: pointer;
  border-radius: 6px;
  margin-bottom: 8px;
  transition: background-color 0.2s ease;
}

.admin-sidebar nav button:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.admin-sidebar nav button.active {
  background-color: rgba(255, 255, 255, 0.2);
  font-weight: bold;
}

/* 메인 영역 */
.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.admin-header {
  background-color: #f5f5f5;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
}

.admin-welcome {
  font-size: 16px;
  font-weight: bold;
}

.logout-btn {
  background-color: rgb(0, 87, 63);
  color: white;
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: rgb(0, 70, 50);
}

.admin-content {
  flex: 1;
  padding: 20px;
  background-color: #fafafa;
  min-width: 0;
}
</style>
