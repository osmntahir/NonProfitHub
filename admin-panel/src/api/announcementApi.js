import axios from 'axios';

// API base URL (backend API URL'inizi buraya ekleyin)
const API_URL = 'http://localhost:8080/api/announcement';

export const getAllAnnouncements = async () => {
    try {
        const response = await axios.get(`${API_URL}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching announcements:', error);
        throw error;
    }
};

export const getAnnouncementById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data; // Belirli bir duyuruyu döndür
    } catch (error) {
        console.error(`Error fetching announcement with id ${id}:`, error);
        throw error;
    }
};

export const addAnnouncement = async (announcementData) => {
    try {
        const response = await axios.post(`${API_URL}`, announcementData, {

        });
        return response.data; // Eklenen duyuruyu döndür
    } catch (error) {
        console.error('Error adding announcement:', error);
        throw error;
    }
};

export const updateAnnouncement = async (id, announcementData) => {
    try {
        const response = await axios.put(`${API_URL}/${id}`, announcementData, {
            headers: {
                'Content-Type': 'multipart/form-data', // Resim yükleme işlemi varsa
            },
        });
        return response.data; // Güncellenen duyuruyu döndür
    } catch (error) {
        console.error(`Error updating announcement with id ${id}:`, error);
        throw error;
    }
};

export const deleteAnnouncement = async (id) => {
    try {
        const response = await axios.delete(`${API_URL}/${id}`);
        return response.data; // Silinen duyuruyu döndür
    } catch (error) {
        console.error(`Error deleting announcement with id ${id}:`, error);
        throw error;
    }
};
