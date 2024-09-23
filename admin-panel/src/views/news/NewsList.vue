<!-- src/views/news/NewsList.vue -->
<template>
  <div class="news-list">
    <h2>Haberler</h2>
    <router-link to="/admin/news/create" class="create-button">Yeni Haber Ekle</router-link>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Konu</th>
        <th>İçerik</th>
        <th>Geçerlilik Tarihi</th>
        <th>Haber Linki</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="news in newsList" :key="news.id">
        <td>{{ news.id }}</td>
        <td>{{ news.konu }}</td>
        <td>{{ news.icerik }}</td>
        <td>{{ formatDate(news.gecerlilikTarihi) }}</td>
        <td><a :href="news.haberLinki" target="_blank">Link</a></td>
        <td>
          <router-link :to="`/admin/news/edit/${news.id}`">Düzenle</router-link>
          <button @click="deleteNews(news.id)">Sil</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import api from '../../services/api';
import { ref, onMounted } from 'vue';

export default {
  name: 'NewsList',
  setup() {
    const newsList = ref([]);

    const fetchNews = async () => {
      try {
        const response = await api.get('/news');
        newsList.value = response.data;
      } catch (error) {
        console.error('Haberleri çekerken hata:', error);
      }
    };

    const deleteNews = async (id) => {
      if (confirm('Bu haberi silmek istediğinizden emin misiniz?')) {
        try {
          await api.delete(`/news/${id}`);
          newsList.value = newsList.value.filter(news => news.id !== id);
        } catch (error) {
          console.error('Haber silinirken hata:', error);
        }
      }
    };

    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return date.toLocaleDateString();
    };

    onMounted(() => {
      fetchNews();
    });

    return {
      newsList,
      deleteNews,
      formatDate,
    };
  },
};
</script>

<style scoped>
.news-list {
  padding: 1rem;
}

.create-button {
  display: inline-block;
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  background-color: #28a745;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #f8f9fa;
}

th, td {
  padding: 0.75rem;
  border: 1px solid #dee2e6;
  text-align: left;
}

button {
  margin-left: 0.5rem;
  padding: 0.25rem 0.5rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
