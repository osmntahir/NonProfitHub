// src/services/api.js
import axios from 'axios';
import { useAuthStore } from '../store';
import { getActivePinia } from 'pinia';

// Axios istemcisi oluşturun
const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Backend API URL'inizi buraya yazın
});

// İsteklerden önce token'ı ekleyin
api.interceptors.request.use((config) => {
    const pinia = getActivePinia();
    if (pinia) {
        const authStore = useAuthStore(pinia);
        if (authStore.token) {
            config.headers.Authorization = `Bearer ${authStore.token}`;
            console.log('Authorization Header Eklendi:', `Bearer ${authStore.token}`);
        }
    } else {
        console.warn('Active Pinia bulunamadı!');
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// Yanıt hatalarını yakalayın
api.interceptors.response.use((response) => response, (error) => {
    if (error.response && error.response.status === 401) {
        const pinia = getActivePinia();
        if (pinia) {
            const authStore = useAuthStore(pinia);
            authStore.logout();
        }
        window.location.href = '/login';
    }
    return Promise.reject(error);
});

export default api;
