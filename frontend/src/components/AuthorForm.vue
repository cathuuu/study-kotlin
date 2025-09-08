<template>
  <div class="author-form-container">
    <h3>{{ editingAuthor ? '✏️ Chỉnh sửa Tác giả' : '✨ Thêm Tác giả mới' }}</h3>

    <!-- Form -->
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="name">Tên</label>
        <input id="name" v-model="authorForm.name" required />
      </div>

      <div class="form-group">
        <label for="birthYear">Năm sinh</label>
        <input id="birthYear" v-model.number="authorForm.birthYear" type="number" />
      </div>

      <div class="form-group">
        <label for="nationality">Quốc tịch</label>
        <input id="nationality" v-model="authorForm.nationality" />
      </div>

      <div class="form-actions">
        <button type="submit" class="btn save-btn">Lưu</button>
        <button type="button" @click="handleCancel" class="btn cancel-btn" v-if="editingAuthor">Hủy</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch } from "vue";
import { useMutation } from "@vue/apollo-composable";
import { ADD_AUTHOR, UPDATE_AUTHOR } from "../services/queries";

// ====== Types ======
export interface Author {
  id?: string;
  name: string;
  birthYear?: number | null;
  nationality?: string | null;
}

// ====== Props & Emit ======
const props = defineProps<{ editingAuthor: Author | null }>();
const emit = defineEmits<{
  (e: "saved"): void;
  (e: "cancel"): void;
}>();

// ====== Reactive Form State ======
const authorForm = reactive<Author>({
  id: "",
  name: "",
  birthYear: null,
  nationality: ""
});

// ====== Mutation ======
const { mutate: addAuthorMutation } = useMutation(ADD_AUTHOR);
const { mutate: updateAuthorMutation } = useMutation(UPDATE_AUTHOR);

// ====== Watch Editing Author ======
watch(() => props.editingAuthor, (val) => {
  if (val) Object.assign(authorForm, val);
  else resetForm();
}, { immediate: true });

// ====== Helper Functions ======
function resetForm() {
  Object.assign(authorForm, { id: "", name: "", birthYear: null, nationality: "" });
}

// ====== Handle Submit ======
const handleSubmit = async () => {
  const input = {
    name: authorForm.name.trim(),
    birthYear: authorForm.birthYear ?? null,
    nationality: authorForm.nationality?.trim() || null
  };

  try {
    if (authorForm.id) {
      await updateAuthorMutation({ id: authorForm.id, input });
    } else {
      await addAuthorMutation({ input });
    }
    emit("saved"); // parent refetch
    resetForm();
  } catch (err) {
    console.error("❌ Lỗi khi lưu tác giả:", err);
  }
};

// ====== Handle Cancel ======
const handleCancel = () => {
  resetForm();
  emit("cancel");
};
</script>

<style scoped>
.author-form-container { padding:20px; border:1px solid #ccc; border-radius:8px; max-width:500px; margin:0 auto 20px; }
.form-group { margin-bottom:15px; }
label { display:block; font-weight:bold; margin-bottom:5px; }
input { width:100%; padding:8px; border:1px solid #ccc; border-radius:4px; }
.form-actions { display:flex; justify-content:flex-end; gap:10px; }
.btn { padding:8px 16px; border:none; border-radius:4px; cursor:pointer; font-weight:bold; }
.save-btn { background-color:#4CAF50; color:white; }
.cancel-btn { background-color:#f44336; color:white; }
</style>
