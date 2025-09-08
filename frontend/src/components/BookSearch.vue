<template>
  <dialog v-if="visible" class="book-search" open>
    <h2>🔍 Tìm kiếm Sách</h2>

    <form @submit.prevent="handleSearch" class="form">
      <div class="form-group">
        <label for="title">Tiêu đề</label>
        <input v-model="filter.title" id="title" placeholder="Nhập tiêu đề..." />
      </div>

      <div class="form-group">
        <label for="publishedYear">Năm XB</label>
        <input v-model.number="filter.publishedYear" id="publishedYear" type="number" />
      </div>

      <div class="form-group">
        <label for="minPrice">Giá tối thiểu</label>
        <input v-model.number="filter.minPrice" id="minPrice" type="number" />
      </div>

      <div class="form-group">
        <label for="maxPrice">Giá tối đa</label>
        <input v-model.number="filter.maxPrice" id="maxPrice" type="number" />
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? "⏳ Đang tìm..." : "Tìm kiếm" }}
      </button>
      <button type="button" @click="close">❌ Đóng</button>
    </form>

    <p v-if="error" class="error">❌ {{ error.message }}</p>

    <div class="results" v-if="data?.searchBooks?.length">
      <h3>📖 Kết quả:</h3>
      <table>
        <thead>
        <tr>
          <th>Tiêu đề</th>
          <th>Năm XB</th>
          <th>Giá</th>
          <th>Số lượng</th>
          <th>Chọn</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="book in data.searchBooks" :key="book.id" class="result-item" @click="selectBook(book)">
          <td>{{ book.title }}</td>
          <td>{{ book.publishedYear || '-' }}</td>
          <td>{{ book.price?.toLocaleString('vi-VN') || '-' }}</td>
          <td>{{ book.quantity ?? '-' }}</td>
          <td>
            <button @click.stop="selectBook(book)">✅</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <p v-else-if="!loading && searched">⚠️ Không có sách nào khớp.</p>
  </dialog>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch } from "vue";
import { useLazyQuery } from "@vue/apollo-composable";
import { SEARCH_BOOKS } from "../services/queries.ts";

// ---- Types ----
interface Book {
  id: string;
  title: string;
  publishedYear?: number | null;
  price?: number | null;
  quantity?: number | null;
  authorId: string;
}

interface BookSearchInput {
  title?: string | null;
  publishedYear?: number | null;
  minPrice?: number | null;
  maxPrice?: number | null;
}

// ---- Props & Emits ----
const props = defineProps({
  visible: Boolean,
});

const emit = defineEmits<{
  (e: "close"): void;
  (e: "searched", books: Book[]): void;
  (e: "selected", book: Book): void;
}>();

// ---- State ----
const filter = reactive<BookSearchInput>({
  title: "",
  publishedYear: null,
  minPrice: null,
  maxPrice: null,
});
const searched = ref(false);

// ---- Tạo biến động bằng computed property ----
const variables = computed(() => ({
  filter: {
    title: filter.title || undefined,
    publishedYear: filter.publishedYear ?? undefined,
    minPrice: filter.minPrice ?? null,
    maxPrice: filter.maxPrice ?? null,
  },
}));

// ---- Truy vấn với computed property ----
const { load, loading, result: data, error } = useLazyQuery<
    { searchBooks: Book[] },
    { filter: BookSearchInput }
>(SEARCH_BOOKS, variables);

// ---- Watcher để làm sạch form khi popup đóng ----
watch(
    () => props.visible,
    (newVal) => {
      if (newVal) {
        searched.value = false;
      } else {
        resetFilter();
      }
    },
);

// ---- Methods ----
async function handleSearch() {
  searched.value = true;
  await load();
  if (!loading.value && !error.value) {
    emit("searched", data.value?.searchBooks || []);
  }
}

function selectBook(book: Book) {
  emit("selected", book);
  close();
}

function resetFilter() {
  Object.assign(filter, {
    title: "",
    publishedYear: null,
    minPrice: null,
    maxPrice: null,
  });
  searched.value = false;
}

function close() {
  emit("close");
}
</script>
<style scoped>
.book-search {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;

  padding: 2.5rem;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 700px;
  max-height: 80vh;
  overflow-y: auto;
  scrollbar-width: none;
  border: 1px solid #e0e0e0;
  animation: fadeIn 0.3s ease-out;
}

.book-search::backdrop {
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
}

.book-search h2 {
  font-size: 1.8rem;
  color: #174a9e;
  text-align: center;
  margin-bottom: 2rem;
  position: relative;
}

.book-search h2::after {
  content: '';
  display: block;
  width: 50px;
  height: 3px;
  background-color: #174a9e;
  margin: 10px auto 0;
}

.form {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: center;
  justify-content: space-between;
}

.form-group {
  flex: 1;
  min-width: 150px;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #444;
}

.form-group input {
  padding: 0.75rem 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #174a9e;
  box-shadow: 0 0 0 3px rgba(23, 74, 158, 0.2);
}

.form button {
  padding: 0.8rem 1.5rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 1rem;
}

.form button[type="submit"] {
  background-color: #174a9e;
  color: white;
}

.form button[type="submit"]:hover:not(:disabled) {
  background-color: #0d47a1;
}

.form button[type="submit"]:disabled {
  background-color: #b0c4de;
  cursor: not-allowed;
}

.form button[type="button"] {
  background-color: #f1f1f1;
  color: #555;
  border: 1px solid #ddd;
}

.form button[type="button"]:hover {
  background-color: #e0e0e0;
}

.results {
  margin-top: 2rem;
  max-height: 250px;
  overflow-y: auto;
  padding-right: 8px;
}

.results::-webkit-scrollbar {
  width: 8px;
}

.results::-webkit-scrollbar-thumb {
  background-color: #174a9e;
  border-radius: 6px;
}

.results::-webkit-scrollbar-thumb:hover {
  background-color: #0d47a1;
}

.results h3 {
  font-size: 1.2rem;
  color: #174a9e;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
}

.results table {
  width: 100%;
  border-collapse: collapse;
}

.results th,
.results td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.results th {
  background-color: #f5f5f5;
  color: #444;
  font-weight: 700;
}

.result-item {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.result-item:hover {
  background-color: #f8f8f8;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translate(-50%, -60%); }
  to { opacity: 1; transform: translate(-50%, -50%); }
}
</style>