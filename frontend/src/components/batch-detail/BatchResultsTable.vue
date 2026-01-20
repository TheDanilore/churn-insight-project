<template>
    <div class="table-wrapper">
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th class="col-client">Cliente</th>
                        <th>Contacto</th>
                        <th>Contrato</th>
                        <th>Servicios</th>
                        <th class="text-right">Cargos</th>
                        <th class="text-center">Riesgo IA</th>
                        <th class="text-right">Resultado</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="row in results" :key="row.id">
                        <td>
                            <div class="cell-group">
                                <span class="font-bold text-primary">{{ row.clientName || 'Anónimo' }}</span>
                                <span class="badge-pill">Antig: {{ row.antiguedad }} m</span>
                            </div>
                        </td>
                        <td>
                            <div class="cell-group contact-cell">
                                <div class="icon-row" v-if="row.email">
                                    <IconComponents name="mail" :size="14" class="icon-muted" />
                                    <span class="truncate" :title="row.email">{{ row.email }}</span>
                                </div>
                                <div class="icon-row" v-if="row.phone">
                                    <IconComponents name="phone" :size="14" class="icon-muted" />
                                    <span>{{ row.phone }}</span>
                                </div>
                                <span v-if="!row.email && !row.phone" class="text-muted text-xs">—</span>
                            </div>
                        </td>
                        <td>
                            <div class="cell-group">
                                <span class="font-medium">{{ row.contrato }}</span>
                                <small class="text-muted">{{ row.metodoPago }}</small>
                            </div>
                        </td>
                        <td>
                            <div class="tags-group">
                                <span :class="['tag', getInternetClass(row.servicioInternet)]">
                                    {{ getInternetLabel(row.servicioInternet) }}
                                </span>
                                <span :class="['tag', row.soporteTecnico === 'Yes' ? 'tag-green' : 'tag-gray']">
                                    {{ row.soporteTecnico === 'Yes' ? '🛠️ Soporte' : 'Sin Soporte' }}
                                </span>
                            </div>
                        </td>
                        <td class="text-right">
                            <span class="amount">${{ Number(row.cargosMensuales).toFixed(2) }}</span>
                        </td>
                        <td class="text-center">
                            <div class="risk-cell">
                                <span :class="['risk-badge', getAlertClass(row.alerta)]">{{ row.alerta }}</span>

                                <div class="prob-wrapper"
                                    :title="`Probabilidad: ${(row.probabilidad * 100).toFixed(1)}%`">
                                    <div class="prob-track">
                                        <div class="prob-fill"
                                            :style="{ width: (row.probabilidad * 100) + '%', backgroundColor: getAlertColor(row.alerta) }">
                                        </div>
                                    </div>
                                    <span class="prob-text">{{ (row.probabilidad * 100).toFixed(0) }}%</span>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            <span :class="['status-pill', row.resultado === 'Se queda' ? 'status-ok' : 'status-bad']">
                                {{ row.resultado }}
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div v-if="results.length === 0" class="empty-state">
                <IconComponents name="inbox" :size="48" class="icon-empty" />
                <p>No se encontraron resultados.</p>
            </div>
        </div>

        <div class="pagination-footer" v-if="pagination.totalPages > 1">
            <span class="pagination-info">
                Página <strong>{{ pagination.currentPage + 1 }}</strong> de {{ pagination.totalPages }}
                (Total: {{ pagination.totalElements }})
            </span>
            <div class="pagination-controls">
                <button class="page-btn" :disabled="pagination.currentPage === 0"
                    @click="$emit('changePage', pagination.currentPage - 1)">
                    <IconComponents name="chevron-left" :size="16" />
                </button>
                <span class="page-current">{{ pagination.currentPage + 1 }}</span>
                <button class="page-btn" :disabled="pagination.currentPage >= pagination.totalPages - 1"
                    @click="$emit('changePage', pagination.currentPage + 1)">
                    <IconComponents name="chevron-right" :size="16" />
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import IconComponents from '@/components/icons/IconComponents.vue'

defineProps(['results', 'pagination'])
defineEmits(['changePage'])

// --- HELPERS UI (Estilos) ---
const getAlertClass = (a) => ({ 'ALTA': 'bg-red', 'MEDIA': 'bg-yellow' }[a] || 'bg-green')
const getAlertColor = (a) => ({ 'ALTA': '#ef4444', 'MEDIA': '#f59e0b' }[a] || '#10b981')
const getInternetLabel = (v) => v === 'Fiber optic' ? '⚡ Fibra' : (v === 'DSL' ? '🌐 DSL' : '📵 Sin Net')
const getInternetClass = (v) => v === 'Fiber optic' ? 'tag-purple' : (v === 'DSL' ? 'tag-blue' : 'tag-gray')
</script>

<style scoped>
/* (Incluye aquí los estilos de tabla, celdas y paginación que ya definimos antes para que quede encapsulado) */
/* Copia los estilos de .data-table, .cell-group, .pagination-footer del paso anterior */
/* ... ESTILOS CSS AQUÍ ... */
.table-wrapper {
    display: flex;
    flex-direction: column;
    height: 100%;
}

