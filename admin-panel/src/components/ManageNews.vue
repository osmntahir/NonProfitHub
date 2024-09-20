<template>
  <div>
    <h1>Haberler</h1>
    <button @click="openAddNewsPopup" class="add-button">Haber Ekle</button>

    <table>
      <thead>
      <tr>
        <th>Konu</th>
        <th>İçerik</th>
        <th>Geçerlilik Tarihi</th>
        <th>Haber Linki</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="news in newsList" :key="news.id">
        <td>{{ news.subject }}</td>
        <td>{{ news.content }}</td>
        <td>{{ news.validityDate }}</td>
        <td><a :href="news.newsLink" target="_blank">{{ news.newsLink }}</a></td>
        <td>
          <button @click="openUpdatePopup(news)" class="update-button">Güncelle</button>
          <button @click="deleteNews(news.id)" class="delete-button">Sil</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Popup for Add/Update News -->
    <div v-if="showPopup" class="popup">
      <div class="popup-content">
        <h2>{{ isUpdate ? 'Haber Güncelle' : 'Yeni Haber Ekle' }}</h2>
        <div class="form-group">
          <label>Konu</label>
          <input v-model="popupData.subject" placeholder="Konu" />
          <!-- Show error message for subject -->
          <div v-if="formErrors.subject" class="error-message">{{ formErrors.subject }}</div>
        </div>
        <div class="form-group">
          <label>İçerik</label>
          <textarea v-model="popupData.content" placeholder="İçerik" rows="5"></textarea>
          <!-- Show error message for content -->
          <div v-if="formErrors.content" class="error-message">{{ formErrors.content }}</div>
        </div>
        <div class="form-group">
          <label>Geçerlilik Tarihi</label>
          <input v-model="popupData.validityDate" placeholder="gg.aa.yyyy" type="date" />
          <!-- Show error message for validity date -->
          <div v-if="formErrors.validityDate" class="error-message">{{ formErrors.validityDate }}</div>
        </div>
        <div class="form-group">
          <label>Haber Linki</label>
          <input v-model="popupData.newsLink" placeholder="Haber Linki" />
          <!-- Show error message for newsLink -->
          <div v-if="formErrors.newsLink" class="error-message">{{ formErrors.newsLink }}</div>
        </div>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <div class="form-actions">
          <button @click="saveNews" class="save-button">{{ isUpdate ? 'Güncelle' : 'Ekle' }}</button>
          <button @click="closePopup" class="cancel-button">İptal</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllNews, addNews, updateNews, deleteNews } from '../api/newsApi.js';

export default {
  data() {
    return {
      newsList: [],
      showPopup: false,
      isUpdate: false,
      popupData: {
        id: null,
        subject: '',
        content: '',
        validityDate: '',
        newsLink: ''
      },
      errorMessage: '',
      formErrors: {
        subject: '',
        content: '',
        validityDate: '',
        newsLink: ''
      }
    };
  },
  mounted() {
    this.loadNews();
  },
  methods: {
    async loadNews() {
      try {
        this.newsList = await getAllNews();
      } catch (error) {
        this.errorMessage = 'Haberler yüklenirken bir hata oluştu.';
      }
    },
    openAddNewsPopup() {
      this.isUpdate = false;
      this.popupData = { id: null, subject: '', content: '', validityDate: '', newsLink: '' };
      this.showPopup = true;
      this.errorMessage = '';
      this.clearFormErrors();
    },
    openUpdatePopup(news) {
      this.isUpdate = true;
      this.popupData = { ...news };
      this.showPopup = true;
      this.errorMessage = '';
      this.clearFormErrors();
    },
    async saveNews() {
      this.clearFormErrors();

      if (!this.validateForm()) {
        return;
      }

      const newsData = {
        subject: this.popupData.subject,
        content: this.popupData.content,
        validityDate: this.popupData.validityDate,
        newsLink: this.popupData.newsLink
      };

      try {
        if (this.isUpdate) {
          await updateNews(this.popupData.id, newsData);
        } else {
          await addNews(newsData);
        }
        this.loadNews();
        this.closePopup();
      } catch (error) {
        this.errorMessage = error.message || 'Haber kaydedilirken bir hata oluştu.';
      }
    },
    validateForm() {
      let isValid = true;

      const now = new Date().toISOString().split("T")[0];
      const userSelectedDate = this.popupData.validityDate;

      if (!this.popupData.subject) {
        this.formErrors.subject = 'Konu gereklidir';
        isValid = false;
      }

      if (!this.popupData.content) {
        this.formErrors.content = 'İçerik gereklidir';
        isValid = false;
      }

      if (!this.popupData.validityDate) {
        this.formErrors.validityDate = 'Geçerlilik Tarihi gereklidir';
        isValid = false;
      } else if (userSelectedDate < now) {
        this.formErrors.validityDate = 'Geçerlilik Tarihi bugun veya gelecekteki bir tarih olmalıdır';
        isValid = false;
      }

      if (!this.popupData.newsLink) {
        this.formErrors.newsLink = 'Haber linki gereklidir';
        isValid = false;
      }

      return isValid;
    },
    clearFormErrors() {
      this.formErrors = { subject: '', content: '', validityDate: '', newsLink: '' };
    },
    async deleteNews(id) {
      try {
        await deleteNews(id);
        this.loadNews();
      } catch (error) {
        this.errorMessage = `Haber silinirken bir hata oluştu: ${error.message}`;
      }
    },
    closePopup() {
      this.showPopup = false;
    }
  }
};
</script>

<style scoped>
.add-button {
  background-color: green;
  color: white;
  padding: 10px;
  margin-bottom: 10px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-button:hover {
  background-color: darkgreen;
}

table {
  width: 100%;
  border-collapse: collapse;
}

table th, table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
  word-wrap: break-word;
  max-width: 200px;
  white-space: normal;
}

.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.popup-content {
  background: white;
  padding: 30px;
  width: 450px;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-group input[type="text"],
.form-group input[type="date"],
.form-group textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
}

.form-group textarea {
  height: 150px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 25px;
}

.form-actions button {
  padding: 12px 25px;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.form-actions button.save-button {
  background-color: #28a745;
  color: white;
}

.form-actions button.save-button:hover {
  background-color: #218838;
}

.form-actions button.cancel-button {
  background-color: #dc3545;
  color: white;
}

.form-actions button.cancel-button:hover {
  background-color: #c82333;
}

.error-message {
  color: red;
  font-size: 13px;
  margin-top: 5px;
}
</style>
