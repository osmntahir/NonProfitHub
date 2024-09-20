<template>
  <div class="news-list">
    <h1>Haberler</h1>
    <div
        v-for="news in newsList"
        :key="news.id"
        class="news-item"
    >
      <div class="news-content">
        <h2>{{ news.subject }}</h2>
        <p>{{ getShortContent(news.content) }}...</p>
        <!-- İki seçenek: Detayı oku veya Habere git -->
        <div class="news-actions">
          <button @click="openModal(news)">Detayı Oku</button>
          <router-link :to="`/news/${news.id}`">Habere Git</router-link>
        </div>
      </div>
    </div>

    <!-- Modal: Eğer selectedNews doluysa modal açılır -->
    <div v-if="selectedNews" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h2>{{ selectedNews.subject }}</h2>
        <p>{{ selectedNews.content }}</p>
        <p class="validity-date">
          <i class="fas fa-calendar-alt"></i>
          {{ formattedValidityDate(selectedNews.validityDate) }}
        </p>
        <button @click="closeModal">Kapat</button>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllNews, getNewsById } from '../api/newsApi.js';
import DateFormatter from '../utils/DateFormatter';

export default {
  data() {
    return {
      newsList: [],
      selectedNews: null, // Seçilen haber modalda gösterilecek
    };
  },
  async created() {
    this.newsList = await getAllNews();
  },
  methods: {
    formattedValidityDate(date) {
      const dateFormatter = new DateFormatter(date);
      return dateFormatter.getFormattedDate();
    },
    getShortContent(content) {
      const maxLength = 150;
      return content.length > maxLength ? content.substring(0, maxLength) : content;
    },
    openModal(news) {
      this.selectedNews = news;
    },
    closeModal() {
      this.selectedNews = null;
    },
  },
};
</script>

<style scoped>
.news-list {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.news-list h1 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 2.5em;
  color: #333;
}

.news-item {
  margin-bottom: 40px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 20px;
}

.news-content {
  flex: 1;
  min-width: 0;
}

.news-content h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.8em;
  color: #2c3e50;
  word-wrap: break-word;
  cursor: pointer;
}

.news-content p {
  text-align: left;
  font-size: 1.1em;
  line-height: 1.6;
  color: #555;
}

.news-actions {
  display: flex;
  gap: 10px;
}

.news-actions button, .news-actions a {
  padding: 8px 15px;
  font-size: 1em;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
}

.news-actions button {
  background-color: #3498db;
  color: #fff;
}

.news-actions button:hover {
  background-color: #2980b9;
}

.news-actions a {
  background-color: #f39c12;
  color: #fff;
}

.news-actions a:hover {
  background-color: #e67e22;
}

/* Modal stilleri */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 10px;
  width: 80%;
  max-width: 800px;
  word-break: break-word; /* Metin taşmalarını engellemek için */
  white-space: normal; /* Uzun kelimeleri bölmek için */
}

.modal-content h2 {
  margin-top: 0;
  font-size: 2em;
}

.modal-content p {
  margin-bottom: 20px;
  font-size: 1.2em;
  line-height: 1.6;
}

.modal-content button {
  padding: 10px 20px;
  font-size: 1em;
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.modal-content button:hover {
  background-color: #2980b9;
}

/* Responsive tasarım */
@media (max-width: 768px) {
  .news-content h2,
  .news-content p {
    text-align: center;
  }
}
</style>
