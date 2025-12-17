import axios, { AxiosInstance } from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export interface ClientData {
  customerId: string
  contractLengthMonths: number
  monthlyCharges: number
  totalCharges: number
  internetService: string
  onlineSecurity: string
  paymentMethod: string
  tenureMonths: number
  monthlyLoginFrequency: number
}

export interface PredictionResponse {
  prediction: string
  probability: number
  confidence: number
  topFeatures: string[]
}

export const predictionService = {
  async predict(clientData: ClientData): Promise<PredictionResponse> {
    const response = await apiClient.post<PredictionResponse>('/predict', clientData)
    return response.data
  },

  async getStats() {
    const response = await apiClient.get('/stats')
    return response.data
  },

  async batchPredict(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    const response = await apiClient.post('/batch-predict', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  }
}

export default apiClient
