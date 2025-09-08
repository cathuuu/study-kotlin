<template>
  <div class="book-form">
    <h2>📚 Thêm sách mới</h2>

    <form @submit.prevent="addBook">
      <input v-model="newBook.title" type="text" placeholder="Tên sách" required />
      <input v-model.number="newBook.publishedYear" type="number" placeholder="Năm xuất bản" />
      <input v-model.number="newBook.price" type="number" step="0.01" placeholder="Giá" />
      <input v-model.number="newBook.quantity" type="number" placeholder="Số lượng" />

      <select v-model="newBook.authorId" required>
        <option value="">-- Chọn tác giả --</option>
        <option v-for="author in authors" :key="author.id" :value="author.id">
          {{ author.name }}
        </option>
      </select>

      <button type="submit" :disabled="loading">
        <span v-if="loading">⏳</span>
        <span v-else>➕</span>
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from "vue";
import { useQuery, useMutation } from "@vue/apollo-composable";
import { ADD_BOOK, GET_ALL_BOOKS, GET_ALL_AUTHORS } from "../services/queries.ts";

// types
interface Author {
  id: string;
  name: string;
}

export interface BookInput {
  title: string;
  publishedYear: number | null;
  price: number | null;
  quantity: number | null;
  authorId: string;
}

const newBook = reactive<BookInput>({
  title: "",
  publishedYear: null,
  price: null,
  quantity: null,
  authorId: "",
});

const loading = ref(false);

// query authors
const { result: authorsResult } = useQuery<{ getAllAuthors: Author[] }>(GET_ALL_AUTHORS);
const authors = computed<Author[]>(() => authorsResult.value?.getAllAuthors || []);

const { mutate: addBookMutation } = useMutation(ADD_BOOK, {
  update: (cache, { data }) => {
    const added = data?.addBook;
    if (!added) return;
    try {
      const cached = cache.readQuery<{ getAllBooks: any[] }>({ query: GET_ALL_BOOKS });
      if (cached?.getAllBooks) {
        cache.writeQuery({
          query: GET_ALL_BOOKS,
          data: { getAllBooks: [...cached.getAllBooks, added] },
        });
      }
    } catch {
      // ignore cache miss
    }
  },
});

const addBook = async () => {
  if (!newBook.title.trim() || !newBook.authorId) return;
  loading.value = true;
  try {
    await addBookMutation({
      authorId: newBook.authorId,
      input: {
        title: newBook.title.trim(),
        publishedYear: newBook.publishedYear || null,
        price: newBook.price ?? null,
        quantity: newBook.quantity ?? null,
      },
    });

    Object.assign(newBook, {
      title: "",
      publishedYear: null,
      price: null,
      quantity: null,
      authorId: "",
    });
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.book-form {
  max-width: 100%;
  margin: 10px auto;
  padding: 12px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

h2 {
  font-size: 18px;
  margin-bottom: 12px;
  text-align: center;
  color: #2c3e50;
}

form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
  justify-content: space-between;
}

input,
select {
  flex: 1;
  min-width: 140px;
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  background: #fafafa;
}

input:focus,
select:focus {
  border-color: #4a90e2;
  background: #fff;
  outline: none;
}

button[type="submit"] {
  padding: 8px 14px;
  background: #4a90e2;
  color: white;
  font-size: 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  flex-shrink: 0;
}

button[type="submit"]:hover:not(:disabled) {
  background: #357abd;
}

button[type="submit"]:disabled {
  background: #a8c5e6;
  cursor: not-allowed;
}

</style>
