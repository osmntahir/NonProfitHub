<template>
  <nav>
    <router-link to="/">Ana Sayfa</router-link>
    <template v-if="isAuthenticated">
      <span>Hoşgeldiniz, {{"Admin"}}</span>
      <button @click="logoutUser">Çıkış Yap</button>
    </template>
    <template v-else>
      <router-link to="/login">Giriş Yap</router-link>
    </template>
  </nav>
</template>

<script>
import { computed } from 'vue';
import { useAuthStore } from '../store';
import { useRouter } from 'vue-router';

export default {
  name: 'Navbar',
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();

    const isAuthenticated = computed(() => authStore.isAuthenticated);
    const user = computed(() => authStore.user || 'Kullanıcı');

    const logoutUser = () => {
      authStore.logout();
      router.push('/login');
    };

    return {
      isAuthenticated,
      user,
      logoutUser,
    };
  },
};
</script>

<style scoped>
nav {
  padding: 1rem;
  background-color: #f8f9fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

nav a {
  margin-right: 1rem;
  text-decoration: none;
  color: #333;
}

nav button {
  padding: 0.5rem 1rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
