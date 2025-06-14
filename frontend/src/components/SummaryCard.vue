<!-- components/SummaryCard.vue -->
<template>
  <div class="summary-card">
    <div class="summary-title">{{ title }}</div>
    <div class="summary-value">{{ value }}</div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: [String, Number],
});
</script>

<style scoped>
.summary-card {
  background-color: #f9f9f9;
  padding: 1.2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  width: 100%;
  text-align: center;
}

.summary-title {
  font-size: 1rem;
  color: #666;
  margin-bottom: 0.5rem;
}

.summary-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}
</style>
