<!-- src/components/Login.vue -->
<template>
  <div class="login-container">
    <h2>Giriş Yap</h2>
    <form @submit.prevent="login">
      <div class="form-group">
        <label>Kullanıcı Adı</label>
        <input v-model="username" type="text" placeholder="Kullanıcı Adınızı Girin" required />
        <div v-if="formErrors.username" class="error-message">{{ formErrors.username }}</div>
      </div>
      <div class="form-group">
        <label>Şifre</label>
        <input v-model="password" type="password" placeholder="Şifrenizi Girin" required />
        <div v-if="formErrors.password" class="error-message">{{ formErrors.password }}</div>
      </div>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      <div class="form-actions">
        <button type="submit" class="login-button">Giriş Yap</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from '@/plugins/axios';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      formErrors: {
        username: '',
        password: '',
      },
    };
  },
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    return { authStore, router };
  },
  methods: {
    async login() {
      this.clearFormErrors();
      this.errorMessage = '';

      if (!this.validateForm()) {
        return;
      }

      try {
        const response = await axios.post('/auth/token', null, {
          params: {
            username: this.username,
            password: this.password,
          },
        });
        const token = response.data.access_token || response.data.token; // Backend token yanıtınıza göre ayarlayın
        if (token) {
          this.authStore.setToken(token);
          this.router.push('/news'); // Giriş sonrası yönlendirme
        } else {
          this.errorMessage = 'Token alınamadı.';
        }
      } catch (error) {
        this.errorMessage = 'Giriş başarısız. Lütfen bilgilerinizi kontrol edin.';
        console.error(error);
      }
    },
    validateForm() {
      let isValid = true;

      if (!this.username) {
        this.formErrors.username = 'Kullanıcı adı gereklidir.';
        isValid = false;
      }

      if (!this.password) {
        this.formErrors.password = 'Şifre gereklidir.';
        isValid = false;
      }

      return isValid;
    },
    clearFormErrors() {
      this.formErrors = { username: '', password: '' };
    },
  },
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
}

.form-actions {
  text-align: center;
}

.login-button {
  background-color: #3fc380;
  color: white;
  border: none;
  padding: 10px 25px;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #28a745;
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}
</style>