.table-responsive {
    overflow-x: auto;
    flex-grow: 1;
}

.data-table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    font-size: 0.85rem;
}

.data-table th {
    background: var(--bg-white);
    padding: 12px 16px;
    text-align: left;
    font-weight: 600;
    color: var(--text-secondary);
    text-transform: uppercase;
    font-size: 0.75rem;
    border-bottom: 1px solid var(--border-color);
    white-space: nowrap;
}

.data-table td {
    padding: 14px 16px;
    border-bottom: 1px solid var(--border-color);
    vertical-align: top;
    color: var(--text-primary);
}

.data-table tr:hover td {
    background: var(--bg-lighter);
}

/* Celdas */
.cell-group {
    display: flex;
    flex-direction: column;
    gap: 3px;
}

.font-bold {
    font-weight: 600;
}

.text-primary {
    color: var(--primary-color);
}

.text-muted {
    color: var(--text-secondary);
    font-size: 0.8rem;
}

.badge-pill {
    background: var(--bg-lighter);
    border: 1px solid var(--border-color);
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 0.7rem;
    color: var(--text-secondary);
    width: fit-content;
}

.icon-row {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 0.8rem;
    color: var(--text-primary);
}

.truncate {
    max-width: 160px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.tags-group {
    display: flex;
    flex-direction: column;
    gap: 4px;
    align-items: flex-start;
}

.tag {
    padding: 3px 8px;
    border-radius: 6px;
    font-size: 0.7rem;
    font-weight: 600;
}

.tag-purple {
    background: rgba(139, 92, 246, 0.1);
    color: #8b5cf6;
}

.tag-blue {
    background: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
}

.tag-green {
    background: rgba(16, 185, 129, 0.1);
    color: #10b981;
}

.tag-gray {
    background: rgba(107, 114, 128, 0.1);
    color: #6b7280;
}

.amount {
    font-family: 'Monaco', monospace;
    font-weight: 700;
}

/* Riesgo Container */
.risk-cell {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    /* Más espacio entre badge y barra */
    min-width: 90px;
    /* Asegura que no se aplaste */
}

.risk-badge {
    padding: 3px 10px;
    border-radius: 12px;
    font-size: 0.7rem;
    font-weight: 700;
    letter-spacing: 0.5px;
    display: inline-block;
    min-width: 60px;
    /* Ancho mínimo para que se vea uniforme */
    text-align: center;
}

/* Barra de Probabilidad */
.prob-wrapper {
    display: flex;
    align-items: center;
    gap: 8px;
    /* Espacio entre barra y texto */
    width: 100%;
    justify-content: center;
}

.prob-track {
    width: 50px;
    /* Ancho fijo de la barra */
    height: 6px;
    /* Un poco más alta */
    background: var(--border-color);
    border-radius: 3px;
    overflow: hidden;
    position: relative;
    /* Para posicionamiento */
}

.prob-fill {
    height: 100%;
    border-radius: 3px;
    transition: width 0.3s ease;
    /* Animación suave al cargar */
}

.prob-text {
    font-size: 0.75rem;
    color: var(--text-secondary);
    font-weight: 600;
    min-width: 30px;
    /* Para que los números no muevan el layout */
    text-align: right;
    font-family: 'Monaco', monospace;
    /* Alineación numérica perfecta */
}

/* Colores de Alerta (Asegurar que estén definidos) */
.bg-red {
    background: #fee2e2;
    color: #991b1b;
    border: 1px solid #fecaca;
}

.bg-yellow {
    background: #fef3c7;
    color: #92400e;
    border: 1px solid #fde68a;
}

.bg-green {
    background: #dcfce7;
    color: #166534;
    border: 1px solid #bbf7d0;
}


.status-pill {
    display: inline-block;
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 0.75rem;
    font-weight: 600;
}

.status-ok {
    background: rgba(16, 185, 129, 0.1);
    color: #10b981;
}

.status-bad {
    background: rgba(239, 68, 68, 0.1);
    color: #ef4444;
}

/* Paginación */
.pagination-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    border-top: 1px solid var(--border-color);
    background: var(--bg-white);
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

.page-btn {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid var(--border-color);
    border-radius: 6px;
    background: var(--bg-white);
    cursor: pointer;
    color: var(--text-primary);
}

.page-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.page-btn:not(:disabled):hover {
    background: var(--bg-lighter);
    color: var(--primary-color);
}

.page-current {
    font-weight: 700;
    font-size: 0.9rem;
}

.empty-state {
    padding: 60px;
    text-align: center;
    color: var(--text-secondary);
}

.icon-empty {
    color: var(--border-color);
    margin-bottom: 5px;
    opacity: 0.5;
}

/* Dark Mode */
[data-theme='dark'] .data-table th,
[data-theme='dark'] .pagination-footer {
    background: var(--bg-lighter);
    border-color: var(--border-color);
}

[data-theme='dark'] .data-table td {
    border-color: var(--border-color);
}

[data-theme='dark'] .prob-track {
    background: rgba(255, 255, 255, 0.1);
}

[data-theme='dark'] .page-btn {
    background: var(--bg-card);
    border-color: var(--border-color);
    color: var(--text-primary);
}
</style>