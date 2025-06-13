<template>
  <div class="main-container">
    <!-- 공통 헤더 적용 -->
    <Header />

    <!-- 오늘 날짜 표시 -->
    <div class="date-title">{{ today }} 오늘의 사전예약 상품</div>

    <!-- 상품 리스트 -->
    <div class="product-list">
      <div
        v-for="product in products"
        :key="product.id"
        class="product-card"
        @click="openModal(product)"
      >
        <img
          :src="getProductImage(product.name)"
          alt="상품 이미지"
          class="product-image"
        />
        <h3>{{ product.name }}</h3>

        <!-- 가격 강조 -->
        <p class="product-price">{{ product.price.toLocaleString() }}원</p>

        <!-- 행사 강조 -->
        <p v-if="product.promotionType" class="promotion">
          [{{ product.promotionType }}]
        </p>

        <!-- 프로모션 안내 (항상 영역 확보) -->
        <div class="promo-section">
          <p v-if="product.promotionType === '카드결제할인'" class="card-info">
            삼성, 롯데, 농협, GS Pay 결제 시
          </p>
          <p
            v-else-if="
              product.promotionType === '1+1' && orderMap[product.id] > 0
            "
            class="promo-info"
          >
            행사 적용 시 총 제공 수량: {{ orderMap[product.id] * 2 }}개
          </p>
          <p v-else class="no-promo">오늘의 행사 상품</p>
        </div>

        <!-- 수량 선택 -->
        <div class="quantity-control" @click.stop>
          <button @click="decreaseQuantity(product.id)">-</button>
          <input type="number" min="0" v-model.number="orderMap[product.id]" />
          <button @click="increaseQuantity(product.id)">+</button>
        </div>
      </div>
    </div>

    <!-- 주문하기 버튼 -->
    <button class="order-btn" @click="submitOrder">주문하기</button>

    <!-- Floating Cart -->
    <div
      class="floating-cart"
      v-if="products.length > 0 && cartItems.length > 0"
    >
      <h4>🛒 장바구니</h4>
      <ul>
        <li v-for="item in cartItems" :key="item.id">
          {{ item.name }} x {{ item.quantity }}
        </li>
      </ul>
      <div class="cart-total">
        총합계: <strong>{{ cartTotal.toLocaleString() }}원</strong>
      </div>
      <button class="cart-order-btn" @click="goToOrder">주문하기</button>
    </div>

    <!-- Product Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <img
          :src="getFullImage(selectedProduct?.name)"
          alt="상세 이미지"
          class="modal-image"
        />
        <div class="modal-description">
          <h2>{{ selectedProduct?.name }}</h2>
          <p v-if="selectedProduct?.promotionType" class="modal-promotion">
            [{{ selectedProduct?.promotionType }}]
          </p>
          <p
            v-if="selectedProduct?.promotionType === '카드결제할인'"
            class="card-extra-info"
          >
            삼성, 롯데, 농협, GS Pay 결제 시
          </p>
          <p class="modal-price">
            {{ selectedProduct?.price?.toLocaleString() }}원
          </p>
          <p class="modal-description-text">
            {{
              productDescriptionMap[selectedProduct?.name] ||
              '※ 행사 상세 내용은 매장 사정에 따라 달라질 수 있습니다.'
            }}
          </p>
          <button class="close-btn" @click="closeModal">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import Header from '@/components/Header.vue';

// 이미지 import (원래 카드용)
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

// full 이미지 import (중요 ★)
import gimFullImg from '@/assets/gim_full.png';
import beefSteakFullImg from '@/assets/beef_steak_full.png';
import porkRibsFullImg from '@/assets/pork_ribs_full.png';
import potatoFullImg from '@/assets/potato_full.png';
import duckSliceFullImg from '@/assets/duck_slice_full.png';
import salmonFullImg from '@/assets/salmon_full.png';
import hamiguaMelonFullImg from '@/assets/hamigua_melon_full.png';
import blueberryFullImg from '@/assets/blueberry_full.png';
import watermelonFullImg from '@/assets/watermelon_full.png';
import cornFullImg from '@/assets/corn_full.png';
import sweetpotatoFullImg from '@/assets/sweetpotato_full.png';
import kiwiFullImg from '@/assets/kiwi_full.png';
import muskmelonFullImg from '@/assets/muskmelon_full.png';

const router = useRouter();
const products = ref([]);
const orderMap = ref({});

