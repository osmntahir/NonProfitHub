// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import AdminDashboard from '../views/AdminDashboard.vue';
import NotFound from '../views/NotFound.vue';
import { useAuthStore } from '../store';

// Haberler (News) Yönetimi
import NewsList from '../views/news/NewsList.vue';
import NewsForm from '../views/news/NewsForm.vue';

// Duyurular (Announcements) Yönetimi
import AnnouncementList from '../views/announcements/AnnouncementList.vue';
import AnnouncementForm from '../views/announcements/AnnouncementForm.vue';

const routes = [
    {
        path: '/',
        name: 'Home',
        redirect: '/admin',
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
    },
    {
        path: '/admin',
        name: 'AdminDashboard',
        component: AdminDashboard,
        meta: { requiresAuth: true, roles: ['ADMIN'] },
        children: [
            {
                path: 'news',
                name: 'NewsList',
                component: NewsList,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
            {
                path: 'news/create',
                name: 'CreateNews',
                component: NewsForm,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
            {
                path: 'news/edit/:id',
                name: 'EditNews',
                component: NewsForm,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
            {
                path: 'announcements',
                name: 'AnnouncementList',
                component: AnnouncementList,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
            {
                path: 'announcements/create',
                name: 'CreateAnnouncement',
                component: AnnouncementForm,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
            {
                path: 'announcements/edit/:id',
                name: 'EditAnnouncement',
                component: AnnouncementForm,
                meta: { requiresAuth: true, roles: ['ADMIN'] },
            },
        ],
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// Route Guard
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    authStore.initialize();

    if (to.meta.requiresAuth) {
        if (!authStore.isAuthenticated) {
            next({ name: 'Login' });
        } else if (to.meta.roles && !to.meta.roles.some(role => authStore.userRoles.includes(role))) {
            next({ name: 'NotFound' });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
