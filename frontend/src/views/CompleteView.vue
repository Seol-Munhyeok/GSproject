<template>
  <div class="complete-container" v-if="order">
    <div class="receipt-box">
      <h2 class="title">🧾 주문이 완료되었습니다</h2>
      <p class="subtitle">고객님의 소중한 주문 감사합니다.</p>

      <div class="info-row">
        <span class="label">주문번호:</span>
        <span class="value">#{{ order.id }}</span>
      </div>
      <div class="info-row">
        <span class="label">주문자명:</span>
        <span class="value">{{ order.customerName }}</span>
      </div>
      <div class="info-row">
        <span class="label">주문일시:</span>
        <span class="value">{{ formatDate(order.createdAt) }}</span>
      </div>

      <div class="item-list">
        <h3 class="section-title">주문 상품</h3>
        <div class="item-row" v-for="(item, index) in order.items" :key="index">
          <span class="item-name">{{ item.productName }}</span>
          <span class="item-price">
            {{ item.price.toLocaleString() }}원 × {{ item.quantity }}
          </span>
        </div>
      </div>

      <div class="total-row">
        <strong>총 결제 금액:</strong>
        <span>{{ order.totalPrice.toLocaleString() }}원</span>
      </div>

      <button class="back-btn" @click="goToOrder">주문화면으로 돌아가기</button>
    </div>
  </div>

  <div v-else class="loading">주문 정보를 불러오는 중...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const orderId = route.params.orderId;
const order = ref(null);

onMounted(async () => {
  const res = await axios.get(`/api/orders/${orderId}`);
  order.value = res.data;
});

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
}

function goToOrder() {
  router.push('/products');
}
</script>

<style scoped>
.complete-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem 1rem;
  background-color: #f8f8f8;
}

.receipt-box {
  width: 100%;
  max-width: 500px;
  background-color: #ffffff;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.title {
  margin-bottom: 0.5rem;
  font-size: 1.6rem;
  text-align: center;
}

.subtitle {
  font-size: 0.95rem;
  text-align: center;
  color: #666;
  margin-bottom: 1.5rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  font-size: 0.95rem;
}

.label {
  font-weight: 600;
  color: #333;
}

.value {
  color: #555;
}

.section-title {
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
  font-weight: bold;
  font-size: 1rem;
  border-bottom: 1px solid #ddd;
  padding-bottom: 0.25rem;
}

.item-list {
  margin-bottom: 1rem;
}

.item-row {
  display: flex;
  justify-content: space-between;
  margin: 0.3rem 0;
  font-size: 0.92rem;
}

.total-row {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  margin: 1.5rem 0 1rem;
  font-size: 1rem;
  border-top: 1px solid #ccc;
  padding-top: 0.8rem;
}

.back-btn {
  width: 100%;
  padding: 0.8rem;
  background-color: #00573f;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.back-btn:hover {
  background-color: #00432f;
}

.loading {
  text-align: center;
  padding: 3rem;
  color: #999;
  font-size: 1.1rem;
}
</style>
