<template>
  <div class="app">
    <!-- Login / Register -->
    <div v-if="!isAuthenticated" class="login-container">
      <h2>{{ isRegistering ? '📝 Đăng ký' : '🔑 Đăng nhập' }}</h2>

      <form @submit.prevent="isRegistering ? doRegister() : doLogin()">
        <input v-model="username" type="text" placeholder="Tên đăng nhập" required />
        <input v-model="password" type="password" placeholder="Mật khẩu" required />

        <!-- Thêm chọn role khi đăng ký -->
        <select v-if="isRegistering" v-model="role" required>
          <option disabled value="">-- Chọn vai trò --</option>
          <option value="STAFF">Staff</option>
          <option value="READER">Reader</option>
        </select>

        <button type="submit" :disabled="loading">
          {{ isRegistering ? 'Đăng ký' : 'Đăng nhập' }}
        </button>
      </form>

      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>

      <!-- Link chuyển đổi -->
      <p>
        <a href="#" @click.prevent="isRegistering = !isRegistering">
          {{ isRegistering ? 'Đã có tài khoản? Đăng nhập' : 'Chưa có tài khoản? Đăng ký' }}
        </a>
      </p>
    </div>

    <!-- Dashboard -->
    <div v-else>
      <nav class="navbar">
        <button class="nav-link" :class="{ active: currentTab==='books' }" @click="currentTab='books'">📚 Quản lý Sách</button>
        <button class="nav-link" :class="{ active: currentTab==='authors' }" @click="currentTab='authors'">✍️ Quản lý Tác giả</button>
        <button class="nav-link logout" @click="logout">🚪 Đăng xuất</button>
      </nav>

      <!-- Books Tab -->
      <div v-if="currentTab==='books'" class="content">
        <button @click="showBookSearch = true">🔍 Tìm kiếm Sách</button>
        <BookSearch
            :visible="showBookSearch"
            @close="showBookSearch = false"
            @searched="booksResult = $event"
            @selected="handleBookSelected"
        />
        <button class="btn add-btn" @click="onAddBook">✨ Thêm Sách</button>
        <BookForm
            v-if="showBookForm"
            :editingBook="editingBook"
            @saved="onBookSaved"
            @cancel="onBookCancel"
        />
        <BookList
            :books="booksResult || []"
            @edit="onEditBook"
            @deleted="refetchBooks"
        />
      </div>

      <!-- Authors Tab -->
      <div v-else class="content">
        <button @click="showAuthorSearch = true">🔍 Tìm kiếm Tác giả</button>
        <AuthorSearchPopup
            :visible="showAuthorSearch"
            @close="showAuthorSearch = false"
            @searched="authorsResult = $event"
            @selected="handleAuthorSelected"
        />
        <button class="btn add-btn" @click="onAddAuthor">✨ Thêm Tác giả</button>
        <AuthorForm
            v-if="showAuthorForm"
            :editingAuthor="editingAuthor"
            @saved="onAuthorSaved"
            @cancel="onAuthorCancel"
        />
        <AuthorList
            :authors="authorsResult || []"
            @edit="onEditAuthor"
            @deleted="refetchAuthors"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useMutation, useQuery } from "@vue/apollo-composable";
import { LOGIN_MUTATION, REGISTER_MUTATION, GET_ALL_BOOKS, GET_ALL_AUTHORS } from "./services/queries";

import BookForm from "./components/BookForm.vue";
import BookList from "./components/BookList.vue";
import BookSearch from "./components/BookSearch.vue";
import AuthorForm from "./components/AuthorForm.vue";
import AuthorList from "./components/AuthorList.vue";
import AuthorSearchPopup from "./components/AuthorSearchPopup.vue";

import type { Book } from "./modals/types/Book";
import type { Author } from "./modals/types/Author";

// --- Auth ---
const isAuthenticated = ref(!!localStorage.getItem("jwtToken"));
const isRegistering = ref(false);

