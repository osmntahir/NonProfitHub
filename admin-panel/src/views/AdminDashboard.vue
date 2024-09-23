<!-- src/views/AdminDashboard.vue -->
<template>
  <div class="admin-dashboard">
    <h1>Admin Dashboard</h1>
    <div class="sections">
      <router-link to="/admin/news">Manage News</router-link>
      <router-link to="/admin/announcements">Manage Announcements</router-link>
    </div>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'AdminDashboard',
};
</script>

<style scoped>
.admin-dashboard {
  padding: 2rem;
}

.sections {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.sections a {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}
</style>
