import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

interface Prediction {
  id: string
  customerId: string
  prediction: string
  probability: number
  confidence: number
  topFeatures: string[]
  timestamp: string
}

export const usePredictionStore = defineStore('prediction', () => {
  const predictions = ref<Prediction[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const totalPredictions = computed(() => predictions.value.length)
  const churnRate = computed(() => {
    if (predictions.value.length === 0) return 0
    const churnCount = predictions.value.filter(p => p.prediction === 'Va a cancelar').length
    return (churnCount / predictions.value.length) * 100
  })

  function addPrediction(prediction: Prediction) {
    predictions.value.push(prediction)
  }

  function clearPredictions() {
    predictions.value = []
  }

  return {
    predictions,
    loading,
    error,
    totalPredictions,
    churnRate,
    addPrediction,
    clearPredictions
  }
})
