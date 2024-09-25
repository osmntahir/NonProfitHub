<!-- src/App.vue -->
<template>
  <div id="app">
    <Navbar />
    <main>
      <router-view></router-view>
    </main>
    <footer>
      <p>&copy; 2024 CMV Bilişim. Tüm hakları saklıdır.</p>
    </footer>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";

export default {
  name: 'App',
  components: {
    Navbar,
  },
};
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  flex: 1;
}
</style>
