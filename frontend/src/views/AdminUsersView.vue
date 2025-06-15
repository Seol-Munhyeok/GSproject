<template>
  <div class="admin-users-view">
    <h1>고객 관리</h1>

    <div class="search-box">
      <span>이름 검색: </span>
      <InputText v-model="nameFilter" placeholder="고객 이름을 입력하세요" />
    </div>

    <DataTable
      :value="customers"
      :filters="filters"
      :paginator="true"
      :rows="10"
      sortMode="multiple"
      responsiveLayout="scroll"
      stripedRows
      tableStyle="min-width: 600px"
    >
      <Column field="name" header="이름" sortable />
      <Column field="phone" header="전화번호" />
      <Column field="totalOrderCount" header="총 주문 수" sortable />
      <Column field="totalSpent" header="총 주문 금액" sortable>
        <template #body="slotProps">
          {{ formatCurrency(slotProps.data.totalSpent) }}
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import { FilterMatchMode } from 'primevue/api';

const customers = ref([]);
const nameFilter = ref('');

const filters = ref({
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const fetchCustomerData = async () => {
  try {
    const res = await axios.get('/api/admin/customers');
    customers.value = res.data;
  } catch (err) {
    console.error('고객 데이터 불러오기 실패', err);
  }
};

watch(nameFilter, (newVal) => {
  filters.value.name.value = newVal;
});

const formatCurrency = (amount) => {
  return Number(amount).toLocaleString('ko-KR') + '원';
};

onMounted(fetchCustomerData);
</script>

<style scoped>
.admin-users-view {
  padding: 2rem;
}

.search-box {
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
