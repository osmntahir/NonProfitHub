import axios from 'axios';

const API_URL = 'http://localhost:8080/api/news';

export const getAllNews = async () => {
    try {
        const response = await axios.get(`${API_URL}/valid`);
        return response.data;
    } catch (error) {
        console.error('Error fetching news:', error);
        return [];
    }
};

export const getNewsById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error fetching news with id ${id}:`, error);
        return null;
    }
};
