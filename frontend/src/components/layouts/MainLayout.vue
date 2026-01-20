<template>
    <div class="wrapper" :class="{ 'full-screen': hideHeader && hideFooter && hideSidebar }">
        <!-- Sidebar -->
        <SidebarNav v-if="!hideSidebar" :isOpen="sidebarOpen" @toggle="toggleSidebar" @navigate="handleNavigation" />

        <!-- Main Content -->
        <div class="main">
            <!-- Header -->
            <HeaderSection v-if="!hideHeader" @toggleSidebar="toggleSidebar" />

            <!-- Page Content -->
            <main class="content">
                <slot />
            </main>

            <!-- Footer -->
            <FooterSection v-if="!hideFooter" />
        </div>

        <!-- Overlay para móvil -->
        <div v-if="sidebarOpen && !hideSidebar" class="sidebar-overlay" @click="toggleSidebar"></div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import SidebarNav from '@/components/layouts/SidebarNav.vue'
import HeaderSection from '@/components/layouts/HeaderSection.vue'
import FooterSection from '@/components/layouts/FooterSection.vue'
import { useRouter } from 'vue-router'
import { useSidebarState } from '@/composables/useSidebarState'

defineProps({
    hideHeader: {
        type: Boolean,
        default: false,
    },
    hideFooter: {
        type: Boolean,
        default: false,
    },
    hideSidebar: {
        type: Boolean,
        default: false,
    },
    hidePadding: {
        type: Boolean,
        default: false,
    },
})

const router = useRouter()
const { sidebarOpen, toggleSidebar, syncWithBreakpoint } = useSidebarState()

const handleNavigation = (view) => {
    router.push({ name: view })
    // Cerrar sidebar en móvil después de navegar
    if (window.innerWidth < 992) {
        sidebarOpen.value = false
    }
}

/**
 * Sincroniza el sidebar con el tamaño de la ventana
 */
const handleResize = () => {
    syncWithBreakpoint()
}

onMounted(() => {
    // Sincronizar estado al montar (cargará desde localStorage si existe)
    syncWithBreakpoint()
    window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.wrapper {
    display: flex;
    width: 100%;
    min-height: 100vh;
    background-color: var(--body-bg);
}

.wrapper.full-screen {
    width: 100%;
    height: 100vh;
}

.wrapper.full-screen .main {
    margin-left: 0 !important;
}

.main {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    margin-left: 260px;
    transition: margin-left 0.3s ease;
}

/* Cuando el sidebar está cerrado, quitar el margen */
.wrapper:has(.sidebar:not(.sidebar-open)) .main {
    margin-left: 0;
}

.content {
    flex: 1;
    padding: 0;
    background-color: var(--body-bg);
}

.sidebar-overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

@media (max-width: 991px) {
    .main {
        margin-left: 0;
    }

    .sidebar-overlay {
        display: block;
    }
}

/* Dark mode con data-theme */
[data-theme="dark"] .wrapper,
[data-theme="dark"] .content {
    background-color: #0d1117;
}
</style>
