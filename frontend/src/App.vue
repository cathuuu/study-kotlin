<template>
  <div class="app">
    <!-- Navbar -->
    <nav class="navbar">

      <button
          class="nav-link"
          :class="{ active: currentTab === 'books' }"
          @click="currentTab = 'books'"
      >
        📚 Quản lý Sách
      </button>
      <button
          class="nav-link"
          :class="{ active: currentTab === 'authors' }"
          @click="currentTab = 'authors'"
      >
        ✍️ Quản lý Tác giả
      </button>

    </nav>

    <!-- BOOKS -->
    <div v-if="currentTab === 'books'" class="content">
      <h1>📚 Quản lý Sách</h1>
      <p v-if="loadingBooks">⏳ Đang tải...</p>
      <p v-if="errorBooks">❌ Lỗi: {{ errorBooks.message }}</p>

      <BookForm
          :editingBook="editingBook"
          @saved="handleBookSaved"
          @cancel="editingBook = null"
      />
      <BookList
          :books="books"
          @deleted="fetchBooks"
          @edit="editingBook = $event"
      />

      <!-- Popup search -->
      <button @click="showBookSearch = true">🔍 Tìm kiếm Sách</button>
      <BookSearch
          :visible="showBookSearch"
          @close="showBookSearch = false"
          @searched="books = $event"
          @selected="handleBookSelected"
      />
    </div>


    <!-- AUTHORS -->
    <div v-else class="content">
      <h1>✍️ Quản lý Tác giả</h1>
      <p v-if="loadingAuthors">⏳ Đang tải...</p>
      <p v-if="errorAuthors">❌ Lỗi: {{ errorAuthors.message }}</p>

      <AuthorForm
          :editingAuthor="editingAuthor"
          @saved="handleAuthorSaved"
          @cancel="editingAuthor = null"
      />
      <AuthorList
          :authors="authors"
          @deleted="fetchAuthors"
          @edit="editingAuthor = $event"
      />

      <!-- Popup search -->
      <button @click="showAuthorSearch = true">🔍 Tìm kiếm Tác giả</button>
      <AuthorSearch
          :visible="showAuthorSearch"
          @close="showAuthorSearch = false"
          @searched="authors = $event"
          @selected="handleAuthorSelected"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { useQuery, provideApolloClient } from "@vue/apollo-composable";
import apolloClient from "./apollo";

// Components
import BookList from "./components/BookList.vue";
import BookForm from "./components/BookForm.vue";
import AuthorList from "./components/AuthorList.vue";
import AuthorForm from "./components/AuthorForm.vue";
import AuthorSearch from "./components/AuthorSearchPopup.vue";

// GraphQL
import { GET_ALL_BOOKS, GET_ALL_AUTHORS } from "./services/queries.ts";
import BookSearch from "./components/BookSearch.vue";

/* ---------- Types ---------- */
interface Author {
  id: string;
  name: string;
  birthYear?: number | null;
  nationality?: string | null;
}

interface Book {
  id: string;
  title: string;
  publishedYear: number;
  price: number;
  quantity: number;
  author?: Author;
}

// Setup Apollo
provideApolloClient(apolloClient);

// State chung
const currentTab = ref<"books" | "authors">("books");
const showAuthorSearch = ref(false);

/* ---------- BOOK ---------- */
const books = ref<Book[]>([]);
const editingBook = ref<Book | null>(null);
const showBookSearch = ref(false);

// Khi chọn từ popup search Book
const handleBookSelected = (book: Book) => {
  editingBook.value = book;
  showBookSearch.value = false;
};


const {
  result: bookResult,
  loading: loadingBooks,
  error: errorBooks,
  refetch: refetchBooks,
} = useQuery<{ getAllBooks: Book[] }>(GET_ALL_BOOKS);

watch(bookResult, (val) => {
  books.value = val?.getAllBooks || [];
});

const fetchBooks = () => refetchBooks();
const handleBookSaved = () => {
  fetchBooks();
  editingBook.value = null;
};

/* ---------- AUTHOR ---------- */
const authors = ref<Author[]>([]);
const editingAuthor = ref<Author | null>(null);

const {
  result: authorResult,
  loading: loadingAuthors,
  error: errorAuthors,
  refetch: refetchAuthors,
} = useQuery<{ getAllAuthors: Author[] }>(GET_ALL_AUTHORS);

watch(authorResult, (val) => {
  authors.value = val?.getAllAuthors || [];
});

const fetchAuthors = () => refetchAuthors();
const handleAuthorSaved = () => {
  fetchAuthors();
  editingAuthor.value = null;
};

// Khi chọn từ popup search
const handleAuthorSelected = (author: Author) => {
  editingAuthor.value = author;
  showAuthorSearch.value = false;
};
</script>

<style scoped>
:root {
  --primary-color: #795548; /* Nâu cà phê */
  --secondary-color: #f5f5f5; /* Xám nhạt */
  --text-color: #4e342e; /* Nâu đen đậm */
  --hover-bg-color: #efebe9; /* Kem nhạt khi hover */
  --active-bg-color: #5d4037; /* Nâu đậm hơn khi active */
  --border-color: #d7ccc8; /* Màu border xám nâu */
  --border-radius: 10px;
  --box-shadow: 0 6px 15px rgba(0, 0, 0, 0.08);
}

.app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: var(--text-color);
  background-color: #f7f3f1;
  min-height: 100vh;
  padding: 3rem;
  box-sizing: border-box;
}

.navbar {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  padding: 1.5rem;
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  margin-bottom: 2.5rem;
}

.nav-link {
  background: none;
  border: none;
  font-weight: 600;
  font-size: 1.1rem;
  padding: 0.9rem 1.8rem;
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: all 0.3s;
}

.nav-link:hover {
  color: var(--primary-color);
}

.nav-link.active {
  color: white;
  background: var(--primary-color);
  box-shadow: 0 4px 10px rgba(121, 85, 72, 0.4);
}

.content {
  max-width: 950px;
  margin: 0 auto;
  padding: 2.5rem;
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
}

h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--primary-color);
  margin-bottom: 2rem;
  border-bottom: 3px solid var(--primary-color);
  padding-bottom: 0.75rem;
}

button {
  cursor: pointer;
  padding: 0.9rem 1.8rem;
  font-size: 1rem;
  border-radius: var(--border-radius);
  border: none;
  background-color: var(--primary-color);
  color: white;
  font-weight: 600;
  transition: all 0.3s;
  margin-top: 1.5rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

button:hover {
  background-color: var(--active-bg-color);
  transform: translateY(-2px);
}

button:disabled {
  background-color: #d7ccc8;
  cursor: not-allowed;
  box-shadow: none;
}
</style>
