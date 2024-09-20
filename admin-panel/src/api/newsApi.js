import axios from 'axios';

const API_URL = 'http://localhost:8080/api/news'; // Backend'deki news endpoint

export const getAllNews = async () => {
    try {
        const response = await axios.get(API_URL);
        console.log("response data " , response.data);
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
