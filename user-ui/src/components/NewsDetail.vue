<template>
  <div v-if="news" class="news-container">
    <h1 class="news-title">{{ news.subject }}</h1>
    <p class="news-content">{{ news.content }}</p>
    <p class="news-validity"><strong>Geçerlilik Tarihi:</strong> {{ news.validityDate }}</p>
  </div>
  <div v-else class="error-container">
    <p class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import {getNewsById} from '../api/newsApi.js';

export default {
  data() {
    return {
      news: null,
      errorMessage: ''
    };
  },
  async mounted() {
    const newsId = this.$route.params.id;
    try {
      this.news = await getNewsById(newsId);
    } catch (error) {
      this.errorMessage = 'Haber bulunamadı.';
    }
  }
};
</script>

<style scoped>
.news-container, .error-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

body {
  font-family: 'Roboto', sans-serif;
}

.news-title {
  font-size: 2.8em;
  font-weight: 700;
  color: #222;
  margin-bottom: 30px;
  word-break: break-word;
}

.news-content {
  font-size: 1.6em;
  line-height: 1.8;
  color: #444;
  font-weight: 400;
  margin-bottom: 20px;
  word-break: break-word;
}

.news-validity {
  font-size: 1.3em;
  color: #555;
  margin-bottom: 20px;
}

.news-link {
  font-size: 1.3em;
  color: #007bff;
  font-weight: 600;
  text-decoration: none;
  word-wrap: break-word;
}

.news-link:hover {
  text-decoration: underline;
  color: #0056b3;
}

.error-message {
  font-size: 1.5em;
  color: #ff4d4f;
  text-align: center;
  margin-top: 50px;
}
</style>