const productDescriptionMap = {
  '광천 곱창김(5g*12봉)/2개/1세트':
    '광천 김의 깊은 풍미와 바삭함을 느껴보세요. 1+1 행사로 더욱 풍성하게 드립니다.',
  '나이스 부채살 스테이크(200g)':
    '부드러운 육질과 고소한 풍미의 부채살 스테이크! 에어프라이어, 팬 구이로 최적.',
  '고흥 햇감자(1.5kg/박스)':
    '고흥에서 갓 수확한 신선한 햇감자! 찌거나 구워서 맛있게 즐기세요.',
  '돼지양념칼집구이(800g/팩)':
    '달콤짭짤한 양념이 깊숙이 배어있는 돼지 양념 칼집구이! 가족 모임, 캠핑에 강력 추천.',
  '5無 훈제오리 슬라이스(250g*2팩)':
    '합성첨가물 5無! 건강하게 즐기는 훈제 오리 슬라이스. 샐러드, 덮밥, 술안주 모두 OK.',
  '노르웨이 생연어 필렛(200g/팩)':
    '신선하게 공수한 노르웨이 생연어 필렛. 부드럽고 고소한 맛이 일품!',
  '고당도 하미과 메론(대/1통)':
    '당도 높은 하미과 메론으로 여름을 시원하게! 껍질 얇고 과육이 풍부한 고급 메론.',
  '국산 생 블루베리(100g)/2개/1세트':
    '달콤하고 촉촉한 국산 블루베리! 간식, 요거트 토핑, 샐러드에 활용해보세요.',
  '수박(7~8kg/미만)':
    '무더운 여름의 필수템, 시원한 수박! 아삭아삭 시원한 맛으로 온 가족이 즐겨요.',
  '햇 초당옥수수(3입/망)':
    '생으로도 먹을 수 있을 만큼 달콤한 초당옥수수! 쪄서 드시면 더욱 맛있어요.',
  '진짜 맛있는 고구마(1.2kg/봉)':
    '밤고구마 못지 않은 달콤함! 껍질째 구워먹는 진짜 맛있는 고구마.',
  '제스프리 골드키위(9개/1세트)':
    '골드키위 특유의 달콤함과 풍부한 비타민C! 아침 한 개로 활기찬 하루를 시작하세요.',
  '머스크 메론(대/1통)':
    '머스크향 가득한 고급 메론! 특별한 날 디저트로 강력 추천.',
};

const today = computed(() => {
  const date = new Date();
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
});

onMounted(async () => {
  const res = await axios.get('/api/products/today');
  products.value = res.data;

  products.value.forEach((product) => {
    orderMap.value[product.id] = 0;
  });
});

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

// full 이미지 map
const fullImageMap = {
  '광천 곱창김(5g*12봉)/2개/1세트': gimFullImg,
  '돼지양념칼집구이(800g/팩)': porkRibsFullImg,
  '고흥 햇감자(1.5kg/박스)': potatoFullImg,
  '5無 훈제오리 슬라이스(250g*2팩)': duckSliceFullImg,
  '노르웨이 생연어 필렛(200g/팩)': salmonFullImg,
  '나이스 부채살 스테이크(200g)': beefSteakFullImg,
  '고당도 하미과 메론(대/1통)': hamiguaMelonFullImg,
  '국산 생 블루베리(100g)/2개/1세트': blueberryFullImg,
  '수박(7~8kg/미만)': watermelonFullImg,
  '햇 초당옥수수(3입/망)': cornFullImg,
  '진짜 맛있는 고구마(1.2kg/봉)': sweetpotatoFullImg,
  '제스프리 골드키위(9개/1세트)': kiwiFullImg,
  '머스크 메론(대/1통)': muskmelonFullImg,
};

const getFullImage = (productName) => {
  return fullImageMap[productName.trim()] || '';
};

const increaseQuantity = (productId) => {
  orderMap.value[productId] += 1;
};

const decreaseQuantity = (productId) => {
  if (orderMap.value[productId] > 0) {
    orderMap.value[productId] -= 1;
  }
};

const submitOrder = () => {
  const orders = Object.entries(orderMap.value)
    .filter(([, qty]) => qty > 0)
    .map(([productId, quantity]) => {
      const product = products.value.find((p) => p.id === parseInt(productId));
      let totalQuantity = quantity;
      let promoNote = null;

      if (product.promotionType === '1+1') {
        totalQuantity = quantity * 2;
        promoNote = `1+1 행사 적용 → 총 제공 수량: ${totalQuantity}개 (유료 ${quantity}개)`;
      } else if (product.promotionType === '카드결제할인') {
        promoNote = `삼성, 롯데, 농협, GS Pay 결제 시 적용가`;
      }

      return {
        productId: parseInt(productId),
        quantity,
        totalQuantity,
        promoNote,
      };
    });

  if (orders.length === 0) {
    alert('주문할 상품을 선택해주세요.');
    return;
  }

  router.push({
    path: '/order',
    query: {
      orders: JSON.stringify(orders),
    },
  });
};

// Floating Cart
const cartItems = computed(() => {
  return products.value
    .filter((product) => orderMap.value[product.id] > 0)
    .map((product) => ({
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: orderMap.value[product.id],
    }));
});

const cartTotal = computed(() => {
  return cartItems.value.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0,
  );
});