const username = ref("");
const password = ref("");
const role = ref("");
const errorMsg = ref("");
const loading = ref(false);

const { mutate: login } = useMutation(LOGIN_MUTATION);
const { mutate: register } = useMutation(REGISTER_MUTATION);

async function doLogin() {
  loading.value = true;
  errorMsg.value = "";
  try {
    const result = await login({ input: { username: username.value, password: password.value } });
    const accessToken = result?.data?.login?.accessToken;
    if (!accessToken) throw new Error("Không nhận được token");
    localStorage.setItem("jwtToken", accessToken);
    isAuthenticated.value = true;
  } catch (err: any) {
    errorMsg.value = err.message || "Sai tài khoản hoặc mật khẩu";
    console.error(err);
  } finally {
    loading.value = false;
  }
}

async function doRegister() {
  loading.value = true;
  errorMsg.value = "";
  try {
    const result = await register({
      input: { username: username.value, password: password.value, role: role.value }
    });
    if (!result?.data?.register) throw new Error("Đăng ký thất bại");
    alert("🎉 Đăng ký thành công, hãy đăng nhập!");
    isRegistering.value = false; // chuyển về login
  } catch (err: any) {
    errorMsg.value = err.message || "Đăng ký thất bại";
    console.error(err);
  } finally {
    loading.value = false;
  }
}

function logout() {
  localStorage.removeItem("jwtToken");
  isAuthenticated.value = false;
}

// --- Tabs ---
const currentTab = ref<"books" | "authors">("books");

// --- Books State ---
const booksResult = ref<Book[] | null>(null);
const editingBook = ref<Book | null>(null);
const showBookForm = ref(false);
const showBookSearch = ref(false);

const { refetch: refetchBooks } = useQuery(GET_ALL_BOOKS, {}, { enabled: false });

function handleBookSelected(book: Book) {
  editingBook.value = book;
  showBookSearch.value = false;
}
function onAddBook() { editingBook.value = null; showBookForm.value = true; }
function onEditBook(book: Book) { editingBook.value = book; showBookForm.value = true; }
function onBookSaved() { showBookForm.value = false; editingBook.value = null; refetchBooks(); }
function onBookCancel() { showBookForm.value = false; editingBook.value = null; }

// --- Authors State ---
const authorsResult = ref<Author[] | null>(null);
const editingAuthor = ref<Author | null>(null);
const showAuthorForm = ref(false);
const showAuthorSearch = ref(false);

const { refetch: refetchAuthors } = useQuery(GET_ALL_AUTHORS, {}, { enabled: false });

function handleAuthorSelected(author: Author) {
  editingAuthor.value = author;
  showAuthorSearch.value = false;
}
function onAddAuthor() { editingAuthor.value = null; showAuthorForm.value = true; }
function onEditAuthor(author: Author) { editingAuthor.value = author; showAuthorForm.value = true; }
function onAuthorSaved() { showAuthorForm.value = false; editingAuthor.value = null; refetchAuthors(); }
function onAuthorCancel() { showAuthorForm.value = false; editingAuthor.value = null; }
</script>

<style scoped>
/* ------------------- Login ------------------- */
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  text-align: center;
}
.login-container input {
  display: block;
  width: 100%;
  margin: 10px 0;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
}
.login-container button {
  width: 100%;
  padding: 10px;
  background: #4a90e2;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.login-container button:disabled {
  background: #aaa;
}
.error { color: red; margin-top: 10px; }

/* ------------------- Navbar ------------------- */
.navbar {
  display: flex;
  gap: 10px;
  background: #f7f7f7;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}
.nav-link { background: none; border: none; padding: 10px 15px; cursor: pointer; }
.nav-link.active { font-weight: bold; border-bottom: 2px solid #4a90e2; }
.logout { margin-left: auto; color: red; }

/* ------------------- Content ------------------- */
.content { padding: 20px; }
.add-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 12px;
  margin-bottom: 10px;
  border-radius: 6px;
  cursor: pointer;
}
</style>
