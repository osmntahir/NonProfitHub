<template>
  <div class="news-list">
    <h1>Haberler</h1>
    <ul>
      <li v-for="news in newsList" :key="news.id">
        <router-link :to="`/news/${news.id}`">{{ news.subject }}</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import { getAllNews } from '../api/newsApi.js';

export default {
  data() {
    return {
      newsList: []
    };
  },
  async created() {
    this.newsList = await getAllNews();
  }
};
</script>

<style scoped>
.news-list {
  padding: 20px;
}
</style>
