<script setup lang="ts">
import { useAuthorForm } from "../composables/useAuthorForm";

const props = defineProps({
  editingAuthor: { type: Object, default: null },
});
const emit = defineEmits(["saved", "cancel", "search"]);

const {
  name,
  birthYear,
  nationality,
  searchKeyword,
  searchNationality,
  searchBirthYear,
  handleSubmit,
  cancelEdit,
  emitSearch,
  resetSearch,
} = useAuthorForm(props.editingAuthor, emit);
</script>

<template>
  <div class="author-form">
    <h2>{{ editingAuthor ? "✏️ Sửa Tác giả" : "➕ Thêm Tác giả" }}</h2>

    <!-- Form thêm/sửa -->
    <form @submit.prevent="handleSubmit" class="form-box">
      <input v-model="name" placeholder="Tên tác giả" required />
      <input v-model.number="birthYear" type="number" placeholder="Năm sinh" />
      <input v-model="nationality" placeholder="Quốc tịch" />

      <button type="submit">
        {{ editingAuthor ? "Cập nhật" : "Thêm" }}
      </button>
      <button v-if="editingAuthor" type="button" @click="cancelEdit">Hủy</button>
    </form>

    <!-- Ô tìm kiếm -->
    <div class="search-box">
      <h3>🔍 Tìm kiếm tác giả</h3>
      <form @submit.prevent="emitSearch" class="form-box">
        <input v-model="searchKeyword" placeholder="Tìm theo tên" />
        <input v-model="searchNationality" placeholder="Quốc tịch" />
        <input
            v-model.number="searchBirthYear"
            type="number"
            placeholder="Năm sinh"
        />
        <button type="submit">Tìm kiếm</button>
        <button type="button" @click="resetSearch">Xóa lọc</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.author-form {
  margin-bottom: 20px;
}
.form-box {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 10px;
}
input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button[type="submit"] {
  background: #1976d2;
  color: white;
}
button[type="button"] {
  background: #f44336;
  color: white;
}
</style>