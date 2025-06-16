<template>
  <div class="admin-add-product page-template">
    <h2 class="section-title">신규 상품 등록</h2>

    <form @submit.prevent="submitProduct" class="product-form">
      <div class="form-group">
        <label>상품명</label>
        <input
          v-model="name"
          type="text"
          placeholder="상품명을 입력하세요"
          required
        />
      </div>

      <div class="form-group">
        <label>가격 (원)</label>
        <input
          v-model.number="price"
          type="number"
          placeholder="가격을 입력하세요"
          required
        />
      </div>

      <div class="form-group">
        <label>프로모션</label>
        <select v-model="promotionType">
          <option value="">없음</option>
          <option value="1+1">1+1</option>
          <option value="카드결제할인">카드결제할인</option>
        </select>
      </div>

      <div class="form-group horizontal-checkbox">
        <input type="checkbox" v-model="availableToday" id="availableToday" />
        <label for="availableToday">오늘 사전예약 상품으로 등록</label>
      </div>

      <div class="form-group">
        <label>상세 설명</label>
        <textarea
          v-model="description"
          placeholder="상품에 대한 설명을 입력하세요"
        ></textarea>
      </div>

      <div class="form-group">
        <label>상품 이미지 업로드 (10MB 까지 업로드 가능)</label>
        <input type="file" @change="onFileChange" accept="image/*" required />
      </div>

      <div v-if="previewUrl" class="preview">
        <p>미리보기:</p>
        <img :src="previewUrl" alt="이미지 미리보기" />
      </div>

      <button type="submit" class="submit-btn">상품 등록</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const name = ref('');
const price = ref(0);
const promotionType = ref('');
const availableToday = ref(true);
const description = ref('');
const imageFile = ref(null);
const previewUrl = ref('');

const onFileChange = (e) => {
  imageFile.value = e.target.files[0];
  previewUrl.value = URL.createObjectURL(imageFile.value);
};

const submitProduct = async () => {
  if (!imageFile.value) return alert('이미지를 업로드해주세요.');

  const formData = new FormData();
  formData.append(
    'product',
    new Blob(
      [
        JSON.stringify({
          name: name.value,
          price: price.value,
          promotionType: promotionType.value || null,
          availableToday: availableToday.value,
          description: description.value,
        }),
      ],
      { type: 'application/json' },
    ),
  );
  formData.append('image', imageFile.value);

  try {
    const res = await axios.post('/api/products', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    alert('상품이 등록되었습니다.');
    console.log(res);
    // 초기화
    name.value = '';
    price.value = 0;
    promotionType.value = '';
    availableToday.value = true;
    description.value = '';
    imageFile.value = null;
    previewUrl.value = '';
  } catch (err) {
    console.error('상품 등록 실패:', err);
    alert('상품 등록 중 오류가 발생했습니다.');
  }
};
</script>

<style scoped>
.page-template {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
.section-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
  border-left: 4px solid #4caf50;
  padding-left: 0.75rem;
  margin-bottom: 2rem;
}
.product-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1.2rem;
}
.horizontal-checkbox {
  flex-direction: row;
  align-items: center;
  gap: 8px;
}

input[type='text'],
input[type='number'],
textarea,
select {
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}
textarea {
  min-height: 80px;
  resize: vertical;
}
.submit-btn {
  padding: 12px;
  font-size: 1rem;
  background-color: #00834e;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.submit-btn:hover {
  background-color: #006b3d;
}
.preview img {
  margin-top: 10px;
  width: 100px;
  border: 1px solid #ccc;
  border-radius: 6px;
}
label {
  font-weight: bold;
  margin-bottom: 7px;
}
</style>
