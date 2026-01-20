<template>
    <div class="table-card">
        <table class="history-table">
            <thead>
                <tr>
                    <th>Fecha / Hora</th>
                    <th>Archivo</th>
                    <th>Formato</th>
                    <th>Estado</th>
                    <th>Progreso</th>
                    <th class="text-right">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="job in history" :key="job.jobId">
                    <td>
                        <div class="date-col">
                            <span class="date">{{ formatDate(job.createdAt) }}</span>
                            <span class="time">{{ formatTime(job.createdAt) }}</span>
                        </div>
                    </td>

                    <td class="file-col" :title="job.fileName">
                        <div class="file-wrapper">
                            <IconComponents name="file-text" :size="16" />
                            {{ truncate(job.fileName, 30) }}
                        </div>
                    </td>

                    <td>
                        <span :class="['badge-format', getFormatClass(job.fileName)]">
                            {{ getFormat(job.fileName) }}
                        </span>
                    </td>

                    <td>
                        <span :class="['badge-status', getStatusClass(job.status)]">
                            {{ translateStatus(job.status) }}
                        </span>
                    </td>

                    <td>
                        <div class="progress-mini">
                            <div class="progress-bar-bg">
                                <div class="progress-bar-fill"
                                    :style="{ width: calculateProgress(job) + '%', backgroundColor: getStatusColor(job.status) }">
                                </div>
                            </div>
                            <small>{{ job.processedRecords }} / {{ job.totalRecords }}</small>
                        </div>
                    </td>

                    <td class="text-right">
                        <button @click="navigateToDetail(job.jobId)" class="btn-icon"
                            :disabled="job.status === 'PENDING'" title="Ver Detalles">
                            <IconComponents name="eye" :size="18" />
                        </button>
                    </td>
                </tr>

                <tr v-if="history.length === 0">
                    <td colspan="6" class="empty-state-row">
                        <div class="empty-content">
                            <IconComponents name="search" :size="32" class="mb-2 text-muted" />
                            <p>No se encontraron resultados.</p>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="pagination-container" v-if="totalPages > 0">
            <span class="pagination-info">
                Mostrando <strong>{{ history.length }}</strong> de {{ totalItems }} registros
            </span>

            <div class="pagination-wrapper">
                <button class="pagination-btn btn-nav" :disabled="currentPage === 0"
                    @click="$emit('changePage', currentPage - 1)" title="Anterior">
                    <IconComponents name="chevron-left" :size="16" />
                    <span>Anterior</span>
                </button>

                <template v-for="(page, index) in visiblePages" :key="index">
                    <span v-if="page === '...'" class="pagination-ellipsis">...</span>
                    <button v-else class="pagination-btn btn-page" :class="{ active: currentPage === (page - 1) }"
                        @click="$emit('changePage', page - 1)">
                        {{ page }}
                    </button>
                </template>

                <button class="pagination-btn btn-nav" :disabled="currentPage >= totalPages - 1"
                    @click="$emit('changePage', currentPage + 1)" title="Siguiente">
                    <span>Siguiente</span>
                    <IconComponents name="chevron-right" :size="16" />
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import IconComponents from '@/components/icons/IconComponents.vue'

const props = defineProps(['history', 'totalItems', 'totalPages', 'currentPage'])
defineEmits(['changePage'])
const router = useRouter()

// --- NAVEGACIÓN ---
const navigateToDetail = (jobId) => {
    // Asegúrate de tener esta ruta configurada en tu router/index.js
    router.push({ name: 'BatchDetail', params: { id: jobId } })
}

// --- LÓGICA PAGINACIÓN (ELLIPSIS) ---
const visiblePages = computed(() => {
    const total = props.totalPages
    const current = props.currentPage + 1 // Usamos base 1 para mostrar
    const delta = 2 // Páginas a mostrar alrededor de la actual
    const range = []
    const rangeWithDots = []
    let l

    for (let i = 1; i <= total; i++) {
        if (i === 1 || i === total || (i >= current - delta && i <= current + delta)) {
            range.push(i)
        }
    }

    for (let i of range) {
        if (l) {
            if (i - l === 2) { rangeWithDots.push(l + 1) }
            else if (i - l !== 1) { rangeWithDots.push('...') }
        }
        rangeWithDots.push(i)
        l = i
    }
    return rangeWithDots
})

// --- UTILS (Iguales que antes) ---
const formatDate = (d) => d ? new Date(d).toLocaleDateString() : '-'
const formatTime = (d) => d ? new Date(d).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''
const truncate = (s, n) => (s && s.length > n) ? s.substr(0, n - 1) + '...' : s
const getFormat = (n) => { if (!n) return '?'; const x = n.toLowerCase(); return x.endsWith('.csv') ? 'CSV' : 'Excel' }
const getFormatClass = (n) => { if (!n) return ''; const x = n.toLowerCase(); return x.endsWith('.csv') ? 'fmt-csv' : 'fmt-excel' }
const translateStatus = (s) => ({ 'COMPLETED': 'Completado', 'FAILED': 'Fallido', 'PROCESSING': 'Procesando', 'PENDING': 'En Cola' }[s] || s)
const getStatusClass = (s) => ({ 'COMPLETED': 'status-success', 'FAILED': 'status-error', 'PROCESSING': 'status-info' }[s] || 'status-warning')
const getStatusColor = (s) => ({ 'COMPLETED': '#10b981', 'FAILED': '#ef4444' }[s] || '#6366f1')
const calculateProgress = (j) => (!j.totalRecords) ? 0 : Math.min(Math.round((j.processedRecords / j.totalRecords) * 100), 100)
</script>

