<template>
  <div class="announcements-list">
    <h1>Duyurular</h1>
    <ul>
      <li v-for="announcement in announcementsList" :key="announcement.id">
        <h2>{{ announcement.subject }}</h2>
        <p>{{ announcement.content }}</p>
        <p>Geçerlilik Tarihi: {{ announcement.validityDate }}</p>
        <img :src="getImageUrl(announcement.id)" alt="Duyuru Resmi" />
      </li>
    </ul>
  </div>
</template>

<script>
import {getAllAnnouncements} from '../api/announcementsApi.js';

export default {
  data() {
    return {
      announcementsList: []
    };
  },
  async created() {
    this.announcementsList = await getAllAnnouncements();
  },
  methods: {
    getImageUrl(id) {
      return `http://localhost:8080/api/announcements/${id}/image`;
    }
  }
};
</script>

<style scoped>
.announcements-list {
  padding: 20px;
}

.announcements-list img {
  max-width: 100%;
  height: auto;
}
</style>
