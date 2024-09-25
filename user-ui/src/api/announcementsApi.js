// src/api/announcementsApi.js
export async function getAllAnnouncements() {
    try {
        const response = await fetch('http://localhost:8080/api/announcement/valid');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching announcements:', error);
        return [];
    }
}
