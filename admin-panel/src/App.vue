<template>
  <div class="app-container">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h1>Admin Panel</h1>
      </div>
      <ul>
        <li @click="goToPage('news')" :class="{ active: isActivePage('news') }">Haberler</li>
        <li @click="goToPage('announcements')" :class="{ active: isActivePage('announcements') }">Duyurular</li>
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
    },
    isActivePage(page) {
      return this.$route.path.includes(page);
    }
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  overflow-x: hidden;
}

.app-container {
  display: flex;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
}

.sidebar {
  width: 250px;
  background-color: #142333;
  color: #fff;
  padding: 20px;
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-header h1 {
  font-size: 1.8em;
  color: #3fc380;
  font-weight: bold;
  text-align: center;
}

.sidebar-header {
  margin-bottom: 60px; /* Increased margin to push the list down */
  text-align: center;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
  flex-grow: 1;
}


.sidebar li {
  font-size: 1.2em;
  margin: 10px 0; /* Added margin to separate the list items further */
  cursor: pointer;
  padding: 10px 20px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.sidebar li:hover, .sidebar li.active {
  background-color: #3fc380;
}

.main-content {
  margin-left: 250px;
  padding: 20px;
  background-color: #f0f2f5;
  width: calc(100% - 250px);
  height: 100vh;
  overflow-y: auto;
}

.main-content h1 {
  font-size: 2.2em;
  margin-bottom: 20px;
  color: #333;
}

.main-content button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.main-content button:hover {
  background-color: #218838;
}
</style>
