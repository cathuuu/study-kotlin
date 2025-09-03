<template>
  <div>
    <h2>📚 Danh sách sách</h2>

    <!-- Bảng hiển thị danh sách -->
    <table v-if="books.length > 0" class="book-table">
      <thead>
      <tr>
        <th>Tiêu đề</th>
        <th>Năm XB</th>
        <th>Giá</th>
        <th>Số lượng</th>
        <th>Tác giả</th>
        <th>Hành động</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="book in books" :key="book.id">
        <td>{{ book.title }}</td>
        <td>{{ book.publishedYear }}</td>
        <td>{{ book.price }}</td>
        <td>{{ book.quantity }}</td>
        <td>{{ book.author?.name }}</td>
        <td>
          <button @click="startEdit(book)">✏️ Sửa</button>
          <button @click="deleteBook(book.id)" :disabled="deleteLoading">🗑️ Xóa</button>
        </td>
      </tr>
      </tbody>
    </table>
    <p v-else>Không có sách nào.</p>

    <!-- Form chỉnh sửa -->
    <div v-if="isEditing" class="edit-form">
      <h3>✏️ Sửa sách</h3>
      <form @submit.prevent="submitEdit">
        <label>Tiêu đề:
          <input v-model="form.title" type="text" required />
        </label>
        <label>Năm xuất bản:
          <input v-model.number="form.publishedYear" type="number" required />
        </label>
        <label>Giá:
          <input v-model.number="form.price" type="number" step="0.01" required />
        </label>
        <label>Số lượng:
          <input v-model.number="form.quantity" type="number" required />
        </label>

        <!-- Dropdown chọn tác giả -->
        <label>Tác giả:
          <select v-model="form.authorId" required>
            <option disabled value="">-- Chọn tác giả --</option>
            <option v-for="author in authors" :key="author.id" :value="author.id">
              {{ author.name }}
            </option>
          </select>
        </label>

        <div class="buttons">
          <button type="submit" :disabled="updateLoading">💾 Lưu</button>
          <button type="button" @click="cancelEdit">❌ Hủy</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { useQuery, useMutation } from "@vue/apollo-composable";
import { GET_ALL_BOOKS, GET_ALL_AUTHORS, UPDATE_BOOK, DELETE_BOOK } from "../graphql/queries";

// ==== Types ====
interface Author {
  id: string;
  name: string;
}

interface Book {
  id: string;
  title: string;
  publishedYear: number;
  price: number;
  quantity: number;
  author?: Author;
}

interface BookForm {
  id: string;
  title: string;
  publishedYear: number | null;
  price: number | null;
  quantity: number | null;
  authorId: string;
}

// ==== Queries ====
const { result: booksResult } = useQuery<{ getAllBooks: Book[] }>(GET_ALL_BOOKS);
const books = computed<Book[]>(() => booksResult.value?.getAllBooks || []);

const { result: authorsResult } = useQuery<{ getAllAuthors: Author[] }>(GET_ALL_AUTHORS);
const authors = computed<Author[]>(() => authorsResult.value?.getAllAuthors || []);

// ==== Mutations ====
const { mutate: updateBookMutation, loading: updateLoading } = useMutation(UPDATE_BOOK, {
  update: (cache, { data }) => {
    const updated: Book | undefined = data?.updateBook;
    if (!updated) return;
    try {
      const existing = cache.readQuery<{ getAllBooks: Book[] }>({ query: GET_ALL_BOOKS });
      if (!existing?.getAllBooks) return;
      cache.writeQuery({
        query: GET_ALL_BOOKS,
        data: {
          getAllBooks: existing.getAllBooks.map((b) => (b.id === updated.id ? updated : b)),
        },
      });
    } catch {
      // cache miss
    }
  },
});

const { mutate: deleteBookMutation, loading: deleteLoading } = useMutation(DELETE_BOOK, {
  update: (cache, _res, { variables }) => {
    const delId: string | undefined = variables?.id;
    if (!delId) return;
    cache.modify({
      fields: {
        getAllBooks(existingRefs = [], { readField }) {
          return existingRefs.filter((ref: any) => readField("id", ref) !== delId);
        },
      },
    });
  },
});

// ==== State ====
const isEditing = ref(false);
const form = ref<BookForm>({
  id: "",
  title: "",
  publishedYear: null,
  price: null,
  quantity: null,
  authorId: "",
});

// ==== Logic ====
function startEdit(book: Book) {
  isEditing.value = true;
  form.value = {
    id: book.id,
    title: book.title,
    publishedYear: book.publishedYear,
    price: book.price,
    quantity: book.quantity,
    authorId: book.author?.id || "",
  };
}

function cancelEdit() {
  isEditing.value = false;
}

async function submitEdit() {
  try {
    await updateBookMutation({
      id: form.value.id,
      authorId: form.value.authorId,
      input: {
        title: form.value.title,
        publishedYear: Number(form.value.publishedYear),
        price: Number(form.value.price),
        quantity: Number(form.value.quantity),
      },
    });
    alert("✅ Cập nhật thành công!");
    isEditing.value = false;
  } catch (e: any) {
    console.error("❌ Lỗi khi cập nhật:", e);
    alert(`❌ Cập nhật thất bại: ${e.message}`);
  }
}

async function deleteBook(bookId: string) {
  try {
    await deleteBookMutation({ id: bookId });
    alert("🗑️ Đã xóa sách!");
  } catch (e: any) {
    console.error("❌ Lỗi khi xóa:", e);
    alert(`❌ Xóa thất bại: ${e.message}`);
  }
}
</script>

<style scoped>
.book-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
.book-table th,
.book-table td {
  border: 1px solid #ddd;
  padding: 8px;
}
.book-table th {
  background: #f5f5f5;
}
.edit-form {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: #fafafa;
}
.edit-form label {
  display: block;
  margin-bottom: 10px;
}
.edit-form input,
.edit-form select {
  margin-left: 5px;
}
.buttons {
  margin-top: 10px;
}
</style>
