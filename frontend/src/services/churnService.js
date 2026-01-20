/**
 * Churn Service
 * Maneja las peticiones de predicción y carga masiva
 */
import api from '@/services/api';

/**
 * Predecir Churn (Individual)
 * POST /api/v1/predictions
 */
export async function predecirChurnRequest(datosCliente) {
    const response = await api.post('/predictions', datosCliente);
    return response.data;
}

/**
 * Carga masiva de predicciones
 * POST /api/v1/predictions/batch
 */
export async function uploadBatchFileRequest(file) {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await api.post('/predictions/batch', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
}

/**
 * Obtener estado de procesamiento
 * GET /api/v1/predictions/batch/status/{jobId}
 */
export async function getJobStatusRequest(jobId) {
    const response = await api.get(`/predictions/batch/status/${jobId}`);
    return response.data;
}

/**
 * Obtener resultados detallados (Paginado + Filtros)
 * GET /api/v1/predictions/batch/results/{jobId}
 */
export async function getBatchResultsRequest(jobId, page = 0, size = 10, search = '', alerta = '') {
    const params = { page, size };

    // Solo enviamos si tienen valor
    if (search) params.search = search;
    if (alerta && alerta !== 'ALL') params.alerta = alerta;

    const response = await api.get(`/predictions/batch/results/${jobId}`, { params });
    return response.data; // Devuelve objeto Page { content: [], totalPages: ... }
}

/**
 * Exportar resultados en Excel
 */
export async function exportBatchResultsExcelRequest(jobId) {
    const response = await api.get(`/predictions/batch/results/${jobId}/export`, {
        responseType: 'blob'
    });
    return response.data;
}

/**
 * Obtener historial paginado CON FILTROS
 * GET /api/v1/predictions/batch/history
 */
export async function getBatchHistoryRequest(page = 0, size = 10, filters = {}) {
    const params = { page, size };

    // Solo añadimos a params si hay valor y no es 'all'
    if (filters.status && filters.status !== 'all') params.status = filters.status;
    if (filters.format && filters.format !== 'all') params.format = filters.format;
    if (filters.dateFrom) params.dateFrom = filters.dateFrom;
    if (filters.dateTo) params.dateTo = filters.dateTo;

    const response = await api.get('/predictions/batch/history', { params });
    // Devolvemos toda la data (content, pageable, totalElements...)
    return response.data; 
}

/**
 * Descargar templates
 */
export async function downloadTemplateExcelRequest() {
    const response = await api.get('/predictions/template/excel', { responseType: 'blob' });
    return response.data;
}

export async function downloadTemplateCSVRequest() {
    const response = await api.get('/predictions/template/csv', { responseType: 'blob' });
    return response.data;
}

export default {
    predecirChurnRequest,
    uploadBatchFileRequest,
    getJobStatusRequest,
    getBatchResultsRequest, 
    exportBatchResultsExcelRequest,
    getBatchHistoryRequest,
    downloadTemplateExcelRequest,
    downloadTemplateCSVRequest
};