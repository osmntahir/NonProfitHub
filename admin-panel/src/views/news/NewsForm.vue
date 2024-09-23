<!-- src/views/news/NewsForm.vue -->
<template>
  <div class="news-form">
    <h2>{{ isEdit ? 'Haber Düzenle' : 'Yeni Haber Ekle' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div>
        <label for="konu">Konu:</label>
        <input v-model="news.konu" type="text" id="konu" required />
      </div>
      <div>
        <label for="icerik">İçerik:</label>
        <textarea v-model="news.icerik" id="icerik" required></textarea>
      </div>
      <div>
        <label for="gecerlilikTarihi">Geçerlilik Tarihi:</label>
        <input v-model="news.gecerlilikTarihi" type="date" id="gecerlilikTarihi" required />
      </div>
      <div>
        <label for="haberLinki">Haber Linki:</label>
        <input v-model="news.haberLinki" type="url" id="haberLinki" required />
      </div>
      <button type="submit">{{ isEdit ? 'Güncelle' : 'Ekle' }}</button>
      <router-link to="/admin/news" class="cancel-button">İptal</router-link>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import api from '../../services/api';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  name: 'NewsForm',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const isEdit = ref(false);
    const news = ref({
      konu: '',
      icerik: '',
      gecerlilikTarihi: '',
      haberLinki: '',
    });
    const error = ref('');

    const fetchNews = async (id) => {
      try {
        const response = await api.get(`/news/${id}`);
        news.value = {
          konu: response.data.konu,
          icerik: response.data.icerik,
          gecerlilikTarihi: response.data.gecerlilikTarihi.split('T')[0], // YYYY-MM-DD format
          haberLinki: response.data.haberLinki,
        };
      } catch (err) {
        console.error('Haber çekerken hata:', err);
        error.value = 'Haber bilgileri alınırken hata oluştu.';
      }
    };

    const handleSubmit = async () => {
      try {
        if (isEdit.value) {
          await api.put(`/news/${route.params.id}`, news.value);
          alert('Haber başarıyla güncellendi.');
        } else {
          await api.post('/news', news.value);
          alert('Haber başarıyla eklendi.');
        }
        router.push('/admin/news');
      } catch (err) {
        console.error('Haber kaydedilirken hata:', err);
        error.value = 'Haber kaydedilirken hata oluştu.';
      }
    };

    onMounted(() => {
      if (route.name === 'EditNews') {
        isEdit.value = true;
        fetchNews(route.params.id);
      }
    });

    return {
      isEdit,
      news,
      handleSubmit,
      error,
    };
  },
};
</script>

<style scoped>
.news-form {
  padding: 1rem;
}

form div {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
}

input, textarea {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
}

button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button {
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  background-color: #6c757d;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>
