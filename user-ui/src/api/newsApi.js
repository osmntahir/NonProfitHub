// src/api/newsApi.js
export async function getAllNews() {
    try {
        const response = await fetch('http://localhost:8080/api/news/valid');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching news:', error);
        return [];
    }
}

export async function getNewsById(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/news/${id}`);
        if (!response.ok) {
            throw new Error('News not found');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching news:', error);
        throw error;
    }
}
