<template>
  <div>
    <!-- 날짜 선택 영역 -->
    <div class="section">
      <h2 class="section-title">조회 기간을 지정하세요</h2>
      <div class="date-picker">
        <span>조회 기간:</span>
        <input type="date" v-model="from" />
        <span>~</span>
        <input type="date" v-model="to" />
        <button @click="fetchStatistics">조회</button>
      </div>
    </div>

    <!-- 요약 카드 -->
    <div v-if="summary" class="section">
      <h2 class="section-title">요약 정보</h2>
      <div class="summary-cards">
        <SummaryCard title="총 주문 수" :value="summary.totalOrderCount" />
        <SummaryCard
          title="총 매출"
          :value="formatCurrency(summary.totalSales)"
        />
        <SummaryCard
          title="총 제공 예정 수량"
          :value="summary.totalProvidedQuantity"
        />
      </div>
    </div>

    <!-- 일별 매출 차트 -->
    <div v-if="dailyStats.length > 0" class="section">
      <h2 class="section-title">일별 매출 추이</h2>
      <ChartSection :stats="dailyStats" />
    </div>

    <!-- Top 상품 차트 -->
    <div v-if="topProducts.length > 0" class="section">
      <h2 class="section-title">인기 상품</h2>
      <TopProductsChart :products="topProducts" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import SummaryCard from '@/components/SummaryCard.vue';
import ChartSection from '@/components/ChartSection.vue';
import TopProductsChart from '@/components/TopProductsChart.vue';

import axios from 'axios';

// 오늘 날짜 기본값
const today = new Date().toISOString().split('T')[0];
const from = ref(today);
const to = ref(today);

const summary = ref(null);
const dailyStats = ref([]);
const topProducts = ref([]);

function formatCurrency(value) {
  return `₩${value.toLocaleString()}`;
}

async function fetchStatistics() {
  if (!from.value || !to.value) return;

  try {
    const res = await axios.get('/api/admin/statistics', {
      params: { from: from.value, to: to.value },
    });
    summary.value = res.data.summary;
    dailyStats.value = res.data.dailyStats;
    topProducts.value = res.data.topProductsByQuantity;
  } catch (err) {
    console.error('통계 요청 실패:', err);
  }
}

onMounted(fetchStatistics);
</script>

<style scoped>
.date-picker {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.summary-cards {
  display: flex;
  gap: 1.5rem;
  justify-content: space-between;
}

.page-template {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section {
  margin-bottom: 2.5rem;
}

.section-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #333;
  border-left: 4px solid #4caf50;
  padding-left: 0.75rem;
}
</style>
