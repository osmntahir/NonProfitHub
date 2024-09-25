// src/utils/DateFormatter.js
export default class DateFormatter {
    constructor(date) {
        this.date = new Date(date);
    }

    getFormattedDate() {
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        return this.date.toLocaleDateString('tr-TR', options);
    }
}
