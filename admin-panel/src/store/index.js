// src/store/index.js
import { defineStore } from 'pinia';
import jwtDecode from 'jwt-decode';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        user: null,
    }),
    getters: {
        isAuthenticated: (state) => !!state.token,
        userRoles: (state) => (state.user ? state.user.realm_access.roles : []),
    },
    actions: {
        login(token) {
            this.token = token;
            localStorage.setItem('token', token);
            const decoded = jwtDecode(token);
            this.user = decoded;
        },
        logout() {
            this.token = '';
            this.user = null;
            localStorage.removeItem('token');
        },
        initialize() {
            if (this.token) {
                try {
                    const decoded = jwtDecode(this.token);
                    this.user = decoded;
                } catch (error) {
                    this.logout();
                }
            }
        },
    },
});
