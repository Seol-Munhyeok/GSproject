<template>
  <header class="global-header">
    <div class="left-section">
      <button
        v-if="isAdminLoggedIn && !isAdminPage"
        @click="goToAdminDashboard"
        class="header-btn"
      >
        관리자 화면으로 이동
      </button>
      <button
        v-if="isAdminLoggedIn && isAdminPage"
        @click="goToProducts"
        class="header-btn"
      >
        주문 페이지로 이동
      </button>
    </div>

    <!-- 가운데 로고 (선택) -->
    <div class="logo-text">GS THE FRESH</div>

    <!-- 오른쪽: 로그인 / 로그아웃 버튼 -->
    <div class="right-section">
      <span v-if="isAdminLoggedIn && adminEmail" class="welcome-text">
        {{ adminEmail }}님, 반갑습니다!
      </span>
      <button v-if="isAdminLoggedIn" @click="handleLogout" class="header-btn">
        로그아웃
      </button>
      <button v-else @click="goToAdminLogin" class="header-btn">
        관리자 로그인
      </button>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ref } from 'vue';

const router = useRouter();
const route = useRoute();
const adminEmail = ref(localStorage.getItem('adminEmail'));

// 관리자 로그인 여부 확인
const isAdminLoggedIn = computed(() => {
  return localStorage.getItem('isAdminLoggedIn') === 'true';
});

// 현재 admin 페이지 여부 확인
const isAdminPage = computed(() => {
  return route.path.startsWith('/admin');
});

const goToProducts = () => {
  router.push('/products');
};

const goToAdminDashboard = () => {
  router.push('/admin/dashboard');
};

const goToAdminLogin = () => {
  router.push('/admin/login');
};

const handleLogout = () => {
  localStorage.removeItem('isAdminLoggedIn');
  localStorage.removeItem('adminEmail');
  router.push('/admin/login');
};
</script>

<style scoped>
.global-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background-color: rgb(0, 87, 63);
  color: white;
  font-family: 'Noto Sans KR', sans-serif;

  max-width: 100%;
  overflow-x: hidden;
  box-sizing: border-box;
  min-width: 0;
}

.left-section,
.right-section {
  display: flex;
  align-items: center;
}

.header-btn {
  background-color: white;
  color: rgb(0, 87, 63);
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  margin-left: 10px;
}

.header-btn:hover {
  background-color: #e6e6e6;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
}

.welcome-text {
  margin-right: 10px;
  font-weight: 500;
}
</style>
