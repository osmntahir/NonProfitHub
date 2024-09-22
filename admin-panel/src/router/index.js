// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import ManageNews from '../components/ManageNews.vue';
import ManageAnnouncements from '../components/ManageAnnouncements.vue';
import NewsDetail from '../components/NewsDetail.vue';
import Login from '../components/Login.vue';
import { useAuthStore } from '@/stores/auth';

const routes = [
    {
        path: '/news',
        component: ManageNews,
        meta: { requiresAuth: true, roles: ['ADMIN'] }
    },
    {
        path: '/news/:id',
        component: NewsDetail,
        meta: { requiresAuth: true, roles: ['ADMIN'] }
    },
    {
        path: '/announcements',
        component: ManageAnnouncements,
        meta: { requiresAuth: true, roles: ['ADMIN'] }
    },
    { path: '/login', component: Login },
    { path: '/', redirect: '/news' },
    // Diğer rotalar
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// Global Route Guard
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    if (to.meta.requiresAuth) {
        if (!authStore.isAuthenticated) {
            authStore.clearToken(); // Token süresi dolmuşsa temizle
            next({ path: '/login', query: { redirect: to.fullPath } });
        } else if (to.meta.roles && !to.meta.roles.some(role => authStore.userRoles.includes(role))) {
            next({ path: '/' }); // Yetkiniz yoksa ana sayfaya yönlendir
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
