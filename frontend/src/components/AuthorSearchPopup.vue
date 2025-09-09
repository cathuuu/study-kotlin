<template>
  <dialog v-if="visible" class="author-search" open>
    <h2>🔍 Tìm kiếm Tác giả</h2>

    <form @submit.prevent="handleSearch" class="form">
      <div class="form-group">
        <label for="keyword">Từ khóa</label>
        <input v-model="filter.keyword" id="keyword" placeholder="Nhập tên..." />
      </div>

      <div class="form-group">
        <label for="birthYear">Năm sinh</label>
        <input v-model.number="filter.birthYear" id="birthYear" type="number" />
      </div>

      <div class="form-group">
        <label for="nationality">Quốc tịch</label>
        <input v-model="filter.nationality" id="nationality" />
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? "⏳ Đang tìm..." : "Tìm kiếm" }}
      </button>
      <button type="button" @click="close">❌ Đóng</button>
    </form>

    <p v-if="error" class="error">❌ {{ error.message }}</p>

    <div class="results" v-if="data?.searchAuthors?.length">
      <h3>📖 Kết quả:</h3>
      <ul>
        <li
            v-for="author in data.searchAuthors"
            :key="author.id"
            @click="selectAuthor(author)"
            class="result-item"
        >
          <strong>{{ author.name }}</strong>
          ({{ author.birthYear ?? "?" }}) - {{ author.nationality ?? "Không rõ" }}
        </li>
      </ul>
    </div>

    <p v-else-if="!loading && searched">⚠️ Không tìm thấy tác giả nào.</p>
  </dialog>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { useLazyQuery } from "@vue/apollo-composable";
import { SEARCH_AUTHOR } from "../services/queries.ts";

// ---- Types ----
interface Author {
  id: string;
  name: string;
  birthYear?: number | null;
  nationality?: string | null;
}

interface AuthorSearchInput {
  keyword?: string | null;
  birthYear?: number | null;
  nationality?: string | null;
}

// ---- Props & Emits ----
const props = defineProps({
  visible: Boolean,
});
const emit = defineEmits<{
  (e: "close"): void;
  (e: "searched", authors: Author[]): void;
  (e: "selected", author: Author): void;
}>();

// ---- State ----
const filter = reactive<AuthorSearchInput>({
  keyword: "",
  birthYear: null,
  nationality: "",
});
const searched = ref(false);

// ---- Lazy Query ----
const { load, loading, result: data, error } = useLazyQuery<
    { searchAuthors: Author[] },
    { filter: AuthorSearchInput }
>(SEARCH_AUTHOR);

// ---- Watcher để reset form khi popup đóng ----
watch(
    () => props.visible,
    (newVal) => {
      if (!newVal) resetFilter();
      else searched.value = false;
    }
);

// ---- Methods ----
async function handleSearch() {
  searched.value = true;

  // Chuẩn hóa variables
  const variables: { filter: AuthorSearchInput } = {
    filter: {
      keyword: filter.keyword?.trim() || undefined,
      birthYear: filter.birthYear != null ? Number(filter.birthYear) : undefined,
      nationality: filter.nationality?.trim() || undefined,
    }
  };

  try {
    await load(null, variables);

    // Emit kết quả tìm kiếm
    emit("searched", data.value?.searchAuthors || []);

    // Reset filter ngay sau khi tìm xong
    resetFilter();
  } catch (err) {
    console.error(err);
  }
}

function selectAuthor(author: Author) {
  emit("selected", author);
  close();
}

function resetFilter() {
  filter.keyword = "";
  filter.birthYear = null;
  filter.nationality = "";
}

function close() {
  emit("close");
}
</script>

<style scoped>
.author-search {
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
  max-width: 500px;
  max-height: 80vh;   /* Giới hạn chiều cao popup */
  overflow-y: auto;   /* Bật cuộn khi nội dung dài */
  scrollbar-width: none;
  border: 1px solid #e0e0e0;
  animation: fadeIn 0.3s ease-out;
}


.author-search::backdrop {
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
}

.author-search h2 {
  font-size: 1.8rem;
  color: #174a9e;
  text-align: center;
  margin-bottom: 2rem;
  position: relative;
}

.author-search h2::after {
  content: '';
  display: block;
  width: 50px;
  height: 3px;
  background-color: #174a9e;
  margin: 10px auto 0;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
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
  max-height: 250px;   /* Giới hạn chiều cao */
  overflow-y: auto;    /* Bật cuộn dọc */
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

.results ul {
  list-style: none;
  padding: 0;
}

.result-item {
  padding: 1rem;
  border-bottom: 1px solid #f0f0f0;
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