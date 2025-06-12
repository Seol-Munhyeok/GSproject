<template>
  <div class="admin-login-page">
    <div class="login-box">
      <!-- 상단 GS 로고 -->
      <div class="login-logo">
        <img src="@/assets/gs1.jpg" alt="GS THE FRESH" />
      </div>

      <!-- 로그인 폼 -->
      <form @submit.prevent="handleLogin">
        <input
          type="text"
          v-model="employeeId"
          placeholder="이메일을 입력하세요"
          required
        />
        <input
          type="password"
          v-model="password"
          placeholder="비밀번호를 입력하세요"
          required
        />

        <button type="submit">로그인</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const employeeId = ref('');
const password = ref('');

const handleLogin = async () => {
  console.log('로그인 시도:', employeeId.value, password.value);

  try {
    const response = await axios.post('/api/admin/login', {
      email: employeeId.value,
      password: password.value,
    });

    if (response.data.success) {
      localStorage.setItem('isAdminLoggedIn', 'true');
      router.push('/admin/dashboard');
    } else {
      alert(response.data.message);
    }
  } catch (e) {
    console.error('로그인 에러:', e);
    alert('서버 오류로 로그인에 실패했습니다.');
  }
};
</script>

<style scoped>
.admin-login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f6f8;
}

.login-box {
  background-color: #ffffff;
  padding: 40px 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 360px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.login-logo {
  margin-bottom: 20px;
  align-self: center; /* 로고 중앙 정렬 */
}

.login-logo img {
  width: 140px;
  height: auto;
}

.login-box input {
  width: 100%;
  padding: 12px 15px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  box-sizing: border-box;
  transition: border 0.3s;
}

.login-box input:focus {
  border-color: #00834e;
  outline: none;
}

.login-options {
  display: flex; /* flexbox를 사용하여 내부 요소 정렬 */
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  margin-bottom: 20px;
  font-size: 14px;
  color: #555;
}

.login-options label {
  display: flex; /* label 자체를 flex 컨테이너로 만들어 내부 요소 정렬 */
  align-items: center; /* 체크박스와 텍스트를 세로 중앙 정렬 */
  cursor: pointer; /* 클릭 가능한 요소임을 나타냄 */
}

.login-options input[type='checkbox'] {
  margin-right: 6px;
  /* 체크박스의 기본 외형을 유지하면서 필요에 따라 크기 조절 */
  width: auto; /* 너비를 자동으로 설정하여 공간 차지 최소화 */
  margin-bottom: 0; /* input에 설정된 margin-bottom 제거 */
}

.login-box button {
  width: 100%;
  padding: 12px 15px;
  background-color: #00834e;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-box button:hover {
  background-color: #00663b;
}
</style>
