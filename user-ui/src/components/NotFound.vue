<!-- src/components/NotFound.vue -->
<template>
  <div class="not-found">
    <h1>404</h1>
    <p>Sayfa bulunamadı.</p>
    <router-link to="/" class="home-button">Anasayfaya Dön</router-link>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
};
</script>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 60px);
  background-color: #f0f2f5;
}

.not-found h1 {
  font-size: 6em;
  color: #e74c3c;
  margin-bottom: 20px;
}

.not-found p {
  font-size: 1.5em;
  color: #2c3e50;
  margin-bottom: 30px;
}

.home-button {
  padding: 12px 25px;
  font-size: 1.1em;
  color: #ffffff;
  background-color: #3498db;
  border: none;
  border-radius: 30px;
  text-decoration: none;
  transition: background-color 0.3s, transform 0.3s;
  font-family: 'Open Sans', sans-serif;
}

.home-button:hover {
  background-color: #2980b9;
  transform: scale(1.05);
}
</style>
