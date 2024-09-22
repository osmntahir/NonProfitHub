// src/stores/auth.js
import { defineStore } from 'pinia';
import jwtDecode from 'jwt-decode';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        user: null,
    }),
    getters: {
        isAuthenticated: (state) => !!state.token && !isTokenExpired(state.token),
        userRoles: (state) => (state.user ? state.user.realm_access.roles : []),
    },
    actions: {
        setToken(token) {
            this.token = token;
            localStorage.setItem('token', token);
            this.user = jwtDecode(token);
        },
        clearToken() {
            this.token = null;
            localStorage.removeItem('token');
            this.user = null;
        },
    },
});

function isTokenExpired(token) {
    try {
        const decoded = jwtDecode(token);
        const now = Date.now().valueOf() / 1000;
        return decoded.exp < now;
    } catch (err) {
        return true;
    }
}
