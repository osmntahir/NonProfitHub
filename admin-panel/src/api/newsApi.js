// src/api/newsApi.js
import axios from '@/plugins/axios';

const API_URL = '/news'; // Axios instance'ı baseURL olarak ayarlandı.

export const getAllNews = async () => {
    try {
        const response = await axios.get(API_URL);
        console.log("response data:", response.data);
        return response.data;
    } catch (error) {
        console.error('Error fetching news:', error);
        throw error;
    }
};

export const addNews = async (newsData) => {
    try {
        const response = await axios.post(API_URL, newsData, {
            headers: {
                'Content-Type': 'application/json',
            },
        });
        return response.data;
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data.message || JSON.stringify(error.response.data));
        }
        throw error;
    }
};

export const updateNews = async (id, newsData) => {
    try {
        const response = await axios.put(`${API_URL}/${id}`, newsData, {
            headers: {
                'Content-Type': 'application/json',
            },
        });
        return response.data;
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data.message || JSON.stringify(error.response.data));
        }
        throw error;
    }
};

export const deleteNews = async (id) => {
    try {
        const response = await axios.delete(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error deleting news with id ${id}:`, error);
        throw error;
    }
};

export const getNewsById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching news:', error);
        throw error;
    }
};
