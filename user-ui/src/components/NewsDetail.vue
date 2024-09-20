<template>
  <div class="news-detail" v-if="news">
    <h1>{{ news.subject }}</h1>
    <p>{{ news.content }}</p>
    <p>Geçerlilik Tarihi: {{ news.validityDate }}</p>
    <a :href="news.newsLink" target="_blank">Detaylı Haber</a>
  </div>
</template>

<script>
import { getNewsById } from '../api/newsApi.js';

export default {
  props: ['id'],
  data() {
    return {
      news: null
    };
  },
  async created() {
    this.news = await getNewsById(this.id);
  }
};
</script>

<style scoped>
.news-detail {
  padding: 20px;
}
</style>
