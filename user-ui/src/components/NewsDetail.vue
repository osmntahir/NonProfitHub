<!-- src/components/NewsDetail.vue -->
<template>
  <div class="news-detail">
    <div v-if="news" class="news-container">
      <h1 class="news-title">{{ news.subject }}</h1>
      <img
          v-if="news.imagePath"
          :src="getImageUrl(news.imagePath)"
          alt="Haber Resmi"
          class="news-image"
      />
      <p class="news-content">{{ news.content }}</p>
      <p class="news-validity">
        <i class="fas fa-calendar-alt"></i>
        <strong>Geçerlilik Tarihi:</strong> {{ news.validityDate }}
      </p>
      <router-link to="/news" class="back-button">Geri Dön</router-link>
    </div>
    <div v-else class="error-container">
      <p class="error-message">{{ errorMessage }}</p>
      <router-link to="/" class="home-button">Anasayfaya Dön</router-link>
    </div>
  </div>
</template>

<script>
import { getNewsById } from '../api/newsApi.js';
import DateFormatter from '../utils/DateFormatter';

export default {
  name: 'NewsDetail',
  props: ['id'],
  data() {
    return {
      news: null,
      errorMessage: '',
    };
  },
  async created() {
    const newsId = this.$route.params.id;
    try {
      let newsData = await getNewsById(newsId);

      if (newsData.validityDate) {
        const formatter = new DateFormatter(newsData.validityDate);
        newsData.validityDate = formatter.getFormattedDate();
      }

      this.news = newsData;
    } catch (error) {
      this.errorMessage = 'Haber bulunamadı.';
    }
  },
  methods: {
    getImageUrl(imagePath) {
      return `http://localhost:8080/uploads/images/${imagePath.split('/').pop()}`;
    },
  },
};
</script>

<style scoped>
.news-detail {
  width: 90%;
  max-width: 800px;
  margin: 60px auto;
  padding: 20px;
  background: #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.news-container {
  text-align: center;
}

.news-title {
  font-size: 2.8em;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 20px;
  font-family: 'Roboto', sans-serif;
  position: relative;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.news-title::after {
  content: '';
  width: 80px;
  height: 4px;
  background: #e74c3c;
  display: block;
  margin: 10px auto 0;
  border-radius: 2px;
}

.news-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);
  transition: transform 0.3s;
}

.news-image:hover {
  transform: scale(1.02);
}

.news-content {
  font-size: 1.6em;
  line-height: 1.8;
  color: #555;
  font-family: 'Open Sans', sans-serif;
  margin-bottom: 20px;
  text-align: left;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.news-validity {
  font-size: 1.3em;
  color: #888;
  display: flex;
  align-items: center;
  justify-content: center;
}

.news-validity i {
  margin-right: 8px;
  color: #e74c3c;
}

.back-button,
.home-button {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #3498db;
  color: #ffffff;
  text-decoration: none;
  border-radius: 25px;
  transition: background-color 0.3s, transform 0.3s;
  font-family: 'Open Sans', sans-serif;
}

.back-button:hover,
.home-button:hover {
  background-color: #2980b9;
  transform: scale(1.05);
}

.error-container {
  text-align: center;
  padding: 60px 20px;
}

.error-message {
  font-size: 1.5em;
  color: #ff4d4f;
  font-family: 'Open Sans', sans-serif;
  margin-bottom: 20px;
}


@media (max-width: 600px) {
  .news-title {
    font-size: 2.2em;
  }

  .news-content {
    font-size: 1.4em;
  }
}
</style>
