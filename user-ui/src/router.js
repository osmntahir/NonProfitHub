// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import Home from './components/Home.vue';
import NewsList from './components/NewsList.vue';
import NewsDetail from './components/NewsDetail.vue';
import AnnouncementsList from './components/AnnouncementsList.vue';
import NotFound from './components/NotFound.vue';

const routes = [
    { path: '/', component: Home },
    { path: '/news', component: NewsList },
    { path: '/news/:id', component: NewsDetail, props: true },
    { path: '/announcements', component: AnnouncementsList },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound }, // 404 Rota
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
