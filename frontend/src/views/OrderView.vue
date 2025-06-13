<template>
  <div class="order-container">
    <Header />
    <!-- 로고 -->
    <div class="logo">
      <img :src="require('@/assets/gs1.jpg')" alt="GS THE FRESH 로고" />
    </div>

    <h1>오늘의 사전예약 주문</h1>

    <!-- 주문 상품 목록 -->
    <div class="order-summary">
      <h2>주문 내역</h2>
      <div
        v-for="item in orderItems"
        :key="item.productId"
        class="order-item-card"
      >
        <img
          :src="getProductImage(item.name)"
          alt="상품 이미지"
          class="product-image"
        />
        <div class="item-info">
          <h3>{{ item.name }}</h3>
          <p>수량: {{ item.quantity }}</p>
          <!-- 프로모션 안내 -->
          <p v-if="item.promoNote" class="promo-note">{{ item.promoNote }}</p>
          <p>금액: {{ (item.price * item.quantity).toLocaleString() }}원</p>
        </div>
      </div>

      <div class="total">
        총 합계: <strong>{{ totalPrice.toLocaleString() }}원</strong>
      </div>
    </div>

    <!-- 고객 정보 입력 -->
    <div class="customer-info">
      <h2>고객 정보 입력</h2>
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

    <!-- 주문 버튼 -->
    <button class="order-button" @click="submitOrder">주문하기</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import Header from '@/components/Header.vue';

// 이미지 import
import beefSteakImg from '@/assets/beef_steak.png';
import blueberryImg from '@/assets/blueberry.png';
import cornImg from '@/assets/corn.png';
import duckSliceImg from '@/assets/duck_slice.png';
import gimImg from '@/assets/gim.png';
import hamiguaMelonImg from '@/assets/hamigua_melon.png';
import kiwiImg from '@/assets/kiwi.png';
import muskmelonImg from '@/assets/muskmelon.png';
import porkRibsImg from '@/assets/pork_ribs.png';
import salmonImg from '@/assets/salmon.png';
import sweetpotatoImg from '@/assets/sweetpotato.png';
import watermelonImg from '@/assets/watermelon.png';
import potatoImg from '@/assets/potato.png';

const route = useRoute();
const router = useRouter();

const customerName = ref('');
const customerPhone = ref('');
const orderItems = ref([]);

// 총 합계 계산
const totalPrice = computed(() =>
  orderItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0),
);

// 쿼리에서 orders 파싱
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
        promoNote: order.promoNote || null,
      };
    });
  } catch (e) {
    console.error('주문 화면 오류:', e);
  }
});

// 상품명 → 이미지 매핑
const getProductImage = (productName) => {
  const map = {
    '돼지양념칼집구이(800g/팩)': porkRibsImg,
    '고흥 햇감자(1.5kg/박스)': potatoImg,
    '광천 곱창김(5g*12봉)/2개/1세트': gimImg,
    '5無 훈제오리 슬라이스(250g*2팩)': duckSliceImg,
    '노르웨이 생연어 필렛(200g/팩)': salmonImg,
    '나이스 부채살 스테이크(200g)': beefSteakImg,
    '고당도 하미과 메론(대/1통)': hamiguaMelonImg,
    '국산 생 블루베리(100g)/2개/1세트': blueberryImg,
    '수박(7~8kg/미만)': watermelonImg,
    '햇 초당옥수수(3입/망)': cornImg,
    '진짜 맛있는 고구마(1.2kg/봉)': sweetpotatoImg,
    '제스프리 골드키위(9개/1세트)': kiwiImg,
    '머스크 메론(대/1통)': muskmelonImg,
  };

  const cleanName = productName.trim();
  return map[cleanName] || '';
};

// 전화번호 하이픈 자동 적용
const formatPhoneNumber = (event) => {
  let input = event.target.value.replace(/[^0-9]/g, ''); // 숫자만 남김

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

  const orderPayload = {
    name: customerName.value,
    phone: customerPhone.value,
    orders: orderItems.value.map((item) => ({
      productId: item.productId,
      quantity: item.quantity,
    })),
  };

  try {
    await axios.post('/api/orders', orderPayload);
    router.push('/complete');
  } catch (e) {
    console.error('주문 실패:', e);
    alert('주문에 실패했습니다.');
  }
};
</script>

<style scoped>
.order-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
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
</style>
