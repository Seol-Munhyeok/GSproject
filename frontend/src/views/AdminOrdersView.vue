<template>
  <div class="admin-orders-view">
    <h2>주문 현황</h2>

    <!-- 표 스크롤을 이 div 안에서만 발생하도록 제한 -->
    <div class="primevue-table-wrapper">
      <DataTable
        :value="tableRows"
        scrollable
        scrollHeight="70vh"
        style="width: 79vw"
        tableStyle="min-width: 1500px"
        :paginator="false"
        stripedRows
        :rowClass="getRowClass"
      >
        <!-- 고정 열 -->
        <Column
          field="customerName"
          header="고객명"
          frozen
          alignFrozen="left"
          style="min-width: 150px"
        />

        <!-- 동적 상품 열 -->
        <Column
          v-for="product in productNames"
          :key="product"
          :field="product"
          :header="truncate(product)"
          style="min-width: 120px"
        />
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const ordersData = ref([]);
const productNames = ref([]);
const customerNames = ref([]);
const tableRows = ref([]);

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

  // DataTable에 맞게 행 단위로 변환
  tableRows.value = customerNames.value.map((customer) => {
    const row = { customerName: customer };
    productNames.value.forEach((product) => {
      const total = ordersData.value
        .filter((o) => o.customerName === customer && o.productName === product)
        .reduce((sum, o) => sum + o.quantity, 0);
      row[product] = total === 0 ? '' : total;
    });
    return row;
  });

  const sumRow = { customerName: '합계' };
  productNames.value.forEach((product) => {
    sumRow[product] = tableRows.value.reduce((sum, row) => {
      const qty = Number(row[product]);
      return sum + (isNaN(qty) ? 0 : qty);
    }, 0);
  });
  tableRows.value.push(sumRow);
});
const getRowClass = (data) => {
  return data.customerName === '합계' ? 'summary-row' : '';
};
const truncate = (text, maxLength = 10) => {
  return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
};
</script>

<style scoped>
.admin-orders-view {
  padding: 2rem;
}

.p-datatable .p-column-title {
  white-space: nowrap;
}
.p-datatable-tbody > tr > td {
  text-align: center;
}

.primevue-table-wrapper {
  max-width: 100%;
  overflow-x: auto;
}

:deep(.summary-row) {
  background-color: #e6f4ea;
  font-weight: bold;
  color: rgb(0, 87, 63);
}
</style>
