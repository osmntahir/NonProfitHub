<template>
  <div id="app">
    <nav>
      <router-link to="/">Anasayfa</router-link>
      <router-link to="/news">Haberler</router-link>
      <router-link to="/announcements">Duyurular</router-link>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
};
</script>

<style scoped>
nav {
  display: flex;
  gap: 20px;
  background-color: #333;
  padding: 10px;
}

nav a {
  color: white;
  text-decoration: none;
  padding: 10px;
}

nav a:hover {
  background-color: #555;
}
</style>