const goToOrder = () => {
  const orders = cartItems.value.map((item) => {
    const product = products.value.find((p) => p.id === item.id);
    let totalQuantity = item.quantity;
    let promoNote = null;

    if (product.promotionType === '1+1') {
      totalQuantity = item.quantity * 2;
      promoNote = `1+1 행사 적용 → 총 제공 수량: ${totalQuantity}개 (유료 ${item.quantity}개)`;
    } else if (product.promotionType === '카드결제할인') {
      promoNote = `삼성, 롯데, 농협, GS Pay 결제 시 적용가`;
    }

    return {
      productId: item.id,
      quantity: item.quantity,
      totalQuantity,
      promoNote,
    };
  });

  if (orders.length === 0) {
    alert('주문할 상품을 선택해주세요.');
    return;
  }

  router.push({
    path: '/order',
    query: {
      orders: JSON.stringify(orders),
    },
  });
};

// Modal 상태
const isModalOpen = ref(false);
const selectedProduct = ref(null);

const openModal = (product) => {
  selectedProduct.value = product;
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedProduct.value = null;
};
</script>

<style scoped>
.main-container {
  font-family: 'Noto Sans KR', sans-serif;
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.admin-btn:hover {
  background-color: #333;
}

.date-title {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 10px;
  text-align: center;
  background-color: #fafafa;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  position: relative;
  padding-bottom: 120px;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.product-price {
  font-size: 24px;
  font-weight: bold;
  color: #d32f2f;
  margin: 8px 0;
}

.promotion {
  font-size: 20px;
  font-weight: bold;
  color: #1976d2;
  background-color: #e3f2fd;
  display: inline-block;
  padding: 4px 8px;
  border-radius: 6px;
}

.promo-section {
  min-height: 24px;
  margin-top: 8px;
  margin-bottom: 8px;
}

.promo-info,
.card-info {
  font-size: 14px;
  font-weight: bold;
}

.promo-info {
  color: #00a651;
  font-size: 15px;
}

.no-promo {
  min-height: 20px;
  font-size: 15px;
  color: #555;
  opacity: 0.7;
  font-weight: bold;
}

.card-info {
  color: #1976d2;
  font-size: 15px;
}

.quantity-control {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 8px;
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
}

.quantity-control button {
  width: 44px;
  height: 44px;
  font-size: 22px;
  font-weight: bold;
  border: 1px solid #00a651;
  background-color: #eafff0;
  color: #00a651;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.quantity-control button:hover {
  background-color: #00a651;
  color: white;
}

.quantity-control input {
  width: 60px;
  height: 44px;
  font-size: 22px;
  font-weight: bold;
  text-align: center;
  margin: 0 12px;
  border: 2px solid #00a651;
  border-radius: 8px;
  background-color: #f9f9f9;
  color: #333;
}

.quantity-control input::-webkit-outer-spin-button,
.quantity-control input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.quantity-control input {
  -moz-appearance: textfield;
}

.order-btn {
  width: 100%;
  padding: 14px;
  background-color: #00a651;
  color: white;
  border: none;
  font-size: 16px;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.order-btn:hover {
  background-color: #008a42;
}

.floating-cart {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 240px;
  background-color: white;
  border: 2px solid #00a651;
  border-radius: 8px;
  padding: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  font-size: 14px;
  z-index: 9999;
}

.floating-cart h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #00a651;
}

.floating-cart ul {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 120px;
  overflow-y: auto;
}

.floating-cart li {
  margin-bottom: 4px;
  font-weight: bold;
}

.cart-total {
  text-align: right;
  font-size: 15px;
  margin-top: 8px;
  margin-bottom: 8px;
}

.cart-order-btn {
  width: 100%;
  padding: 8px;
  background-color: #00a651;
  color: white;
  border: none;
  font-size: 14px;
  cursor: pointer;
  border-radius: 6px;
}

.cart-order-btn:hover {
  background-color: #008a42;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  display: flex;
  max-width: 800px;
  width: 90%;
  padding: 20px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  position: relative;
}

.modal-image {
  width: 50%;
  object-fit: contain;
  border-radius: 8px;
  margin-right: 20px;
}

.modal-description {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.modal-description h2 {
  margin-bottom: 10px;
}

.close-btn {
  margin-top: 20px;
  padding: 10px;
  background-color: #00a651;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.close-btn:hover {
  background-color: #008a42;
}

.modal-promotion {
  font-size: 20px;
  font-weight: bold;
  color: #1976d2;
  background-color: #e3f2fd;
  display: block;
  text-align: center;
  padding: 4px 10px;
  border-radius: 6px;
  margin-bottom: 12px;
}

.modal-price {
  font-size: 28px;
  font-weight: bold;
  color: #d32f2f;
  margin-bottom: 12px;
}

.modal-description-text {
  font-size: 16px;
  line-height: 1.5;
  color: #333;
}

.card-extra-info {
  font-size: 15px;
  font-weight: bold;
  color: #1976d2;
  margin-top: 8px;
  text-align: center;
}
</style>
