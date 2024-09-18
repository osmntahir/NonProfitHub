import { createRouter, createWebHistory } from 'vue-router';
import ManageNews from './components/ManageNews.vue';
import ManageAnnouncements from './components/ManageAnnouncements.vue';

const routes = [
    { path: '/news', component: ManageNews },
    { path: '/announcements', component: ManageAnnouncements },
    { path: '/', redirect: '/news' }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
