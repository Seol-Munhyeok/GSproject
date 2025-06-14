<template>
  <div class="chart-container">
    <Line :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
);

// props로 dailyStats 받아오기
const props = defineProps({
  stats: {
    type: Array,
    required: true,
  },
});

// 📈 날짜별 매출만 보여주는 reactive chartData
const chartData = computed(() => ({
  labels: props.stats.map((s) => s.date),
  datasets: [
    {
      label: '매출액',
      data: props.stats.map((s) => s.sales),
      borderColor: 'rgba(75, 192, 192, 1)',
      backgroundColor: 'rgba(75, 192, 192, 0.1)',
      tension: 0.3,
      fill: false,
    },
  ],
}));

const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      display: true,
      position: 'top',
    },
    title: {
      display: true,
      text: '일별 매출 추이',
    },
    tooltip: {
      callbacks: {
        label: (ctx) => `₩${ctx.raw.toLocaleString()}`,
      },
    },
  },
  scales: {
    y: {
      ticks: {
        callback: (val) => `₩${val.toLocaleString()}`,
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
