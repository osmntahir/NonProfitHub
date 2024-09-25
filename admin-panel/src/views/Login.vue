<template>
  <div class="login-container">
    <h2>Giriş Yap</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Kullanıcı Adı:</label>
        <input v-model="username" type="text" id="username" required />
      </div>
      <div>
        <label for="password">Şifre:</label>
        <input v-model="password" type="password" id="password" required />
      </div>
      <button type="submit">Giriş Yap</button>
      <p v-if="error" class="error">{{ error }}</p>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import api from '../services/api';
import { useAuthStore } from '../store';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const username = ref('');
    const password = ref('');
    const error = ref('');
    const authStore = useAuthStore();
    const router = useRouter();

    const handleLogin = async () => {
      try {
        const response = await api.post('/auth/token', null, {
          params: {
            username: username.value,
            password: password.value,
          },
        });

        // Token'ın doğru alınıp alınmadığını kontrol edin
        console.log('Giriş Yanıtı:', response.data);

        const token = response.data.access_token;

        if (token) {
          authStore.login(token);
          router.push('/admin');
        } else {
          throw new Error('Token bulunamadı');
        }
      } catch (err) {
        console.error('Giriş hatası:', err);
        error.value = 'Geçersiz kullanıcı adı veya şifre';
      }
    };

    return {
      username,
      password,
      handleLogin,
      error,
    };
  },
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-container h2 {
  text-align: center;
  margin-bottom: 1rem;
}

.login-container form div {
  margin-bottom: 1rem;
}

.login-container label {
  display: block;
  margin-bottom: 0.5rem;
}

.login-container input {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
}

.login-container button {
  width: 100%;
  padding: 0.75rem;
}

.error {
  color: red;
  text-align: center;
  margin-top: 1rem;
}
</style>
