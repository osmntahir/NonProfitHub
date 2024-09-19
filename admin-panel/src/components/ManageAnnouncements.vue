<template>
  <div>
    <h1>Duyurular</h1>
    <button @click="openAddAnnouncementPopup" class="add-button">Duyuru Ekle</button>

    <table>
      <thead>
      <tr>
        <th>Konu</th>
        <th>İçerik</th>
        <th>Geçerlilik Tarihi</th>
        <th>Resim</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="announcement in announcements" :key="announcement.id">
        <td>{{ announcement.subject }}</td>
        <td>{{ announcement.content }}</td>
        <td>{{ announcement.validityDate }}</td>
        <td>
          <img v-if="announcement.imagePath" :src="getImageUrl(announcement.imagePath)" alt="Duyuru Resmi" class="announcement-image" />
        </td>
        <td>
          <button @click="openUpdatePopup(announcement)" class="update-button">Güncelle</button>
          <button @click="deleteAnnouncement(announcement.id)" class="delete-button">Sil</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Popup for Add/Update Announcement -->
    <div v-if="showPopup" class="popup">
      <div class="popup-content">
        <h2>{{ isUpdate ? 'Duyuru Güncelle' : 'Yeni Duyuru Ekle' }}</h2>
        <div class="form-group">
          <label>Konu</label>
          <input v-model="popupData.subject" placeholder="Konu" class="large-input" />
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
          <input v-model="popupData.validityDate" placeholder="gg.aa.yyyy" type="date" class="small-date" />
          <!-- Show error message for validity date -->
          <div v-if="formErrors.validityDate" class="error-message">{{ formErrors.validityDate }}</div>
        </div>
        <div class="form-group">
          <label>Resim Yükle</label>
          <input type="file" @change="handleFileUpload" />
        </div>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <div class="form-actions">
          <button @click="saveAnnouncement" class="save-button">{{ isUpdate ? 'Güncelle' : 'Ekle' }}</button>
          <button @click="closePopup" class="cancel-button">İptal</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllAnnouncements, addAnnouncement, updateAnnouncement, deleteAnnouncement } from '../api/announcementApi.js';

export default {
  data() {
    return {
      announcements: [],
      showPopup: false,
      isUpdate: false,
      popupData: {
        id: null,
        subject: '',
        content: '',
        validityDate: '',
        image: null
      },
      errorMessage: '',
      formErrors: {
        subject: '',
        content: '',
        validityDate: ''
      }
    };
  },
  mounted() {
    this.loadAnnouncements();
  },
  methods: {
    async loadAnnouncements() {
      try {
        this.announcements = await getAllAnnouncements();
      } catch (error) {
        this.errorMessage = 'Duyurular yüklenirken bir hata oluştu.';
      }
    },
    openAddAnnouncementPopup() {
      this.isUpdate = false;
      this.popupData = { id: null, subject: '', content: '', validityDate: '', image: null };
      this.showPopup = true;
      this.errorMessage = '';
      this.clearFormErrors();
    },
    openUpdatePopup(announcement) {
      this.isUpdate = true;
      this.popupData = { ...announcement, image: null };
      this.showPopup = true;
      this.errorMessage = '';
      this.clearFormErrors();
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.popupData.image = file;
    },
    async saveAnnouncement() {
      this.clearFormErrors();

      if (!this.validateForm()) {
        return;
      }

      const formData = new FormData();
      formData.append('announcement', new Blob([JSON.stringify({
        subject: this.popupData.subject,
        content: this.popupData.content,
        validityDate: this.popupData.validityDate
      })], { type: 'application/json' }));

      if (this.popupData.image) {
        formData.append('image', this.popupData.image);
      }

      try {
        if (this.isUpdate) {
          await updateAnnouncement(this.popupData.id, formData);
        } else {
          await addAnnouncement(formData);
        }
        this.loadAnnouncements();
        this.closePopup();
      } catch (error) {
        this.errorMessage = error.message || 'Duyuru kaydedilirken bir hata oluştu.';
      }
    },
    validateForm() {
      let isValid = true;

      const now = new Date().toISOString().split("T")[0];
      const userSelectedDate = this.popupData.validityDate;

      // Check if subject is empty
      if (!this.popupData.subject) {
        this.formErrors.subject = 'Konu gereklidir';
        isValid = false;
      }

      // Check if content is empty
      if (!this.popupData.content) {
        this.formErrors.content = 'İçerik gereklidir';
        isValid = false;
      }

      // Check if validity date is empty or invalid
      if (!this.popupData.validityDate) {
        this.formErrors.validityDate = 'Geçerlilik Tarihi gereklidir';
        isValid = false;
      } else if (userSelectedDate < now) {
        this.formErrors.validityDate = 'Geçerlilik Tarihi bugun veya gelecekteki bir tarih olmalıdır';
        isValid = false;
      }

      return isValid;
    },
    clearFormErrors() {
      this.formErrors = { subject: '', content: '', validityDate: '' };
    },
    async deleteAnnouncement(id) {
      try {
        await deleteAnnouncement(id);
        this.loadAnnouncements();
      } catch (error) {
        this.errorMessage = `Duyuru silinirken bir hata oluştu: ${error.message}`;
      }
    },
    closePopup() {
      this.showPopup = false;
    },
    getImageUrl(imagePath) {
      return `http://localhost:8080/${imagePath}`;
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
}

.announcement-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
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
  font-family: 'Arial', sans-serif;
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
  font-size: 14px;
}

.form-group input[type="text"],
.form-group input[type="date"],
.form-group input[type="file"],
.form-group textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
  transition: border 0.3s;
}

.form-group input[type="date"] {
  padding: 10px;
  font-size: 16px;
  border: 2px solid #007bff;
  border-radius: 8px;
  background-color: #f9f9f9;
  transition: border-color 0.3s, background-color 0.3s;
}

.form-group input[type="date"]:focus {
  border-color: #0056b3;
  background-color: #e6f0ff;
  outline: none;
}

.form-group textarea {
  height: 150px;
  resize: vertical;
}

.form-group input[type="text"]:focus,
.form-group textarea:focus,
.form-group input[type="file"]:focus,
.form-group textarea:focus {
  border-color: #007bff;
  outline: none;
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

input[type="file"] {
  padding: 10px;
  font-size: 16px;
  cursor: pointer;
}
</style>