<style scoped>
/* Estilos Tabla (Iguales que antes) */
.table-card {
    background: var(--bg-white);
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid var(--border-color);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.history-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.9rem;
}

.history-table th {
    background: var(--bg-light);
    padding: 14px 20px;
    text-align: left;
    font-weight: 600;
    color: var(--text-secondary);
    border-bottom: 1px solid var(--border-color);
}

.history-table td {
    padding: 14px 20px;
    border-bottom: 1px solid var(--border-color);
    vertical-align: middle;
    color: var(--text-primary);
}

.text-right {
    text-align: right;
}

.text-muted {
    color: var(--text-secondary);
}

.file-wrapper {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 500;
    color: var(--primary-color);
}

.date-col {
    display: flex;
    flex-direction: column;
}

.date {
    font-weight: 600;
}

.time {
    font-size: 0.8rem;
    color: var(--text-secondary);
}

/* Badges & Progress */
.badge-format {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 0.75rem;
    font-weight: 700;
    display: inline-block;
}

.fmt-excel {
    background: rgba(16, 185, 129, 0.1);
    color: #10b981;
    border: 1px solid rgba(16, 185, 129, 0.2);
}

.fmt-csv {
    background: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
    border: 1px solid rgba(59, 130, 246, 0.2);
}

.badge-status {
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
    display: inline-block;
    min-width: 90px;
    text-align: center;
}

.status-success {
    background: rgba(16, 185, 129, 0.15);
    color: #059669;
}

.status-error {
    background: rgba(239, 68, 68, 0.15);
    color: #dc2626;
}

.status-info {
    background: rgba(99, 102, 241, 0.15);
    color: #4f46e5;
}

.status-warning {
    background: rgba(245, 158, 11, 0.15);
    color: #d97706;
}

.progress-mini {
    width: 100px;
}

.progress-bar-bg {
    width: 100%;
    height: 6px;
    background: var(--border-color);
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 2px;
}

.progress-bar-fill {
    height: 100%;
    border-radius: 3px;
    transition: width 0.3s ease;
}

.progress-mini small {
    font-size: 0.75rem;
    color: var(--text-secondary);
}

.btn-icon {
    background: transparent;
    border: 1px solid var(--border-color);
    border-radius: 6px;
    padding: 6px;
    cursor: pointer;
    transition: all 0.2s;
    color: var(--text-secondary);
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.btn-icon:hover:not(:disabled) {
    background: var(--bg-light);
    border-color: var(--primary-color);
    color: var(--primary-color);
}

.btn-icon:disabled {
    opacity: 0.4;
    cursor: not-allowed;
}

.empty-state-row {
    text-align: center;
    padding: 40px !important;
    color: var(--text-secondary);
}

.empty-content {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.mb-2 {
    margin-bottom: 0.5rem;
}

/* --- ESTILOS PAGINACIÓN MEJORADOS --- */
.pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background: var(--bg-light);
    /* Fondo sutil para separar */
    border-top: 1px solid var(--border-color);
}

.pagination-info {
    font-size: 0.9rem;
    color: var(--text-secondary);
}

.pagination-wrapper {
    display: flex;
    align-items: center;
    gap: 6px;
}

.pagination-btn {
    height: 36px;
    min-width: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid var(--border-color);
    background: var(--bg-white);
    border-radius: 8px;
    /* Bordes más redondeados */
    cursor: pointer;
    color: var(--text-primary);
    font-weight: 500;
    transition: all 0.2s ease;
    font-size: 0.9rem;
    padding: 0 12px;
}

.pagination-btn:hover:not(:disabled) {
    border-color: var(--primary-color);
    color: var(--primary-color);
    background: var(--bg-light);
}

.pagination-btn.active {
    background: var(--primary-color);
    color: white;
    border-color: var(--primary-color);
    box-shadow: 0 2px 4px rgba(99, 102, 241, 0.25);
}

.pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    background: var(--bg-light);
}

.btn-nav {
    gap: 6px;
    /* Espacio entre icono y texto */
    padding: 0 14px;
    font-weight: 600;
}

.pagination-ellipsis {
    color: var(--text-secondary);
    padding: 0 4px;
    font-weight: bold;
}

/* Dark Mode */
[data-theme='dark'] .table-card {
    background: var(--bg-card);
    border-color: var(--border-color);
}

[data-theme='dark'] .history-table th,
[data-theme='dark'] .pagination-container {
    background: var(--bg-lighter);
    color: var(--text-secondary);
    border-color: var(--border-color);
}

[data-theme='dark'] .history-table td {
    border-color: var(--border-color);
}

[data-theme='dark'] .btn-icon,
[data-theme='dark'] .pagination-btn {
    border-color: var(--border-color);
    color: var(--text-primary);
    background: var(--bg-card);
}

[data-theme='dark'] .pagination-btn:hover:not(:disabled) {
    background: var(--bg-lighter);
}

[data-theme='dark'] .pagination-btn.active {
    background: var(--primary-color);
    color: white;
}
</style>