import api from './api';

export default {
    async predecirChurn(datosCliente) {
        try {
            // Axios devuelve un objeto grande { data, status, headers... }
            // Nosotros solo queremos 'data' que es el JSON de tu Spring Boot
            const response = await api.post('/predictions', datosCliente);
            return response.data; 
        } catch (error) {
            // Axios mete la respuesta del servidor en 'error.response'
            if (error.response && error.response.data) {
                // Aquí atrapamos tu JSON de error (con validationErrors)
                // y se lo lanzamos al componente para que lo pinte en rojo
                throw error.response.data; 
            }
            // Si el servidor está muerto o no hay internet
            throw new Error("No se pudo conectar con el servidor.");
        }
    }
};