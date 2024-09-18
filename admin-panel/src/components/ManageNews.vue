<template>
  <div>
    <h2>Haberler</h2>
    <button @click="addNews">Ekle</button>
    <ul>
      <li v-for="news in newsList" :key="news.id">
        <span>{{ news.title }}</span>
        <button @click="editNews(news.id)">Güncelle</button>
        <button @click="deleteNews(news.id)">Sil</button>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newsList: [],
    };
  },
  methods: {
    fetchNews() {
      axios.get('/api/haberler')
          .then(response => {
            this.newsList = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the news!', error);
          });
    },
    addNews() {
      // Haber ekleme işlemi
    },
    editNews(id) {
      // Haber güncelleme işlemi
    },
    deleteNews(id) {
      axios.delete(`/api/haberler/${id}`)
          .then(() => {
            this.fetchNews();
          })
          .catch(error => {
            console.error('There was an error deleting the news!', error);
          });
    },
  },
  mounted() {
    this.fetchNews();
  },
};
</script>