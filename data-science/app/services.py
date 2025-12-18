import joblib
import pandas as pd
import os

class ChurnModelService:
    def __init__(self):
        self.model = None
        self._load_model()

    def _load_model(self):
        # Ruta dinámica para encontrar el archivo dentro o fuera de Docker
        model_path = "modelo_churn.joblib"
        try:
            self.model = joblib.load(model_path)
            print(f"✅ Modelo cargado desde: {model_path}")
        except Exception as e:
            print(f"❌ Error fatal cargando modelo: {e}")
            self.model = None

    def predict(self, data: dict) -> dict:
        if not self.model:
            raise Exception("El modelo no está disponible.")

        # Transformación de datos (Feature Engineering simple)
        input_df = pd.DataFrame([{
            "tenure": data.antiguedad,
            "Contract": data.contrato,
            "MonthlyCharges": data.cargos_mensuales,
            "TechSupport": data.soporte_tecnico,
            "InternetService": data.servicio_internet,
            "PaymentMethod": data.metodo_pago
        }])

        prediction = self.model.predict(input_df)[0]
        probability = self.model.predict_proba(input_df)[0][1]

        return {
            "prevision": "Va a cancelar" if prediction == 1 else "Se queda",
            "probabilidad": round(float(probability), 2),
            "alerta": "ALTA" if probability > 0.7 else "BAJA"
        }

# Instancia global para no recargar el modelo en cada petición
churn_service = ChurnModelService()