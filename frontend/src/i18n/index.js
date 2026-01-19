import { createI18n } from 'vue-i18n'
import es from '../locales/es.json'
import en from '../locales/en.json'

const i18n = createI18n({
  legacy: false, // Usar Composition API
  locale: localStorage.getItem('lang') || 'es', // Idioma por defecto
  fallbackLocale: 'en',
  messages: {
    es,
    en,
  },
})

export default i18n
