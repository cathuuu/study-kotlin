<script setup lang="ts">
import { ref, reactive } from "vue";
import { useMutation } from "@vue/apollo-composable";
import { useRouter } from "vue-router";
import { LOGIN_MUTATION } from "../../services/queries";

// ====== Types ======
interface LoginResponse {
  login: {
    token: string;
  };
}

interface LoginVariables {
  input: {
    username: string;
    password: string;
  };
}

// ====== State ======
const router = useRouter();
const username = ref("");
const password = ref("");
const errors = reactive<{ username?: string; password?: string }>({});
const serverError = ref("");
const loading = ref(false);

// ====== Mutation ======
const { mutate: loginMutation } = useMutation<LoginResponse, LoginVariables>(LOGIN_MUTATION);

// ====== Validate ======
const validate = () => {
  errors.username = !username.value ? "Tên đăng nhập là bắt buộc" : "";
  errors.password = !password.value ? "Mật khẩu là bắt buộc" : "";
  return !errors.username && !errors.password;
};

// ====== Handle login ======
const handleLogin = async () => {
  serverError.value = "";
  if (!validate()) return;

  loading.value = true;
  try {
    const res = await loginMutation({
      input: { username: username.value, password: password.value },
    });

    console.log("✅ Login result:", res);

    const token = res?.data?.login?.token;
    if (token) {
      localStorage.setItem("token", token);
      router.push("/");
    } else {
      serverError.value = "Sai tên đăng nhập hoặc mật khẩu!";
    }
  } catch (err: any) {
    console.error("❌ Login error:", err);
    serverError.value = err.message || "Đăng nhập thất bại!";
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="login-container">
    <h2>Đăng nhập</h2>
    <form @submit.prevent="handleLogin" class="login-form">
      <div>
        <label>Tên đăng nhập</label>
        <input v-model="username" type="text" />
        <span v-if="errors.username" class="error">{{ errors.username }}</span>
      </div>

      <div>
        <label>Mật khẩu</label>
        <input v-model="password" type="password" />
        <span v-if="errors.password" class="error">{{ errors.password }}</span>
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? "Đang đăng nhập..." : "Đăng nhập" }}
      </button>

      <p v-if="serverError" class="error">{{ serverError }}</p>
    </form>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: auto;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.error {
  color: red;
  font-size: 0.9rem;
}
</style>
