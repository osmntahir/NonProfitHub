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
          <img :src="announcement.imagePath" alt="Duyuru Resmi" class="announcement-image" v-if="announcement.imagePath" />
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
export default {
  data() {
    return {
      announcements: [
        { id: 1, subject: "Duyuru 1", content: "Duyuru içeriği 1", validityDate: "2024-12-31", imagePath: "http://localhost:8080/images/duyuru1.jpg" },
        { id: 2, subject: "Duyuru 2", content: "Duyuru içeriği 2", validityDate: "2024-11-30", imagePath: "http://localhost:8080/images/duyuru2.jpg" },
      ],
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
  methods: {
    openAddAnnouncementPopup() {
      this.isUpdate = false;
      this.popupData = { id: null, subject: '', content: '', validityDate: '', image: null };
      this.showPopup = true;
    },
    openUpdatePopup(announcement) {
      this.isUpdate = true;
      this.popupData = { ...announcement };
      this.showPopup = true;
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.popupData.image = file; // Dosya objesini sakla
    },
    saveAnnouncement() {
      if (this.isUpdate) {
        const index = this.announcements.findIndex(a => a.id === this.popupData.id);
        this.announcements[index] = { ...this.popupData };
      } else {
        this.popupData.id = this.announcements.length + 1; // Fake ID for now
        this.announcements.push({ ...this.popupData });
      }
      this.closePopup();
    },
    deleteAnnouncement(id) {
      this.announcements = this.announcements.filter(a => a.id !== id);
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
  margin-top: 20px;
}
</style>
