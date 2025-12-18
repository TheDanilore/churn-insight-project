/**
 * Backend configuration
 * Centraliza constantes de configuración
 */

// Base URL de la API - desde variable de entorno o default
export const API_BASE_URL = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

// Keys de localStorage
export const STORAGE_KEYS = {
  AUTH_TOKEN: 'auth_token',
  USER_DATA: 'user_data',
  THEME: 'theme',
  LAST_ROUTE: 'lastRoute',
}

export default {
  API_BASE_URL,
  STORAGE_KEYS,
}
