// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import axiosInstance from './plugins/axios';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);

// Axios'u global property olarak eklemek yerine, bileşenlerde doğrudan import edin.
// Ancak, global olarak erişmek isterseniz aşağıdaki şekilde ekleyebilirsiniz:
app.config.globalProperties.$axios = axiosInstance;

app.mount('#app');
