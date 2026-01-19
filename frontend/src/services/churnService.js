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
    // ✅ CORRECCIÓN: Esperamos la respuesta y devolvemos solo .data
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
    
    const response = await api.post('/predictions/batch', formData);
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
 * Descargar template Excel
 */
export async function downloadTemplateExcelRequest() {
    return api.get('/predictions/template/excel', {
        responseType: 'blob'
    });
}

/**
 * Descargar template CSV
 */
export async function downloadTemplateCSVRequest() {
    return api.get('/predictions/template/csv', {
        responseType: 'blob'
    });
}

// Export por defecto (opcional, por si acaso)
export default {
    predecirChurnRequest,
    uploadBatchFileRequest,
    getJobStatusRequest,
    downloadTemplateExcelRequest,
    downloadTemplateCSVRequest
};