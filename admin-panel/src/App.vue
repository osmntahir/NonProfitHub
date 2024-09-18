<template>
  <div class="app-container">
    <!-- Sidebar -->
    <aside class="sidebar">
      <ul>
        <li @click="goToPage('news')">Haberler</li>
        <li @click="goToPage('announcements')">Duyurular</li>
      </ul>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script>
export default {
  methods: {
    goToPage(page) {
      this.$router.push(`/${page}`);
    }
  }
};
</script>

<style scoped>
.app-container {
  display: flex;
}

.sidebar {
  width: 200px;
  background-color: #333;
  color: white;
  padding: 20px;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  margin: 20px 0;
  cursor: pointer;
  color: white;
}

.main-content {
  margin-left: 220px;
  padding: 20px;
  width: calc(100% - 220px);
}
</style>
