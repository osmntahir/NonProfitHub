<!-- src/views/announcements/AnnouncementForm.vue -->
<template>
  <div class="announcement-form">
    <h2>{{ isEdit ? 'Duyuru Düzenle' : 'Yeni Duyuru Ekle' }}</h2>
    <form @submit.prevent="handleSubmit" enctype="multipart/form-data">
      <div>
        <label for="konu">Konu:</label>
        <input v-model="announcement.konu" type="text" id="konu" required />
      </div>
      <div>
        <label for="icerik">İçerik:</label>
        <textarea v-model="announcement.icerik" id="icerik" required></textarea>
      </div>
      <div>
        <label for="gecerlilikTarihi">Geçerlilik Tarihi:</label>
        <input v-model="announcement.gecerlilikTarihi" type="date" id="gecerlilikTarihi" required />
      </div>
      <div>
        <label for="image">Resim:</label>
        <input @change="handleFileChange" type="file" id="image" accept="image/*" :required="!isEdit" />
        <div v-if="existingImage">
          <p>Mevcut Resim:</p>
          <img :src="`http://localhost:8080/uploads/images/${existingImage}`" alt="Resim" width="100" />
        </div>
      </div>
      <button type="submit">{{ isEdit ? 'Güncelle' : 'Ekle' }}</button>
      <router-link to="/admin/announcements" class="cancel-button">İptal</router-link>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import api from '../../services/api';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  name: 'AnnouncementForm',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const isEdit = ref(false);
    const announcement = ref({
      konu: '',
      icerik: '',
      gecerlilikTarihi: '',
    });
    const imageFile = ref(null);
    const existingImage = ref(null);
    const error = ref('');

    const fetchAnnouncement = async (id) => {
      try {
        const response = await api.get(`/announcement/${id}`);
        announcement.value = {
          konu: response.data.konu,
          icerik: response.data.icerik,
          gecerlilikTarihi: response.data.gecerlilikTarihi.split('T')[0], // YYYY-MM-DD format
        };
        existingImage.value = response.data.resim;
      } catch (err) {
        console.error('Duyuru çekerken hata:', err);
        error.value = 'Duyuru bilgileri alınırken hata oluştu.';
      }
    };

    const handleFileChange = (event) => {
      imageFile.value = event.target.files[0];
    };

    const handleSubmit = async () => {
      try {
        const formData = new FormData();
        formData.append('announcement', JSON.stringify(announcement.value));
        if (imageFile.value) {
          formData.append('image', imageFile.value);
        }

        if (isEdit.value) {
          await api.put(`/announcement/${route.params.id}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          alert('Duyuru başarıyla güncellendi.');
        } else {
          if (!imageFile.value) {
            alert('Resim seçmeniz gerekmektedir.');
            return;
          }
          await api.post('/announcement', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          alert('Duyuru başarıyla eklendi.');
        }
        router.push('/admin/announcements');
      } catch (err) {
        console.error('Duyuru kaydedilirken hata:', err);
        error.value = 'Duyuru kaydedilirken hata oluştu.';
      }
    };

    onMounted(() => {
      if (route.name === 'EditAnnouncement') {
        isEdit.value = true;
        fetchAnnouncement(route.params.id);
      }
    });

    return {
      isEdit,
      announcement,
      handleSubmit,
      handleFileChange,
      existingImage,
      error,
    };
  },
};
</script>

<style scoped>
.announcement-form {
  padding: 1rem;
}

form div {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
}

input[type="text"],
input[type="date"],
input[type="url"],
textarea,
input[type="file"] {
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

img {
  margin-top: 0.5rem;
  border-radius: 4px;
}
</style>
