<template>
    <MainLayout>
        <div class="history-container">

            <div class="page-header">
                <div>
                    <h1>📜 Historial de Importaciones</h1>
                    <p>Consulta y gestiona todas las cargas masivas realizadas.</p>
                </div>
                <div class="summary-stats">
                    <div class="stat-pill">
                        <span class="label">Total Histórico</span>
                        <span class="value">{{ totalElements }}</span>
                    </div>
                </div>
            </div>

            <BatchHistoryFilters :filters="filters" :pagination="pagination" @update:filters="updateFilters"
                @update:pagination="updatePagination" @refresh="loadData" />

            <div v-if="loading" class="loading-state">
                <div class="spinner"></div>
                <p>Cargando datos...</p>
            </div>

            <div v-else>
                <BatchHistoryTable :history="history" :total-items="totalElements" :total-pages="totalPages"
                    :current-page="pagination.currentPage" @view="viewDetails" @changePage="goToPage" />
            </div>

            <div v-if="selectedJob" class="modal-overlay" @click.self="selectedJob = null">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3>Detalle de Importación</h3>
                        <button class="btn-close" @click="selectedJob = null">✕</button>
                    </div>
                    <div class="modal-body">
                        <BatchResults :job-status="selectedJob" />
                    </div>
                </div>
            </div>

        </div>
    </MainLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import MainLayout from '@/components/layouts/MainLayout.vue'
import BatchHistoryFilters from '@/components/batch/BatchHistoryFilters.vue'
import BatchHistoryTable from '@/components/batch/BatchHistoryTable.vue'
import BatchResults from '@/components/batch/BatchResults.vue'
import { getBatchHistoryRequest } from '@/services/churnService'

// --- ESTADO ---
const history = ref([])
const loading = ref(true)
const selectedJob = ref(null)

// Paginación Backend
const totalElements = ref(0)
const totalPages = ref(0)
const first = ref(true)
const last = ref(true)

const filters = reactive({ status: 'all', dateFrom: '', dateTo: '', format: 'all' })
const pagination = reactive({ currentPage: 0, itemsPerPage: 10 })

// --- CARGA DE DATOS (SERVER SIDE) ---
const loadData = async () => {
    loading.value = true
    try {
        // 1. ✅ PASAMOS LOS FILTROS AL SERVICIO
        // El servicio getBatchHistoryRequest(page, size, filters) armará la URL
        const response = await getBatchHistoryRequest(
            pagination.currentPage,
            pagination.itemsPerPage,
            filters // <--- ¡AQUÍ ESTABA EL ERROR ANTES!
        )

        // 2. Asignamos la respuesta del Page<BatchJob>
        history.value = response.content
        totalElements.value = response.totalElements
        totalPages.value = response.totalPages
        first.value = response.first
        last.value = response.last

    } catch (error) {
        console.error("Error cargando historial:", error)
    } finally {
        loading.value = false
    }
}

// --- ACTUALIZADORES ---
const updateFilters = (newFilters) => {
    Object.assign(filters, newFilters)
    // Al cambiar filtros, volvemos a la página 1 para evitar errores de índice
    pagination.currentPage = 0
    loadData()
}

const updatePagination = (newPagination) => {
    Object.assign(pagination, newPagination)
    pagination.currentPage = 0 // Si cambia el tamaño, resetear página
    loadData()
}

const goToPage = (p) => {
    if (p >= 0 && p < totalPages.value) {
        pagination.currentPage = p
        loadData()
    }
}

const viewDetails = (job) => { selectedJob.value = job }

// Inicializar
onMounted(loadData)
</script>

<style scoped>
/* Tus estilos se mantienen igual... */
.history-container {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
    color: var(--text-primary);
    font-family: 'Inter', sans-serif;
}

.page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 30px;
}

.page-header h1 {
    margin: 0 0 5px 0;
    font-size: 1.8rem;
}

.page-header p {
    margin: 0;
    color: var(--text-secondary);
}

.stat-pill {
    background: var(--bg-white);
    padding: 8px 16px;
    border-radius: 20px;
    border: 1px solid var(--border-color);
    display: flex;
    gap: 8px;
    font-weight: 600;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.stat-pill .label {
    color: var(--text-secondary);
    text-transform: uppercase;
    font-size: 0.8rem;
}

.stat-pill .value {
    color: var(--primary-color);
}

.pagination-footer {
    padding: 15px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid var(--border-color);
    margin-top: -1px;
    background: var(--bg-white);
    border-radius: 0 0 12px 12px;
    padding-left: 20px;
    padding-right: 20px;
}

.pagination-info {
    font-size: 0.85rem;
    color: var(--text-secondary);
}

.pagination-controls {
    display: flex;
    align-items: center;
    gap: 10px;
}

.btn-page {
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid var(--border-color);
    background: var(--bg-white);
    border-radius: 6px;
    cursor: pointer;
    color: var(--text-primary);
    transition: all 0.2s;
}

.btn-page:hover:not(:disabled) {
    background: var(--primary-color);
    color: white;
    border-color: var(--primary-color);
}

.btn-page:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.page-current {
    font-weight: 700;
    font-size: 0.9rem;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.6);
    backdrop-filter: blur(2px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    animation: fadeIn 0.2s ease;
}

.modal-content {
    background: var(--bg-white);
    border-radius: 16px;
    width: 95%;
    max-width: 1200px;
    max-height: 90vh;
    display: flex;
    flex-direction: column;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.modal-header {
    padding: 20px 25px;
    border-bottom: 1px solid var(--border-color);
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.modal-body {
    padding: 0;
    overflow-y: auto;
    flex: 1;
}

.btn-close {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    color: var(--text-secondary);
}

.loading-state {
    padding: 40px;
    text-align: center;
}

.spinner {
    border: 3px solid #e0e7ff;
    border-top: 3px solid #6366f1;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    animation: spin 1s linear infinite;
    margin: 0 auto 10px;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

[data-theme='dark']  .stat-pill,
[data-theme='dark']  .pagination-footer,
[data-theme='dark']  .modal-content,
[data-theme='dark']  .btn-page {
    background: var(--bg-card);
    border-color: var(--border-color);
}
</style>