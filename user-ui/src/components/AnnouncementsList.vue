<template>
  <div class="announcements-list">
    <h1>Duyurular</h1>
    <div v-for="announcement in announcementsList" :key="announcement.id" class="announcement-item">
      <img v-if="announcement.imagePath" :src="getImageUrl(announcement.imagePath)" alt="Duyuru Resmi" class="announcement-image" />
      <div class="announcement-content" :class="{ 'no-image': !announcement.imagePath }">
        <h2>{{ announcement.subject }}</h2>
        <p>{{ announcement.content }}</p>
        <p class="validity-date">
          <i class="fas fa-calendar-alt"></i>
          {{ formattedValidityDate(announcement.validityDate) }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllAnnouncements } from '../api/announcementsApi.js';
import DateFormatter from '../utils/DateFormatter';

export default {
  data() {
    return {
      announcementsList: [],
    };
  },
  async created() {
    this.announcementsList = await getAllAnnouncements();
  },
  methods: {
    getImageUrl(imagePath) {
      return `http://localhost:8080/${imagePath}`;
    },
    formattedValidityDate(date) {
      const dateFormatter = new DateFormatter(date);
      return dateFormatter.getFormattedDate();
    },
  },
};
</script>

<style scoped>
.announcements-list {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.announcements-list h1 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 2.5em;
  color: #333;
}

.announcement-item {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  margin-bottom: 40px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 20px;
}

.announcement-image {
  width: 100%;
  max-width: 300px;
  height: auto;
  margin-right: 20px;
  border-radius: 10px;
  object-fit: cover;
}

.announcement-content {
  flex: 1;
  min-width: 0;
}

.announcement-content.no-image {
  margin-left: 0;
}

.announcement-content h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.8em;
  color: #2c3e50;
  word-wrap: break-word;
}

.announcement-content p {
  text-align: left;
  font-size: 1.1em;
  line-height: 1.6;
  color: #555;
}

.validity-date {
  font-style: italic;
  color: #888;
  margin-top: 15px;
  display: flex;
  align-items: center;
}

.validity-date i {
  margin-right: 8px;
  color: #3498db;
}
</style>