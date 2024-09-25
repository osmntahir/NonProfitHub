<template>
  <div class="not-found">
    <h2>404 - Sayfa Bulunamadı</h2>
    <router-link to="/">Ana Sayfaya Git</router-link>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
};
</script>

<style scoped>
.not-found {
  text-align: center;
  margin-top: 2rem;
}

.not-found a {
  color: #007bff;
  text-decoration: none;
}
</style>
