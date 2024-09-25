<!-- src/components/AnnouncementsList.vue -->
<template>
  <div class="announcements-list">
    <h1>Duyurular</h1>
    <div
        v-for="announcement in announcementsList"
        :key="announcement.id"
        class="announcement-item"
        @mouseover="announcement.hover = true"
        @mouseleave="announcement.hover = false"
    >
      <img
          v-if="announcement.imagePath"
          :src="getImageUrl(announcement.imagePath)"
          alt="Duyuru Resmi"
          class="announcement-image"
      />
      <div
          class="announcement-content"
          :class="{ 'no-image': !announcement.imagePath, 'hovered': announcement.hover }"
      >
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
  name: 'AnnouncementsList',
  data() {
    return {
      announcementsList: [],
    };
  },
  async created() {
    const data = await getAllAnnouncements();

    this.announcementsList = data.map(item => ({ ...item, hover: false }));
  },
  methods: {
    getImageUrl(imagePath) {
      return `http://localhost:8080/uploads/images/${imagePath.split('/').pop()}`;
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
  width: 90%;
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background: #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.announcements-list h1 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 3em;
  color: #2c3e50;
  font-family: 'Roboto', sans-serif;
  position: relative;
}

.announcements-list h1::after {
  content: '';
  width: 60px;
  height: 4px;
  background: #3498db;
  display: block;
  margin: 10px auto 0;
  border-radius: 2px;
}

.announcement-item {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 10px;
  transition: background 0.3s, transform 0.3s;
  cursor: pointer;
}

.announcement-item:hover {
  background: #f0f8ff;
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.announcement-image {
  width: 100%;
  max-width: 300px;
  height: auto;
  margin-right: 20px;
  border-radius: 10px;
  object-fit: cover;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.announcement-image:hover {
  transform: scale(1.05);
}

.announcement-content {
  flex: 1;
  min-width: 0;
  transition: background-color 0.3s;
}

.announcement-content.no-image {
  margin-left: 0;
}

.announcement-content.hovered {
  background-color: #f9f9f9;
  border-radius: 10px;
}

.announcement-content h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.8em;
  color: #2c3e50;
  font-family: 'Roboto', sans-serif;
  transition: color 0.3s;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
}

.announcement-content h2:hover {
  color: #3498db;
}

.announcement-content p {
  text-align: left;
  font-size: 1.1em;
  line-height: 1.6;
  color: #555;
  font-family: 'Open Sans', sans-serif;

  overflow-wrap: break-word;
  word-wrap: break-word;
  hyphens: auto;
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


@media (max-width: 768px) {
  .announcement-content h2,
  .announcement-content p {
    text-align: center;
  }

  .announcement-image {
    max-width: 100%;
    margin-right: 0;
    margin-bottom: 15px;
  }

  .announcement-content.hovered {
    background-color: #f9f9f9;
    border-radius: 10px;
  }
}
</style>
