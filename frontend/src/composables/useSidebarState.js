import { ref, watch } from 'vue'

// Constantes
const STORAGE_KEY = 'sidebar-state'
const DESKTOP_BREAKPOINT = 992

// Estado reactivo compartido (a nivel de módulo para que sea singleton)
const sidebarOpen = ref(true)

// Flag para saber si ya se inicializó el watcher
let watcherInitialized = false

// Inicializar el estado desde localStorage cuando se carga el módulo
try {
    const saved = localStorage.getItem(STORAGE_KEY)
    if (saved !== null) {
        const state = JSON.parse(saved)
        if (typeof state === 'boolean') {
            sidebarOpen.value = state
        }
    }
} catch (error) {
    console.warn(`[useSidebarState] Error initializing from localStorage: ${error.message}`)
}

/**
 * Composable para manejar el estado persistente del sidebar
 * 
 * Proporciona una solución centralizada para:
 * - Obtener y guardar el estado del sidebar en localStorage
 * - Suscribirse a cambios del estado automáticamente
 * - Sincronizar el estado con breakpoints responsivos
 * 
 * Características:
 * - Persistencia automática en localStorage (clave: 'sidebar-state')
 * - Comportamiento responsive: se cierra automáticamente en móvil
 * - Manejo seguro de errores en localStorage
 * - Validación de datos guardados
 * - Estado compartido entre todos los componentes (singleton pattern)
 * 
 * @returns {Object} Objeto reactivo con métodos para controlar el sidebar
 * @returns {Ref<boolean>} sidebarOpen - Estado actual del sidebar (ref reactiva, compartida)
 * @returns {Function} getSidebarState - Obtiene el estado desde localStorage
 * @returns {Function} saveSidebarState - Guarda el estado en localStorage
 * @returns {Function} toggleSidebar - Alterna el estado del sidebar
 * @returns {Function} openSidebar - Abre el sidebar
 * @returns {Function} closeSidebar - Cierra el sidebar
 * @returns {Function} setSidebarState - Establece un estado específico
 * @returns {Function} syncWithBreakpoint - Sincroniza con el breakpoint (desktop/mobile)
 * 
 * @example
 * ```vue
 * <script setup>
 * import { useSidebarState } from '@/composables/useSidebarState'
 * 
 * const { sidebarOpen, toggleSidebar, syncWithBreakpoint } = useSidebarState()
 * 
 * onMounted(() => {
 *   syncWithBreakpoint()
 * })
 * </script>
 * ```
 */
export function useSidebarState() {

    /**
     * Obtiene el estado del sidebar desde localStorage
     * Si no hay estado guardado, usa el breakpoint para determinar el estado por defecto
     * 
     * @returns {boolean} true si el sidebar debe estar abierto, false si está cerrado
     * 
     * @example
     * ```js
     * const state = getSidebarState() // true o false
     * ```
     */
    const getSidebarState = () => {
        try {
            const saved = localStorage.getItem(STORAGE_KEY)
            if (saved !== null) {
                const state = JSON.parse(saved)
                // Validar que sea un boolean
                if (typeof state === 'boolean') {
                    return state
                }
            }
        } catch (error) {
            console.warn(`[useSidebarState] Error reading from localStorage: ${error.message}`)
        }

        // Por defecto: abierto en desktop (>= 992px), cerrado en móvil (< 992px)
        return window.innerWidth >= DESKTOP_BREAKPOINT
    }

    /**
     * Guarda el estado actual del sidebar en localStorage
     * 
     * @param {boolean} state - Estado a guardar (true = abierto, false = cerrado)
     * 
     * @example
     * ```js
     * saveSidebarState(true) // Guarda que el sidebar está abierto
     * ```
     */
    const saveSidebarState = (state) => {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
        } catch (error) {
            console.warn(`[useSidebarState] Error saving to localStorage: ${error.message}`)
        }
    }

    /**
     * Alterna el estado actual del sidebar
     * Si está abierto, lo cierra; si está cerrado, lo abre
     * 
     * @example
     * ```js
     * toggleSidebar() // Cambia entre abierto/cerrado
     * ```
     */
    const toggleSidebar = () => {
        sidebarOpen.value = !sidebarOpen.value
    }

    /**
     * Abre el sidebar inmediatamente
     * 
     * @example
     * ```js
     * openSidebar() // Abre el sidebar
     * ```
     */
    const openSidebar = () => {
        sidebarOpen.value = true
    }

    /**
     * Cierra el sidebar inmediatamente
     * 
     * @example
     * ```js
     * closeSidebar() // Cierra el sidebar
     * ```
     */
    const closeSidebar = () => {
        sidebarOpen.value = false
    }

    /**
     * Establece el estado del sidebar a un valor específico
     * 
     * @param {boolean} state - Estado a establecer (true = abierto, false = cerrado)
     * 
     * @example
     * ```js
     * setSidebarState(false) // Cierra el sidebar específicamente
     * ```
     */
    const setSidebarState = (state) => {
        sidebarOpen.value = state
    }

    /**
     * Sincroniza el estado del sidebar con el breakpoint de pantalla
     * 
     * En desktop (width >= 992px): restaura el estado guardado
     * En móvil (width < 992px): cierra automáticamente
     * 
     * Útil para llamar en:
     * - onMounted(): inicializar el estado al cargar el componente
     * - En listener de 'resize': adaptar cuando cambia el tamaño de la ventana
     * 
     * @example
     * ```js
     * onMounted(() => {
     *   syncWithBreakpoint() // Inicializar según el tamaño actual
     *   window.addEventListener('resize', syncWithBreakpoint)
     * })
     * ```
     */
    const syncWithBreakpoint = () => {
        if (window.innerWidth < DESKTOP_BREAKPOINT) {
            // En móvil: siempre cerrado
            sidebarOpen.value = false
        } else {
            // En desktop: restaurar el estado guardado
            sidebarOpen.value = getSidebarState()
        }
    }

    /**
     * Watcher automático: guarda el estado cada vez que cambia
     * Se configura solo una vez (patrón singleton)
     */
    if (!watcherInitialized) {
        watch(
            sidebarOpen,
            (newState) => {
                saveSidebarState(newState)
            },
            { immediate: false }
        )
        watcherInitialized = true
    }

    // Retornar API pública del composable
    return {
        // Estado reactivo
        sidebarOpen,

        // Métodos para lectura/escritura
        getSidebarState,
        saveSidebarState,

        // Métodos para control del estado
        toggleSidebar,
        openSidebar,
        closeSidebar,
        setSidebarState,

        // Método para sincronización responsiva
        syncWithBreakpoint,
    }
}

