<template>
  <div class="order-container">
    <Header />

    <h1>오늘의 사전예약 주문</h1>

    <div class="order-summary">
      <h2>주문 내역</h2>
      <div
        v-for="item in orderItems"
        :key="item.productId"
        class="order-item-card"
      >
        <img
          :src="`http://localhost:8080/uploads/${item.imageUrl}`"
          alt="상품 이미지"
          class="product-image"
        />
        <div class="item-info">
          <h3>{{ item.name }}</h3>
          <p>수량: {{ item.quantity }}</p>
          <p v-if="item.promoNote" class="promo-note">{{ item.promoNote }}</p>
          <p>금액: {{ (item.price * item.quantity).toLocaleString() }}원</p>
        </div>
      </div>

      <div class="total">
        총 합계: <strong>{{ totalPrice.toLocaleString() }}원</strong>
      </div>
    </div>

    <div class="customer-info">
      <h2>고객 정보 입력</h2>

      <div class="remember-checkbox">
        <label for="remember">이름과 전화번호 저장하기</label>
        <input type="checkbox" v-model="rememberInfo" id="remember" />
      </div>

      <label>
        이름:
        <input
          v-model="customerName"
          type="text"
          placeholder="이름을 입력하세요"
        />
      </label>
      <label>
        전화번호:
        <input
          v-model="customerPhone"
          type="tel"
          placeholder="전화번호를 입력하세요"
          @input="formatPhoneNumber"
        />
      </label>
    </div>

    <button class="order-button" @click="submitOrder">주문하기</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import Header from '@/components/Header.vue';

const route = useRoute();
const router = useRouter();

const customerName = ref('');
const customerPhone = ref('');
const orderItems = ref([]);
const rememberInfo = ref(false);

const totalPrice = computed(() =>
  orderItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0),
);

onMounted(async () => {
  try {
    const orders = JSON.parse(route.query.orders || '[]');
    if (orders.length === 0) {
      alert('주문 상품이 없습니다.');
      router.push('/products');
      return;
    }

    const res = await axios.get('/api/products/today');
    const products = res.data;

    orderItems.value = orders.map((order) => {
      const product = products.find((p) => p.id === order.productId);
      return {
        productId: order.productId,
        name: product?.name || 'Unknown',
        price: product?.price || 0,
        quantity: order.quantity,
        promoNote: product?.promotionType || null,
        imageUrl: product?.imageUrl || '',
      };
    });
  } catch (e) {
    console.error('주문 화면 오류:', e);
  }

  const savedName = localStorage.getItem('savedCustomerName');
  const savedPhone = localStorage.getItem('savedCustomerPhone');
  if (savedName && savedPhone) {
    customerName.value = savedName;
    customerPhone.value = savedPhone;
    rememberInfo.value = true;
  }
});

const formatPhoneNumber = (event) => {
  let input = event.target.value.replace(/[^0-9]/g, '');
  if (input.length < 4) {
    customerPhone.value = input;
  } else if (input.length < 7) {
    customerPhone.value = `${input.slice(0, 3)}-${input.slice(3)}`;
  } else if (input.length < 11) {
    customerPhone.value = `${input.slice(0, 3)}-${input.slice(
      3,
      6,
    )}-${input.slice(6)}`;
  } else {
    customerPhone.value = `${input.slice(0, 3)}-${input.slice(
      3,
      7,
    )}-${input.slice(7, 11)}`;
  }
};

const submitOrder = async () => {
  if (!customerName.value || !customerPhone.value) {
    alert('이름과 전화번호를 입력해주세요.');
    return;
  }

  if (rememberInfo.value) {
    localStorage.setItem('savedCustomerName', customerName.value);
    localStorage.setItem('savedCustomerPhone', customerPhone.value);
  } else {
    localStorage.removeItem('savedCustomerName');
    localStorage.removeItem('savedCustomerPhone');
  }

  const orderPayload = {
    name: customerName.value,
    phone: customerPhone.value,
    orders: orderItems.value.map((item) => ({
      productId: item.productId,
      quantity: item.quantity,
    })),
  };

  try {
    const res = await axios.post('/api/orders', orderPayload);
    const orderId = res.data.orderId;
    router.push(`/complete/${orderId}`);
  } catch (e) {
    console.error('주문 실패:', e);
    alert('주문에 실패했습니다.');
  }
};
</script>

<style scoped>
/* 기존 스타일 유지 */
.order-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
  min-width: 47vw;
}

.logo img {
  width: 200px;
  display: block;
  margin: 0 auto 20px;
}

.order-summary {
  margin-bottom: 20px;
}

.order-item-card {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 8px;
  background-color: #fafafa;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  margin-right: 15px;
}

.item-info h3 {
  margin: 0 0 5px 0;
  font-size: 16px;
}

.item-info p {
  margin: 2px 0;
}

.promo-note {
  font-size: 14px;
  color: #00a651;
  font-weight: bold;
  margin: 4px 0;
}

.total {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  margin-top: 10px;
}

.customer-info {
  margin-bottom: 20px;
}

.customer-info label {
  display: block;
  margin-bottom: 12px;
  font-weight: bold;
}

.customer-info input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  margin-top: 4px;
}

.order-button {
  width: 100%;
  padding: 12px;
  background-color: #0078d4;
  color: white;
  border: none;
  font-size: 16px;
  cursor: pointer;
  border-radius: 8px;
}

.order-button:hover {
  background-color: #005fa3;
}

.remember-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.remember-checkbox input[type='checkbox'] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}
</style>
