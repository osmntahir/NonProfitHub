<!-- src/views/announcements/AnnouncementList.vue -->
<template>
  <div class="announcement-list">
    <h2>Duyurular</h2>
    <router-link to="/admin/announcements/create" class="create-button">Yeni Duyuru Ekle</router-link>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Konu</th>
        <th>İçerik</th>
        <th>Geçerlilik Tarihi</th>
        <th>Resim</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="announcement in announcementList" :key="announcement.id">
        <td>{{ announcement.id }}</td>
        <td>{{ announcement.konu }}</td>
        <td>{{ announcement.icerik }}</td>
        <td>{{ formatDate(announcement.gecerlilikTarihi) }}</td>
        <td>
          <img :src="`http://localhost:8080/uploads/images/${announcement.resim}`" alt="Resim" width="100" />
        </td>
        <td>
          <router-link :to="`/admin/announcements/edit/${announcement.id}`">Düzenle</router-link>
          <button @click="deleteAnnouncement(announcement.id)">Sil</button>
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
  name: 'AnnouncementList',
  setup() {
    const announcementList = ref([]);

    const fetchAnnouncements = async () => {
      try {
        const response = await api.get('/announcement');
        announcementList.value = response.data;
      } catch (error) {
        console.error('Duyuruları çekerken hata:', error);
      }
    };

    const deleteAnnouncement = async (id) => {
      if (confirm('Bu duyuruyu silmek istediğinizden emin misiniz?')) {
        try {
          await api.delete(`/announcement/${id}`);
          announcementList.value = announcementList.value.filter(a => a.id !== id);
        } catch (error) {
          console.error('Duyuru silinirken hata:', error);
        }
      }
    };

    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return date.toLocaleDateString();
    };

    onMounted(() => {
      fetchAnnouncements();
    });

    return {
      announcementList,
      deleteAnnouncement,
      formatDate,
    };
  },
};
</script>

<style scoped>
.announcement-list {
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

img {
  border-radius: 4px;
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
