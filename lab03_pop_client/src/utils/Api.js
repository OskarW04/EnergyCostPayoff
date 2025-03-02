import axios from "axios";

export const API = axios.create({
    baseURL: "http://localhost:8080",
});

API.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            sessionStorage.removeItem("token");
            window.location.href = "/";
        }
        return Promise.reject(error);
    }
);

API.interceptors.request.use((config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

