<template>
  <div class="author-form">
    <h2>{{ isEditMode ? "✏️ Cập nhật Tác giả" : "➕ Thêm Tác giả" }}</h2>

    <form @submit.prevent="handleSubmit">
      <!-- Tên -->
      <div class="form-group">
        <label for="name">Tên</label>
        <input v-model="form.name" id="name" required />
      </div>

      <!-- Năm sinh -->
      <div class="form-group">
        <label for="birthYear">Năm sinh</label>
        <input v-model.number="form.birthYear" id="birthYear" type="number" required />
      </div>

      <!-- Quốc tịch -->
      <div class="form-group">
        <label for="nationality">Quốc tịch</label>
        <input v-model="form.nationality" id="nationality" required />
      </div>

      <!-- Submit -->
      <button type="submit" :disabled="loading">
        {{ loading ? "⏳ Đang xử lý..." : (isEditMode ? "Cập nhật" : "Thêm") }}
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed } from "vue";
import { useMutation } from "@vue/apollo-composable";
import { ADD_AUTHOR, UPDATE_AUTHOR } from "../graphql/queries";

// Props: nếu có author thì là edit mode
interface Author {
  id?: string;
  name: string;
  birthYear: number;
  nationality: string;
}

const props = defineProps<{
  author?: Author | null; // khi edit sẽ truyền vào, thêm mới thì null
}>();

const emit = defineEmits<{
  (e: "saved"): void; // emit khi save xong để refresh danh sách
}>();

// Form state
const form = reactive<Author>({
  id: props.author?.id,
  name: props.author?.name ?? "",
  birthYear: props.author?.birthYear ?? new Date().getFullYear(),
  nationality: props.author?.nationality ?? "",
});

// Xác định edit hay add
const isEditMode = computed(() => !!props.author);

// Mutations
const { mutate: addAuthor, loading: adding } = useMutation(ADD_AUTHOR);
const { mutate: updateAuthor, loading: updating } = useMutation(UPDATE_AUTHOR);

const loading = computed(() => adding.value || updating.value);

// Submit handler
async function handleSubmit() {
  try {
    if (isEditMode.value && form.id) {
      await updateAuthor({
        id: form.id,
        input: {
          name: form.name,
          birthYear: form.birthYear,
          nationality: form.nationality,
        },
      });
    } else {
      await addAuthor({
        input: {
          name: form.name,
          birthYear: form.birthYear,
          nationality: form.nationality,
        },
      });
    }
    emit("saved");
  } catch (err) {
    console.error("❌ Lỗi khi lưu tác giả:", err);
  }
}
</script>

<style scoped>
.author-form {
  max-width: 900px;
  margin: 1.5rem auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  text-align: center;
  color: var(--primary-color, #174a9e);
}

form {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  align-items: center;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: var(--text-color, #444);
}

input,
select {
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-color, #ccc);
  border-radius: 8px;
  font-size: 1rem;
  background: var(--secondary-color, #fafafa);
  transition: all 0.3s ease;
}

input:focus,
select:focus {
  border-color: var(--primary-color, #4a90e2);
  background: white;
  outline: none;
  box-shadow: 0 0 0 3px rgba(23, 74, 158, 0.2);
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

button {
  padding: 0.9rem 2rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button[type="submit"] {
  background-color: var(--primary-color, #4a90e2);
  color: white;
}

button[type="submit"]:hover:not(:disabled) {
  background-color: var(--active-bg-color, #357abd);
}

button[type="submit"]:disabled {
  background-color: var(--border-color, #b0c4de);
  cursor: not-allowed;
}

</style>
