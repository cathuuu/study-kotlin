<template>
  <div class="author-form">
    <h2>{{ isEditMode ? "✏️ Cập nhật Tác giả" : "➕ Thêm Tác giả" }}</h2>

    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="name">Tên</label>
        <input v-model="form.name" id="name" required />
      </div>

      <div class="form-group">
        <label for="birthYear">Năm sinh</label>
        <input id="birthYear" v-model.number="form.birthYear" type="number"/>
      </div>

      <div class="form-group">
        <label for="nationality">Quốc tịch</label>
        <input id="nationality" v-model="form.nationality"/>
      </div>

      <div class="form-actions">
        <button :disabled="loading" type="submit">
          {{ loading ? "⏳ Đang xử lý..." : (isEditMode ? "Cập nhật" : "Thêm") }}
        </button>
        <button :disabled="loading" type="button" @click="onCancel">Hủy</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import {computed, reactive, watch} from "vue";
import {useMutation} from "@vue/apollo-composable";
import {ADD_AUTHOR, UPDATE_AUTHOR} from "../services/queries.ts";

type Nullable<T> = T | null | undefined;

interface Author {
  id?: string;
  name: string;
  birthYear?: Nullable<number>;
  nationality?: Nullable<string>;
}

const props = defineProps<{
  editingAuthor: Author | null; // App.vue truyền :editingAuthor="editingAuthor"
}>();

const emit = defineEmits<{
  (e: "saved"): void;
  (e: "cancel"): void;
}>();

// ---- state form
const form = reactive<Author>({
  id: "",
  name: "",
  birthYear: null,
  nationality: "",
});

// ✅ đồng bộ form khi props.editingAuthor thay đổi
watch(
    () => props.editingAuthor,
    (a) => {
      if (a) {
        form.id = a.id ?? "";
        form.name = a.name ?? "";
        form.birthYear = a.birthYear ?? null;
        form.nationality = a.nationality ?? "";
      } else {
        resetForm();
      }
    },
    {immediate: true}
);

const isEditMode = computed(() => !!props.editingAuthor && !!form.id);

// ---- mutations
const { mutate: addAuthor, loading: adding } = useMutation(ADD_AUTHOR);
const { mutate: updateAuthor, loading: updating } = useMutation(UPDATE_AUTHOR);
const loading = computed(() => adding.value || updating.value);

// helper: chuẩn hóa input trước khi gửi
function normalizeInput() {
  return {
    name: form.name.trim(),
    birthYear:
        form.birthYear === null || form.birthYear === undefined || form.birthYear === ("" as any)
            ? null
            : Number(form.birthYear),
    nationality: form.nationality?.toString().trim() || null,
  };
}

async function handleSubmit() {
  try {
    const input = normalizeInput();

    if (isEditMode.value && form.id) {
      // ✅ GỬI ĐÚNG DẠNG BIẾN CHO BE: { id, input }
      await updateAuthor({
        id: form.id,
        input,
      });
    } else {
      // ✅ Thêm mới: { input }
      await addAuthor({input});
    }

    emit("saved");
    resetForm();
  } catch (err) {
    console.error("❌ Lỗi khi lưu tác giả:", err);
  }
}

function onCancel() {
  resetForm();
  emit("cancel");
}

function resetForm() {
  form.id = "";
  form.name = "";
  form.birthYear = null;
  form.nationality = "";
}
</script>

<style scoped>
.author-form {
  max-width: 900px;
  margin: 1.5rem auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, .1);
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
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: 600;
  margin-bottom: .5rem;
  color: var(--text-color, #444);
}

input {
  padding: .75rem 1rem;
  border: 1px solid var(--border-color, #ccc);
  border-radius: 8px;
  background: var(--secondary-color, #fafafa);
  transition: all .3s;
}

input:focus {
  border-color: var(--primary-color, #4a90e2);
  background: white;
  outline: none;
  box-shadow: 0 0 0 3px rgba(23, 74, 158, .2);
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: .5rem;
}

button {
  padding: .9rem 2rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: .2s;
}

button[type="submit"] {
  background: var(--primary-color, #4a90e2);
  color: #fff;
}

button[type="submit"]:disabled {
  background: #b0c4de;
  cursor: not-allowed;
}
</style>
