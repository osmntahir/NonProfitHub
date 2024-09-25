<!-- src/components/NewsList.vue -->
<template>
  <div class="news-list">
    <h1>Haberler</h1>
    <div v-for="news in newsList" :key="news.id" class="news-item">
      <div class="news-content">
        <h2>{{ news.subject }}</h2>
        <p>{{ getShortContent(news.content) }}...</p>
        <p class="validity-date">
          <i class="fas fa-calendar-alt"></i>
          {{ formattedValidityDate(news.validityDate) }}
        </p>
        <div class="news-actions">
          <button @click="openModal(news)">Detayı Oku</button>
          <router-link :to="`/news/${news.id}`">Habere Git</router-link>
        </div>
      </div>
    </div>

    <!-- Modal -->
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
import { getAllNews } from '../api/newsApi.js';
import DateFormatter from '../utils/DateFormatter';

export default {
  name: 'NewsList',
  data() {
    return {
      newsList: [],
      selectedNews: null,
    };
  },
  async created() {
    this.newsList = await getAllNews();
    // Format validity dates for the list
    this.newsList = this.newsList.map(news => {
      if (news.validityDate) {
        const formatter = new DateFormatter(news.validityDate);
        return {
          ...news,
          formattedValidityDate: formatter.getFormattedDate(),
        };
      }
      return news;
    });
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
    getImageUrl(imagePath) {
      return `http://localhost:8080/uploads/images/${imagePath.split('/').pop()}`;
    },
  },
};
</script>

<style scoped>
.news-list {
  width: 90%;
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background: #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.news-list h1 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 3em;
  color: #2c3e50;
  font-family: 'Roboto', sans-serif;
  position: relative;
}

.news-list h1::after {
  content: '';
  width: 60px;
  height: 4px;
  background: #3498db;
  display: block;
  margin: 10px auto 0;
  border-radius: 2px;
}

.news-item {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 10px;
  transition: background 0.3s, transform 0.3s;
  cursor: pointer;
}

.news-item:hover {
  background: #f0f8ff;
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
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
  font-family: 'Roboto', sans-serif;
  transition: color 0.3s;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.news-content h2:hover {
  color: #3498db;
}

.news-content p {
  text-align: left;
  font-size: 1.1em;
  line-height: 1.6;
  color: #555;
  font-family: 'Open Sans', sans-serif;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.validity-date {
  font-size: 1.3em;
  color: #888;
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.validity-date i {
  margin-right: 8px;
  color: #e74c3c;
}

.news-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.news-actions button,
.news-actions a {
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
  transition: background-color 0.3s, transform 0.3s;
}

.news-actions button:hover {
  background-color: #2980b9;
  transform: scale(1.05);
}

.news-actions a {
  background-color: #f39c12;
  color: #fff;
  transition: background-color 0.3s, transform 0.3s;
}

.news-actions a:hover {
  background-color: #e67e22;
  transform: scale(1.05);
}

/* Modal Stilleri */
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
  z-index: 2000;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 10px;
  width: 80%;
  max-width: 800px;
  word-break: break-word;
  white-space: normal;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.modal-content h2 {
  margin-top: 0;
  font-size: 2em;
  color: #2c3e50;
  font-family: 'Roboto', sans-serif;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.modal-content p {
  margin-bottom: 20px;
  font-size: 1.2em;
  line-height: 1.6;
  color: #555;
  font-family: 'Open Sans', sans-serif;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.modal-content .validity-date {
  font-size: 1.3em;
  color: #888;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content .validity-date i {
  margin-right: 8px;
  color: #e74c3c;
}

.modal-content button {
  padding: 10px 20px;
  font-size: 1em;
  background-color: #3498db;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
  font-family: 'Open Sans', sans-serif;
}

.modal-content button:hover {
  background-color: #2980b9;
  transform: scale(1.05);
}


@media (max-width: 768px) {
  .news-content h2,
  .news-content p {
    text-align: center;
  }

  .modal-content {
    width: 90%;
  }
}
</style>
