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
          <button @click="openUpdatePopup(announcement)">Güncelle</button>
          <button @click="deleteAnnouncement(announcement.id)">Sil</button>
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
          <input v-model="popupData.subject" placeholder="Konu" />
        </div>
        <div class="form-group">
          <label>İçerik</label>
          <input v-model="popupData.content" placeholder="İçerik" />
        </div>
        <div class="form-group">
          <label>Geçerlilik Tarihi</label>
          <input v-model="popupData.validityDate" placeholder="gg.aa.yyyy" type="date" />
        </div>
        <div class="form-group">
          <label>Resim Yükle</label>
          <input type="file" @change="handleFileUpload" />
        </div>
        <div class="form-actions">
          <button @click="saveAnnouncement">{{ isUpdate ? 'Güncelle' : 'Ekle' }}</button>
          <button @click="closePopup">İptal</button>
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
        console.error('Error fetching announcements:', error);
      }
    },
    openAddAnnouncementPopup() {
      this.isUpdate = false;
      this.popupData = { id: null, subject: '', content: '', validityDate: '', image: null };
      this.showPopup = true;
    },
    openUpdatePopup(announcement) {
      this.isUpdate = true;
      this.popupData = { ...announcement, image: null }; // Reset image
      this.showPopup = true;
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.popupData.image = file; // Store file object
    },
    async saveAnnouncement() {
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
        console.error('Error saving announcement:', error);
      }
    },
    async deleteAnnouncement(id) {
      try {
        await deleteAnnouncement(id);
        await this.loadAnnouncements();
      } catch (error) {
        console.error('Error deleting announcement:', error);
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
}

table {
  width: 100%;
  border-collapse: collapse;
}

table th, table td {
  border: 1px solid #ddd;
  padding: 10px;
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
}

.popup-content {
  background: white;
  padding: 20px;
  width: 400px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input[type="text"], .form-group input[type="date"], .form-group input[type="file"] {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px
}

.announcement-image {
  width: 100px;
  height: auto;
}
</style>