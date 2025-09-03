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
  </div>

</template>

<script setup lang="ts">
import { useMutation } from "@vue/apollo-composable";
import { DELETE_AUTHOR } from "../graphql/queries";

// ---- Type Author ----
interface Author {
  id: string;
  name: string;
  birthYear?: number | null;
  nationality?: string | null;
}

// ---- Props ----
defineProps<{
  authors: Author[];
}>();
// ---- Emits ----
const emit = defineEmits<{
  (e: "edit", author: Author): void;
  (e: "deleted", id: string): void;
}>();

// ---- Mutation ----
const { mutate: deleteAuthorMutation } = useMutation<{ deleteAuthor: boolean }>(DELETE_AUTHOR);

// ---- Methods ----
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
  padding: 8px;
  text-align: left;
}

th {
  background: #f0f0f0;
}

button {
  margin-right: 6px;
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:first-of-type {
  background: #ffc107; /* nút sửa: vàng */
}

button:last-of-type {
  background: #f44336; /* nút xóa: đỏ */
  color: white;
}
</style>
