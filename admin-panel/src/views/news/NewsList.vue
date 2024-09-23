<!-- src/views/news/NewsList.vue -->
<template>
  <div class="news-list">
    <h2>Haberler</h2>
    <button class="create-button" @click="openModal('create')">Yeni Haber Ekle</button>
    <div class="table-responsive">
      <table>
        <thead>
        <tr>
          <th class="id-column">ID</th>
          <th>Konu</th>
          <th>İçerik</th>
          <th>Geçerlilik Tarihi</th>
          <th>Haber Linki</th>
          <th>İşlemler</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="news in newsList" :key="news.id">
          <td class="id-column">{{ news.id }}</td>
          <td class="wrap-text">{{ news.subject }}</td>
          <td class="wrap-text">{{ news.content }}</td>
          <td>{{ formatDate(news.validityDate) }}</td>
          <td>
            <a :href="news.newsLink" target="_blank">{{ news.newsLink }}</a>
          </td>
          <td class="actions">
            <button class="edit-button" @click="openModal('edit', news)">Düzenle</button>
            <button class="delete-button" @click="deleteNews(news.id)">Sil</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ modalType === 'create' ? 'Yeni Haber Ekle' : 'Haber Düzenle' }}</h3>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="subject">Konu:</label>
            <input v-model="form.subject" type="text" id="subject" required />
          </div>
          <div class="form-group">
            <label for="content">İçerik:</label>
            <textarea v-model="form.content" id="content" required></textarea>
          </div>
          <div class="form-group">
            <label for="validityDate">Geçerlilik Tarihi:</label>
            <input
                v-model="form.validityDate"
                type="date"
                id="validityDate"
                :min="todayDate"
                required
            />
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-button">
              {{ modalType === 'create' ? 'Ekle' : 'Güncelle' }}
            </button>
            <button type="button" class="cancel-button" @click="closeModal">İptal</button>
          </div>
          <p v-if="error" class="error">{{ error }}</p>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../../services/api';
import { ref, onMounted } from 'vue';

export default {
  name: 'NewsList',
  setup() {
    const newsList = ref([]);
    const isModalOpen = ref(false);
    const modalType = ref('create'); // 'create' veya 'edit'
    const form = ref({
      id: null,
      subject: '',
      content: '',
      validityDate: '',
    });
    const error = ref('');
    const todayDate = ref(new Date().toISOString().split('T')[0]);

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

    const openModal = (type, news = null) => {
      modalType.value = type;
      if (type === 'edit' && news) {
        form.value = { ...news };
      } else {
        form.value = {
          id: null,
          subject: '',
          content: '',
          validityDate: '',
        };
      }
      error.value = '';
      isModalOpen.value = true;
    };

    const closeModal = () => {
      isModalOpen.value = false;
    };

    const submitForm = async () => {
      try {
        if (modalType.value === 'create') {
          const response = await api.post('/news', form.value);
          newsList.value.push(response.data);
          alert('Haber başarıyla eklendi.');
        } else if (modalType.value === 'edit') {
          const response = await api.put(`/news/${form.value.id}`, form.value);
          const index = newsList.value.findIndex(news => news.id === form.value.id);
          if (index !== -1) {
            newsList.value[index] = response.data;
          }
          alert('Haber başarıyla güncellendi.');
        }
        closeModal();
      } catch (err) {
        console.error('Haber kaydedilirken hata:', err);
        error.value = 'Haber kaydedilirken hata oluştu.';
      }
    };

    onMounted(() => {
      fetchNews();
    });

    return {
      newsList,
      deleteNews,
      formatDate,
      isModalOpen,
      modalType,
      form,
      openModal,
      closeModal,
      submitForm,
      error,
      todayDate,
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
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.create-button:hover {
  background-color: #218838;
}

.table-responsive {
  overflow-x: auto; /* Yatay kaydırma ekler */
}

table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed; /* Sabit tablo düzeni */
  min-width: 600px; /* Tablo genişliğini minimum 600px olarak ayarlar */
}

thead {
  background-color: #f8f9fa;
}

th, td {
  padding: 0.5rem; /* Padding'i azaltır */
  border: 1px solid #dee2e6;
  text-align: left;
  vertical-align: top;
  overflow: hidden;
}

.id-column {
  width: 40px; /* ID sütunu için daha dar bir genişlik */
  text-align: center;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.wrap-text {
  white-space: normal;
  word-wrap: break-word;
  word-break: break-word;
}

.actions {
  white-space: nowrap;
}

.edit-button {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  background-color: #007bff; /* Mavi renk */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
  margin-right: 0.5rem;
}

.edit-button:hover {
  background-color: #0056b3; /* Hover efekti için koyu mavi */
}

.delete-button {
  padding: 0.25rem 0.5rem;
  background-color: #dc3545; /* Kırmızı renk */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.delete-button:hover {
  background-color: #a71d2a; /* Hover efekti için koyu kırmızı */
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
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 1.5rem;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.modal-content h3 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.submit-button {
  padding: 0.5rem 1rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #218838;
}

.cancel-button {
  padding: 0.5rem 1rem;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cancel-button:hover {
  background-color: #5a6268;
}

.error {
  color: red;
  margin-top: 0.5rem;
  font-size: 0.9rem;
}

/* Hover efekti ekleme */
tr:hover {
  background-color: #f1f1f1;
}
</style>
