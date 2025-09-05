<template>
  <div class="author-list">
    <h2>📖 Danh sách Tác giả</h2>
    <table>
      <thead>
      <tr>
        <th>Tên</th>
        <th>Năm sinh</th>
        <th>Quốc tịch</th>
        <th>Hành động</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="author in authors" :key="author.id">
        <td>{{ author.name }}</td>
        <td>{{ author.birthYear || "-" }}</td>
        <td>{{ author.nationality || "-" }}</td>
        <td>
          <button @click="edit(author)">✏️</button>
          <button @click="remove(author.id)">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button :disabled="page === 0" @click="prevPage">Trước</button>
      <span>Trang {{page +1}} / {{totalPages}} </span>
      <button :disabled="page >= totalPages - 1" @click="nextPage">Tiếp</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useMutation, useQuery} from "@vue/apollo-composable";
import {DELETE_AUTHOR, SEARCH_AUTHOR_PAGE_NATIVE} from "../services/queries.ts";
import {computed, ref} from "vue";

interface Author {
  id: string;
  name: string;
  birthYear?: number | null;
  nationality?: string | null;
}

defineProps<{ authors: Author[] }>();

const emit = defineEmits<{
  (e: "edit", author: Author): void;
  (e: "deleted", id: string): void;
}>();
//phan trang
const page = ref(0);
const size = ref(5);

const { result, refetch } = useQuery(SEARCH_AUTHOR_PAGE_NATIVE, {
  page: page.value,
  size: size.value,
});
const { mutate: deleteAuthorMutation } = useMutation<{ deleteAuthor: boolean }>(
    DELETE_AUTHOR
);
  const authors = computed<Author[]>(() => result.value?.getAuthorsByPage?.content || []);
  const totalPages = computed<number>(() => result.value?.getAuthorsByPage?.totalPages || 0);
  function nextPage() {
    if (page.value < totalPages.value - 1) {
      page.value++;
      refetch({ page: page.value, size: size.value });
    }
  }

  function prevPage() {
    if (page.value > 0) {
      page.value--;
      refetch({ page: page.value, size: size.value });
    }
  }
function edit(author: Author) {
  emit("edit", author);
}

async function remove(id: string) {
  if (!confirm("Bạn có chắc muốn xóa tác giả này?")) return;
  await deleteAuthorMutation({ id });
  emit("deleted", id);
}
</script>

<style scoped>
.author-list {
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 12px 8px;
  text-align: left;
  vertical-align: top;
}

th {
  background: #f0f0f0;
}

.status-message {
  text-align: center;
  padding: 1rem;
  font-style: italic;
  color: #666;
}

.status-message.error {
  color: #d32f2f;
  font-weight: bold;
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border-left-color: #2196f3;
  animation: spin 1s ease infinite;
  display: inline-block;
  vertical-align: middle;
  margin-right: 8px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.btn {
  margin-right: 6px;
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn.edit {
  background: #ffc107;
}

.btn.delete {
  background: #f44336;
  color: white;
}
</style>