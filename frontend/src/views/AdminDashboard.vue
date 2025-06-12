<template>
  <div class="admin-dashboard">
    <h1>관리자 대시보드</h1>
    <p>여기는 관리자 전용 페이지입니다.</p>
    <button @click="handleLogout">로그아웃</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const handleLogout = () => {
  localStorage.removeItem('isAdminLoggedIn');
  router.push('/admin/login');
};
</script>

<style scoped>
.admin-dashboard {
  max-width: 600px;
  margin: 100px auto;
  text-align: center;
  font-family: 'Noto Sans KR', sans-serif;
}

.admin-dashboard h1 {
  font-size: 32px;
  margin-bottom: 20px;
}

.admin-dashboard p {
  font-size: 18px;
  margin-bottom: 20px;
}

.admin-dashboard button {
  padding: 10px 20px;
  background-color: #00834e;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.admin-dashboard button:hover {
  background-color: #00663b;
}
</style>
