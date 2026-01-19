import { ref, watch } from 'vue'

/**
 * Composable para manejar el estado persistente del sidebar
 * 
 * Proporciona funciones para:
 * - Obtener y guardar el estado del sidebar en localStorage
 * - Suscribirse a cambios del estado
 * - Sincronizar el estado con el tamaño de la ventana
 * 
 * @returns {Object} { sidebarOpen, getSidebarState, saveSidebarState, toggleSidebar }
 */
export function useSidebarState() {
    const sidebarOpen = ref(true)

    /**
     * Obtiene el estado del sidebar desde localStorage
     * @returns {boolean} true si el sidebar debe estar abierto
     */
    const getSidebarState = () => {
        try {
            const saved = localStorage.getItem('sidebar-state')
            if (saved !== null) {
                const state = JSON.parse(saved)
                // Validar que sea un boolean
                if (typeof state === 'boolean') {
                    return state
                }
            }
        } catch (error) {
            console.warn('Error reading sidebar state from localStorage:', error)
        }

        // Por defecto, abierto en desktop, cerrado en móvil
        return window.innerWidth >= 992
    }

    /**
     * Guarda el estado del sidebar en localStorage
     * @param {boolean} state - Estado del sidebar
     */
    const saveSidebarState = (state) => {
        try {
            localStorage.setItem('sidebar-state', JSON.stringify(state))
        } catch (error) {
            console.warn('Error saving sidebar state to localStorage:', error)
        }
    }

    /**
     * Alterna el estado del sidebar
     */
    const toggleSidebar = () => {
        sidebarOpen.value = !sidebarOpen.value
    }

    /**
     * Abre el sidebar
     */
    const openSidebar = () => {
        sidebarOpen.value = true
    }

    /**
     * Cierra el sidebar
     */
    const closeSidebar = () => {
        sidebarOpen.value = false
    }

    /**
     * Establece el estado del sidebar
     * @param {boolean} state
     */
    const setSidebarState = (state) => {
        sidebarOpen.value = state
    }

    /**
     * Sincroniza el estado basado en el breakpoint
     * En desktop (>= 992px): usar estado guardado
     * En móvil (< 992px): cerrar sidebar
     */
    const syncWithBreakpoint = () => {
        if (window.innerWidth < 992) {
            sidebarOpen.value = false
        } else {
            // En desktop, restaurar el estado guardado
            sidebarOpen.value = getSidebarState()
        }
    }

    // Watcher para guardar el estado cuando cambia
    watch(sidebarOpen, (newState) => {
        saveSidebarState(newState)
    })

    return {
        sidebarOpen,
        getSidebarState,
        saveSidebarState,
        toggleSidebar,
        openSidebar,
        closeSidebar,
        setSidebarState,
        syncWithBreakpoint,
    }
}
