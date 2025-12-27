import axios from 'axios';
// Importamos la configuración
import { API_BASE_URL } from '@/config/backend'; 

const api = axios.create({
    baseURL: API_BASE_URL, 
    headers: {
        'Content-Type': 'application/json'
    }
});

export default api;