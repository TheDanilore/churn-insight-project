/**
 * Toast Composable
 * Simple notification system
 */
import { reactive } from 'vue'

const state = reactive({
  toasts: []
})

let toastId = 0

export function useToast() {
  /**
   * Normaliza el mensaje a string
   * Soporta strings, arrays y objetos
   */
  const normalizeMessage = (message) => {
    if (!message) return 'Error desconocido'

    // Si es array, unir con saltos de línea
    if (Array.isArray(message)) {
      return message
        .filter(msg => msg && String(msg).trim())
        .join('\n')
        .substring(0, 500) // Limitar a 500 caracteres
    }

    // Si es objeto, convertir a string
    if (typeof message === 'object') {
      return JSON.stringify(message).substring(0, 500)
    }

    // Si es string, usar tal cual (máximo 500 caracteres)
    return String(message).substring(0, 500)
  }

  const add = (message, type = 'info', duration = 3000) => {
    const id = toastId++
    const normalizedMsg = normalizeMessage(message)
    const toast = { id, message: normalizedMsg, type, visible: true }

    state.toasts.push(toast)

    if (duration > 0) {
      setTimeout(() => {
        remove(id)
      }, duration)
    }

    return id
  }

  const remove = (id) => {
    const index = state.toasts.findIndex(t => t.id === id)
    if (index > -1) {
      state.toasts[index].visible = false
      setTimeout(() => {
        state.toasts.splice(index, 1)
      }, 300) // Wait for animation
    }
  }

  /**
   * Procesa errores de validación 422
   * Convierte { field: [messages] } a "field: message\nfield2: message2"
   */
  const validationError = (errors, duration = 6000) => {
    if (!errors || typeof errors !== 'object') {
      return error('Error de validación', duration)
    }

    const fieldErrors = Object.entries(errors)
      .map(([field, messages]) => {
        const msg = Array.isArray(messages) ? messages[0] : messages
        return `${field}: ${msg}`
      })
      .slice(0, 3) // Máximo 3 campos

    if (fieldErrors.length === 0) {
      return error('Error de validación', duration)
    }

    return error(fieldErrors, duration) // Pasa array para multi-línea
  }

  const success = (message, duration = 3000) => add(message, 'success', duration)
  const error = (message, duration = 6000) => add(message, 'error', duration) // 6s para errores (más visible)
  const warning = (message, duration = 4000) => add(message, 'warning', duration)
  const info = (message, duration = 3500) => add(message, 'info', duration)

  return {
    toasts: state.toasts,
    add,
    remove,
    success,
    error,
    warning,
    info,
    validationError
  }
}
