import axios from 'axios';

const API_URL = 'http://localhost:8080/api/announcement';

export const getAllAnnouncements = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error('Error fetching announcements:', error);
        return [];
    }
};
