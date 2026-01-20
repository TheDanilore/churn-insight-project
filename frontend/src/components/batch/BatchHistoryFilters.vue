<template>
    <div class="toolbar">
        <div class="filters-container">

            <div class="filter-item">
                <label>Desde</label>
                <div class="input-wrapper">
                    <input type="date" :value="filters.dateFrom"
                        @input="$emit('update:filters', { ...filters, dateFrom: $event.target.value })"
                        class="form-control">
                </div>
            </div>

            <div class="filter-item">
                <label>Hasta</label>
                <div class="input-wrapper">
                    <input type="date" :value="filters.dateTo"
                        @input="$emit('update:filters', { ...filters, dateTo: $event.target.value })"
                        class="form-control">
                </div>
            </div>

            <div class="filter-item">
                <label>Formato</label>
                <select :value="filters.format"
                    @change="$emit('update:filters', { ...filters, format: $event.target.value })" class="form-control">
                    <option value="all">Todos</option>
                    <option value="xlsx">Excel (.xlsx)</option>
                    <option value="csv">CSV (.csv)</option>
                </select>
            </div>

            <div class="filter-item">
                <label>Estado</label>
                <select :value="filters.status"
                    @change="$emit('update:filters', { ...filters, status: $event.target.value })" class="form-control">
                    <option value="all">Todos</option>
                    <option value="COMPLETED">Completados</option>
                    <option value="FAILED">Fallidos</option>
                    <option value="PROCESSING">Procesando</option>
                </select>
            </div>

            <div class="filter-item button-item">
                <button @click="handleRefresh" class="btn-refresh" title="Actualizar lista">
                    <div :class="{ 'spin-anim': isRefreshing }">
                        <IconComponents name="refresh-cw" :size="20" />
                    </div>
                </button>
            </div>
        </div>

        <div class="pagination-group">
            <label>Mostrar</label>
            <select :value="pagination.itemsPerPage"
                @change="$emit('update:pagination', { ...pagination, itemsPerPage: +$event.target.value })"
                class="form-control small">
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
                <option :value="50">50</option>
            </select>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import IconComponents from '@/components/icons/IconComponents.vue'

defineProps(['filters', 'pagination'])
const emit = defineEmits(['update:filters', 'update:pagination', 'refresh'])

const isRefreshing = ref(false)

const handleRefresh = () => {
    isRefreshing.value = true
    emit('refresh')
    setTimeout(() => isRefreshing.value = false, 1000)
}
</script>

<style scoped>
/* CONTENEDOR PRINCIPAL */
.toolbar {
    background: var(--bg-white);
    padding: 20px 24px;
    border-radius: 16px;
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    flex-wrap: wrap;
    gap: 20px;
    margin-bottom: 24px;
    border: 1px solid var(--border-color);
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px -1px rgba(0, 0, 0, 0.02);
    transition: all 0.3s ease;
}

/* GRUPOS */
.filters-container {
    display: flex;
    gap: 16px;
    align-items: flex-end;
    flex-wrap: wrap;
}

.filter-item {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.pagination-group {
    display: flex;
    flex-direction: column;
    gap: 6px;
    align-items: flex-end;
}

/* LABELS */
label {
    font-size: 0.75rem;
    font-weight: 600;
    color: var(--text-secondary);
    text-transform: uppercase;
    letter-spacing: 0.5px;
    margin-left: 2px;
}

/* INPUTS Y SELECTS */
.form-control {
    padding: 10px 14px;
    border: 1px solid var(--border-color);
    border-radius: 10px;
    background: var(--bg-light);
    color: var(--text-primary);
    font-size: 0.9rem;
    min-width: 140px;
    height: 42px;
    /* Altura fija para alineación */
    transition: all 0.2s ease;
    outline: none;
    cursor: pointer;
}

.form-control:hover {
    border-color: #cbd5e1;
    background: var(--bg-white);
}

.form-control:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
    /* Anillo de foco sutil */
    background: var(--bg-white);
}

.form-control.small {
    min-width: 80px;
    text-align: center;
}

/* BOTÓN REFRESH */
.button-item {
    justify-content: flex-end;
}

.btn-refresh {
    width: 42px;
    height: 42px;
    border: 1px solid var(--border-color);
    background: var(--bg-white);
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.2s ease;
    color: var(--text-secondary);
    display: flex;
    align-items: center;
    justify-content: center;
}

.btn-refresh:hover {
    background: var(--bg-light);
    border-color: var(--primary-color);
    color: var(--primary-color);
    transform: translateY(-1px);
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.btn-refresh:active {
    transform: translateY(0);
}

/* ANIMACIÓN */
.spin-anim {
    animation: spin 0.8s cubic-bezier(0.4, 0, 0.2, 1) infinite;
    display: flex;
}

@keyframes spin {
    100% {
        transform: rotate(360deg);
    }
}

/* DARK MODE */
[data-theme='dark']  .toolbar {
    background: var(--bg-card);
    border-color: var(--border-color);
    box-shadow: none;
}

[data-theme='dark']  .form-control {
    background: var(--bg-lighter);
    border-color: var(--border-color);
    color: var(--text-primary);
}

[data-theme='dark']  .form-control:hover {
    border-color: #475569;
}

[data-theme='dark']  .form-control:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
}

[data-theme='dark']  .btn-refresh {
    background: var(--bg-lighter);
    border-color: var(--border-color);
    color: var(--text-secondary);
}

[data-theme='dark']  .btn-refresh:hover {
    background: #1e293b;
    border-color: var(--primary-color);
    color: var(--primary-color);
}
</style>