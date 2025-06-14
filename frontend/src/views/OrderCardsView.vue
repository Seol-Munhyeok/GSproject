<template>
  <div>
    <h2>주문 상세 카드 보기</h2>
    <div class="cards-grid">
      <div class="order-card" v-for="customer in customerNames" :key="customer">
        <h4>{{ customer }}</h4>
        <ul>
          <li v-for="item in customerItems(customer)" :key="item.productName">
            {{ item.productName }}: {{ item.quantity }}개
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const ordersData = ref([]);
const customerNames = ref([]);

onMounted(async () => {
  const res = await axios.get('/api/admin/orders');
  ordersData.value = res.data;

  customerNames.value = Array.from(
    new Set(ordersData.value.map((o) => o.customerName)),
  ).sort((a, b) => a.localeCompare(b, 'ko'));
});

const customerItems = (customer) => {
  return ordersData.value.filter((o) => o.customerName === customer);
};
</script>

<style scoped>
.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  width: 100%;
  box-sizing: border-box;
}

.order-card {
  border: 1px solid var(--gs-border-color, #ccc);
  border-radius: 8px;
  padding: 16px;
  background-color: #fafafa;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.order-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.order-card h4 {
  margin-bottom: 12px;
  color: rgb(0, 87, 63);
  font-size: 18px;
}

.order-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.order-card li {
  margin-bottom: 6px;
  font-size: 14px;
  color: var(--gs-text-color, #333);
}
</style>
