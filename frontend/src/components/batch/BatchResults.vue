<template>
    <div class="results-container">

        <BatchResultsStats :job-status="localJobStatus" />

        <div class="details-card">
            <div class="card-header">
                <div class="header-left">
                    <h3>📋 Resultados Detallados</h3>
                    <p class="subtitle">Análisis individual por cliente</p>
                </div>
                <button @click="downloadExcel" class="btn-excel">
                    <IconComponents name="download" :size="18" />
                    <span class="hide-mobile">Descargar Excel</span>
                </button>
            </div>

            <BatchResultsFilters :filters="filters" @update:search="val => filters.search = val"
                @update:alerta="val => filters.alerta = val" @filter="applyFilters" />

            <div v-if="loading" class="loading-state">
                <div class="spinner"></div>
                <p>Recuperando predicciones...</p>
            </div>

            <BatchResultsTable v-else :results="results" :pagination="pagination" @changePage="changePage" />
        </div>

        <div class="footer-actions" v-if="$attrs.onNewUpload">
            <button @click="$emit('new-upload')" class="btn-primary">
                📁 Cargar Otro Archivo
            </button>
        </div>

    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { exportBatchResultsExcelRequest, getBatchResultsRequest, getJobStatusRequest } from '@/services/churnService'
import IconComponents from '@/components/icons/IconComponents.vue'
import BatchResultsStats from '@/components/batch-detail/BatchResultsStats.vue'
import BatchResultsFilters from '@/components/batch-detail/BatchResultsFilters.vue'
import BatchResultsTable from '@/components/batch-detail/BatchResultsTable.vue'

const props = defineProps({ jobStatus: { type: Object, required: true } })
defineEmits(['new-upload'])

// Estado
const localJobStatus = ref({ ...props.jobStatus })
const results = ref([])
const loading = ref(false)

const pagination = reactive({
    currentPage: 0,
    itemsPerPage: 10,
    totalPages: 0,
    totalElements: 0
})

const filters = reactive({ search: '', alerta: '' })

// Carga Inicial
onMounted(async () => {
    if (props.jobStatus?.jobId) {
        if (props.jobStatus.totalRecords === undefined) {
            try {
                localJobStatus.value = await getJobStatusRequest(props.jobStatus.jobId)
            } catch (e) { console.error("Error status:", e) }
        }
        await loadTableData()
    }
})

// Lógica de Datos
const loadTableData = async () => {
    loading.value = true
    try {
        const response = await getBatchResultsRequest(
            localJobStatus.value.jobId,
            pagination.currentPage,
            pagination.itemsPerPage,
            filters.search,
            filters.alerta
        )
        results.value = response.content
        pagination.totalPages = response.totalPages
        pagination.totalElements = response.totalElements
    } catch (error) {
        console.error("Error:", error)
    } finally {
        loading.value = false
    }
}

const changePage = (page) => {
    pagination.currentPage = page
    loadTableData()
}

const applyFilters = () => {
    pagination.currentPage = 0
    loadTableData()
}

const downloadExcel = async () => {
    try {
        const blob = await exportBatchResultsExcelRequest(localJobStatus.value.jobId)
        const url = window.URL.createObjectURL(new Blob([blob]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `reporte_${localJobStatus.value.jobId}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
    } catch (e) { console.error(e) }
}
</script>

<style scoped>
.results-container {
    font-family: 'Inter', sans-serif;
    animation: fadeIn 0.4s ease;
    margin: 0 auto;
    width: 100%;
}

.details-card {
    background: var(--bg-white);
    border-radius: 16px;
    border: 1px solid var(--border-color);
    overflow: hidden;
    display: flex;
    flex-direction: column;
    min-height: 500px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

.card-header {
    padding: 20px 24px;
    border-bottom: 1px solid var(--border-color);
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-left h3 {
    margin: 0;
    font-size: 1.1rem;
    color: var(--text-primary);
}

.subtitle {
    margin: 2px 0 0;
    font-size: 0.85rem;
    color: var(--text-secondary);
}

.btn-excel {
    display: flex;
    align-items: center;
    gap: 8px;
    background: var(--bg-white);
    border: 1px solid var(--border-color);
    padding: 8px 16px;
    border-radius: 8px;
    cursor: pointer;
    color: var(--text-primary);
    font-size: 0.85rem;
    font-weight: 500;
    transition: all 0.2s;
}

.btn-excel:hover {
    border-color: var(--primary-color);
    color: var(--primary-color);
    background: var(--bg-lighter);
}

.footer-actions {
    margin-top: 30px;
    text-align: center;
}

.btn-primary {
    background: var(--primary-color);
    color: white;
    border: none;
    padding: 12px 24px;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: transform 0.2s;
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.loading-state {
    padding: 60px;
    text-align: center;
    color: var(--text-secondary);
}

.spinner {
    border: 3px solid #e0e7ff;
    border-top: 3px solid var(--primary-color);
    border-radius: 50%;
    width: 30px;
    height: 30px;
    animation: spin 1s linear infinite;
    margin: 0 auto 15px;
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
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Dark Mode */
[data-theme='dark']  .details-card {
    background: var(--bg-card);
    border-color: var(--border-color);
}
</style>