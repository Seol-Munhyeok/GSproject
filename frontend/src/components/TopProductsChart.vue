<template>
  <div class="chart-container">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { Bar } from 'vue-chartjs';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
);

// props로 topProducts 배열 받기
const props = defineProps({
  products: {
    type: Array,
    required: true,
  },
});

// 차트에 쓸 데이터 계산
const chartData = computed(() => ({
  labels: props.products.map((p) => p.productName),
  datasets: [
    {
      label: '총 판매 수량',
      data: props.products.map((p) => p.totalQuantity),
      backgroundColor: 'rgba(255, 159, 64, 0.6)',
      borderColor: 'rgba(255, 159, 64, 1)',
      borderWidth: 1,
    },
  ],
}));

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false },
    title: {
      display: true,
      text: '기간 내 인기 상품 (판매 수량 기준)',
    },
    tooltip: {
      callbacks: {
        label: (ctx) => `${ctx.raw}개`,
      },
    },
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        stepSize: 1,
        callback: (val) => `${val}개`,
      },
    },
  },
};
</script>

<style scoped>
.chart-container {
  margin-top: 2rem;
}
</style>
