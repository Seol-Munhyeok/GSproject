<template>
  <div class="card">
    <h3>{{ product.name }}</h3>
    <p>{{ product.price }}원</p>
    <p v-if="product.promotionType">[{{ product.promotionType }}]</p>
    <label>
      수량:
      <input type="number" min="0" v-model.number="quantity" />
    </label>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { ref, watch } from 'vue'

const props = defineProps(['product'])
const emits = defineEmits(['update'])

const quantity = ref(0)

watch(quantity, (newVal) => {
  emits('update', { productId: props.product.id, quantity: newVal })
})
</script>
