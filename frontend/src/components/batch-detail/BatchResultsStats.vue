<template>
    <div>
        <div :class="['status-banner', resultClass]">
            <div class="banner-icon">{{ resultIcon }}</div>

            <div class="banner-content">
                <div class="banner-header">
                    <h2>{{ resultTitle }}</h2>
                    <span class="job-id-badge" :title="jobStatus.jobId">
                        ID: {{ jobStatus.jobId }}
                    </span>
                </div>

                <div class="meta-row">
                    <div class="meta-item">
                        <IconComponents name="file-text" :size="16" />
                        <span class="font-medium">{{ jobStatus.fileName || 'Archivo desconocido' }}</span>
                    </div>

                    <div class="meta-item" v-if="jobStatus.completedAt">
                        <IconComponents name="clock" :size="16" />
                        <span>{{ formatDate(jobStatus.completedAt) }}</span>
                    </div>
                </div>

                <p v-if="!isSuccess && jobStatus.errorMessage" class="error-msg">
                    {{ jobStatus.errorMessage }}
                </p>
            </div>
        </div>

        <div class="kpi-grid">
            <div class="kpi-card">
                <div class="kpi-icon-bg total">
                    <IconComponents name="database" :size="24" />
                </div>
                <div class="kpi-content">
                    <span class="kpi-value">{{ jobStatus?.totalRecords || 0 }}</span>
                    <span class="kpi-label">Total Registros</span>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon-bg success">
                    <IconComponents name="check-circle" :size="24" />
                </div>
                <div class="kpi-content">
                    <span class="kpi-value text-success">{{ jobStatus?.processedRecords || 0 }}</span>
                    <span class="kpi-label">Procesados</span>
                </div>
            </div>

            <div v-if="jobStatus?.failedRecords > 0" class="kpi-card error">
                <div class="kpi-icon-bg error">
                    <IconComponents name="alert-triangle" :size="24" />
                </div>
                <div class="kpi-content">
                    <span class="kpi-value text-error">{{ jobStatus.failedRecords }}</span>
                    <span class="kpi-label">Fallidos</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'
import IconComponents from '@/components/icons/IconComponents.vue'

const props = defineProps({ jobStatus: { type: Object, required: true } })

const isSuccess = computed(() => props.jobStatus?.status === 'COMPLETED' && props.jobStatus?.failedRecords === 0)

const resultClass = computed(() => {
    if (isSuccess.value) return 'banner-success'
    if (props.jobStatus?.failedRecords > 0) return 'banner-warning'
    return 'banner-error'
})

const resultIcon = computed(() => isSuccess.value ? '🎉' : (props.jobStatus?.failedRecords > 0 ? '⚠️' : '❌'))
const resultTitle = computed(() => isSuccess.value ? 'Procesamiento Exitoso' : (props.jobStatus?.failedRecords > 0 ? 'Completado con Alertas' : 'Error en Procesamiento'))

// Utils
const formatDate = (d) => d ? new Date(d).toLocaleString() : '-'
</script>

<style scoped>
/* BANNER */
.status-banner {
    background: var(--bg-white);
    border-radius: 16px;
    padding: 24px;
    display: flex;
    align-items: flex-start;
    gap: 20px;
    border: 1px solid var(--border-color);
    border-left: 5px solid transparent;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.02);
    margin-bottom: 24px;
}

.banner-success {
    border-left-color: #10b981;
}

.banner-warning {
    border-left-color: #f59e0b;
}

.banner-error {
    border-left-color: #ef4444;
}

.banner-icon {
    font-size: 2rem;
    background: var(--bg-lighter);
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    flex-shrink: 0;
}

.banner-content {
    flex-grow: 1;
}

/* Header Row (Título + ID) */
.banner-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 8px;
    flex-wrap: wrap;
    justify-content: space-between;
}

.banner-header h2 {
    margin: 0;
    font-size: 1.25rem;
    color: var(--text-primary);
}

.job-id-badge {
    background: var(--bg-lighter);
    border: 1px solid var(--border-color);
    color: var(--text-secondary);
    font-family: 'Monaco', monospace;
    font-size: 0.75rem;
    padding: 4px 8px;
    border-radius: 6px;
}

/* Meta Row (Archivo + Fecha) */
.meta-row {
    display: flex;
    align-items: center;
    gap: 20px;
    color: var(--text-secondary);
    font-size: 0.9rem;
    flex-wrap: wrap;
    justify-content: space-between;
}

.meta-item {
    display: flex;
    align-items: center;
    gap: 6px;
}

.font-medium {
    font-weight: 600;
    color: var(--text-primary);
}

.error-msg {
    color: #ef4444;
    margin: 8px 0 0 0;
    font-size: 0.9rem;
}

/* KPI GRID */
.kpi-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
}

.kpi-card {
    background: var(--bg-white);
    padding: 20px;
    border-radius: 16px;
    border: 1px solid var(--border-color);
    display: flex;
    align-items: center;
    gap: 15px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

.kpi-icon-bg {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    flex-shrink: 0;
}

.kpi-icon-bg.total {
    background: linear-gradient(135deg, #6366f1, #8b5cf6);
}

.kpi-icon-bg.success {
    background: linear-gradient(135deg, #10b981, #34d399);
}

.kpi-icon-bg.error {
    background: linear-gradient(135deg, #ef4444, #f87171);
}

.kpi-content {
    display: flex;
    flex-direction: column;
}

.kpi-value {
    font-size: 1.6rem;
    font-weight: 800;
    color: var(--text-primary);
    line-height: 1;
}

.kpi-label {
    font-size: 0.8rem;
    text-transform: uppercase;
    color: var(--text-secondary);
    font-weight: 600;
}

.text-success {
    color: #10b981;
}

.text-error {
    color: #ef4444;
}

/* Dark Mode Support */
[data-theme='dark']  .status-banner,
[data-theme='dark']  .kpi-card {
    background: var(--bg-card);
    border-color: var(--border-color);
}

[data-theme='dark']  .job-id-badge {
    background: var(--bg-card);
    border-color: var(--border-color);
}
</style>