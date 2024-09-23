<!-- src/views/announcements/AnnouncementList.vue -->
<template>
  <div class="announcement-list">
    <h2>Duyurular</h2>
    <button class="create-button" @click="openModal('create')">Yeni Duyuru Ekle</button>
    <div class="table-responsive">
      <table>
        <thead>
        <tr>
          <th class="id-column">ID</th>
          <th>Konu</th>
          <th>İçerik</th>
          <th>Geçerlilik Tarihi</th>
          <th>Resim</th>
          <th>İşlemler</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="announcement in announcementList" :key="announcement.id">
          <td class="id-column">{{ announcement.id }}</td>
          <td class="wrap-text">{{ announcement.konu }}</td>
          <td class="wrap-text">{{ announcement.icerik }}</td>
          <td>{{ formatDate(announcement.gecerlilikTarihi) }}</td>
          <td>
            <img
                v-if="announcement.imagePath"
                :src="`http://localhost:8080/uploads/images/${announcement.imagePath}`"
                alt="Resim"
                width="100"
            />
            <span v-else>Yok</span>
          </td>
          <td class="actions">
            <button class="edit-button" @click="openModal('edit', announcement)">Düzenle</button>
            <button class="delete-button" @click="deleteAnnouncement(announcement.id)">Sil</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ modalType === 'create' ? 'Yeni Duyuru Ekle' : 'Duyuru Düzenle' }}</h3>
        <form @submit.prevent="submitForm" enctype="multipart/form-data">
          <div class="form-group">
            <label for="konu">Konu:</label>
            <input v-model="form.konu" type="text" id="konu" required />
          </div>
          <div class="form-group">
            <label for="icerik">İçerik:</label>
            <textarea v-model="form.icerik" id="icerik" required></textarea>
          </div>
          <div class="form-group">
            <label for="gecerlilikTarihi">Geçerlilik Tarihi:</label>
            <input
                v-model="form.gecerlilikTarihi"
                type="date"
                id="gecerlilikTarihi"
                :min="todayDate"
                required
            />
          </div>
          <div class="form-group">
            <label for="image">Resim:</label>
            <input
                @change="handleFileChange"
                type="file"
                id="image"
                accept="image/*"
                :required="false"
            />
            <div v-if="existingImage">
              <p>Mevcut Resim:</p>
              <img
                  :src="`http://localhost:8080/uploads/images/${existingImage}`"
                  alt="Resim"
                  width="100"
              />
            </div>
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
  name: 'AnnouncementList',
  setup() {
    const announcementList = ref([]);
    const isModalOpen = ref(false);
    const modalType = ref('create'); // 'create' veya 'edit'
    const form = ref({
      id: null,
      konu: '',
      icerik: '',
      gecerlilikTarihi: '',
    });
    const imageFile = ref(null);
    const existingImage = ref(null);
    const error = ref('');
    const todayDate = ref(new Date().toISOString().split('T')[0]);

    const fetchAnnouncements = async () => {
      try {
        const response = await api.get('/announcement');
        announcementList.value = response.data;
      } catch (err) {
        console.error('Duyuruları çekerken hata:', err);
      }
    };

    const deleteAnnouncement = async (id) => {
      if (confirm('Bu duyuruyu silmek istediğinizden emin misiniz?')) {
        try {
          await api.delete(`/announcement/${id}`);
          announcementList.value = announcementList.value.filter(a => a.id !== id);
        } catch (err) {
          console.error('Duyuru silinirken hata:', err);
        }
      }
    };

    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return date.toLocaleDateString();
    };

    const openModal = (type, announcement = null) => {
      modalType.value = type;
      if (type === 'edit' && announcement) {
        form.value = {
          id: announcement.id,
          konu: announcement.konu,
          icerik: announcement.icerik,
          gecerlilikTarihi: announcement.gecerlilikTarihi.split('T')[0], // YYYY-MM-DD format
        };
        existingImage.value = announcement.imagePath;
      } else {
        form.value = {
          id: null,
          konu: '',
          icerik: '',
          gecerlilikTarihi: '',
        };
        existingImage.value = null;
      }
      imageFile.value = null;
      error.value = '';
      isModalOpen.value = true;
    };

    const closeModal = () => {
      isModalOpen.value = false;
    };

    const handleFileChange = (event) => {
      imageFile.value = event.target.files[0];
    };

    const submitForm = async () => {
      try {
        const formData = new FormData();
        formData.append('announcement', JSON.stringify({
          konu: form.value.konu,
          icerik: form.value.icerik,
          gecerlilikTarihi: form.value.gecerlilikTarihi,
        }));
        if (imageFile.value) {
          formData.append('image', imageFile.value);
        }

        if (modalType.value === 'create') {
          const response = await api.post('/announcement', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          announcementList.value.push(response.data);
          alert('Duyuru başarıyla eklendi.');
        } else if (modalType.value === 'edit') {
          const response = await api.put(`/announcement/${form.value.id}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          const index = announcementList.value.findIndex(a => a.id === form.value.id);
          if (index !== -1) {
            announcementList.value[index] = response.data;
          }
          alert('Duyuru başarıyla güncellendi.');
        }
        closeModal();
      } catch (err) {
        console.error('Duyuru kaydedilirken hata:', err);
        error.value = 'Duyuru kaydedilirken hata oluştu.';
      }
    };

    onMounted(() => {
      fetchAnnouncements();
    });

    return {
      announcementList,
      deleteAnnouncement,
      formatDate,
      isModalOpen,
      modalType,
      form,
      openModal,
      closeModal,
      submitForm,
      handleFileChange,
      existingImage,
      error,
      todayDate,
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
  min-width: 700px; /* Tablo genişliğini minimum 700px olarak ayarlar */
}

thead {
  background-color: #f8f9fa;
}

th,
td {
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

img {
  margin-top: 0.5rem;
  border-radius: 4px;
}
</style>
