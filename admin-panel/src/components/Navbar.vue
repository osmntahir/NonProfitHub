<!-- src/components/Navbar.vue -->
<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <h1>Dernek Yönetim Sistemi</h1>
    </div>
    <div class="navbar-actions" v-if="isAuthenticated">
      <span>Hoşgeldiniz, {{ username }}</span>
      <button @click="logout" class="logout-button">Çıkış Yap</button>
    </div>
  </nav>
</template>

<script>
import { useAuthStore } from '@/stores/auth';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Navbar',
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();

    const isAuthenticated = computed(() => authStore.isAuthenticated);
    const username = computed(() => authStore.user?.preferred_username || 'Kullanıcı');

    const logout = () => {
      authStore.clearToken();
      router.push('/login');
    };

    return { isAuthenticated, username, logout };
  },
};
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background-color: #3fc380;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  z-index: 1000;
}

.navbar-brand h1 {
  font-size: 1.5em;
}

.navbar-actions {
  display: flex;
  align-items: center;
}

.navbar-actions span {
  margin-right: 15px;
}

.logout-button {
  background-color: #dc3545;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-button:hover {
  background-color: #c82333;
}
</style>
