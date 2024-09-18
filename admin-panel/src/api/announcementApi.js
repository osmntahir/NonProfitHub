import axios from 'axios';

const API_URL = 'http://localhost:8080/api/announcement';

export const getAllAnnouncements = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error('Error fetching announcements:', error);
        throw error;
    }
};

export const addAnnouncement = async (formData) => {
    try {
        const response = await axios.post(API_URL, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error adding announcement:', error);
        throw error;
    }
};

export const updateAnnouncement = async (id, formData) => {
    try {
        const response = await axios.put(`${API_URL}/${id}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    } catch (error) {
        console.error(`Error updating announcement with id ${id}:`, error);
        throw error;
    }
};

export const deleteAnnouncement = async (id) => {
    try {
        const response = await axios.delete(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error deleting announcement with id ${id}:`, error);
        throw error;
    }
};
