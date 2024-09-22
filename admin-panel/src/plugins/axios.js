// src/plugins/axios.js
import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const getAuthStore = () => {
    const authStore = useAuthStore();
    return authStore;
};

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api', // Backend API URL'inizi buraya ekleyin
});

// Request interceptor to add the token to headers
axiosInstance.interceptors.request.use(
    (config) => {
        const authStore = getAuthStore();
        if (authStore.token) {
            config.headers.Authorization = `Bearer ${authStore.token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default axiosInstance;
