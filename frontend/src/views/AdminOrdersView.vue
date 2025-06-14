<template>
  <div>
    <h2>주문 현황</h2>

    <!-- 표 영역 -->
    <div class="orders-table-section">
      <table class="orders-table">
        <thead>
          <tr>
            <th>고객명</th>
            <th v-for="product in productNames" :key="product">
              {{ truncate(product) }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="customer in customerNames" :key="customer">
            <td>{{ customer }}</td>
            <td
              v-for="product in productNames"
              :key="product"
              style="text-align: center"
            >
              {{
                ordersMap[customer] && ordersMap[customer][product]
                  ? ordersMap[customer][product]
                  : ''
              }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const ordersData = ref([]);
const productNames = ref([]);
const customerNames = ref([]);
const ordersMap = ref({});

onMounted(async () => {
  const res = await axios.get('/api/admin/orders');
  ordersData.value = res.data;

  // 상품명 정리
  productNames.value = Array.from(
    new Set(ordersData.value.map((o) => o.productName)),
  ).sort();

  // 고객명 정리
  customerNames.value = Array.from(
    new Set(ordersData.value.map((o) => o.customerName)),
  ).sort((a, b) => a.localeCompare(b, 'ko'));

  // Map 구조로 데이터 가공
  const map = {};
  ordersData.value.forEach((o) => {
    if (!map[o.customerName]) {
      map[o.customerName] = {};
    }
    map[o.customerName][o.productName] = o.quantity;
  });
  ordersMap.value = map;
});

// 상품명 너무 길 때 자르기
const truncate = (text, maxLength = 10) => {
  return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
};
</script>

<style scoped>
.orders-table-section {
  overflow-x: auto;
  overflow-y: hidden;
  max-height: 60vh;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  background-color: white;
  padding: 10px;
  margin-bottom: 30px;
  max-width: 100%;
  box-sizing: border-box;
}

.orders-table {
  width: auto;
  min-width: 1200px;
  border-collapse: collapse;
}

.orders-table th,
.orders-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
  font-size: 14px;
}

.orders-table th {
  background-color: rgb(240, 250, 245); /* 헤더 배경 강조 */
  color: rgb(0, 87, 63);
  font-weight: bold;
}

.orders-table td:first-child {
  background-color: #f9f9f9; /* 고객명 열 배경 */
  font-weight: bold;
  color: #333;
}

/* 행 hover 효과 */
.orders-table tbody tr:hover {
  background-color: #f5f5f5;
}
</style>
