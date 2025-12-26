import joblib
import pandas as pd
import os
# Corregimos el import: en schemas se llama ChurnInput, no ChurnRequest
from app.schemas import ChurnInput 

class ChurnModelService:
    def __init__(self):
        self.model = None
        self.feature_columns = None
        self._load_artifacts()

    def _load_artifacts(self):
        try:
            # 1. Encontrar rutas (Basado en tu imagen, esto es correcto)
            current_dir = os.path.dirname(os.path.abspath(__file__)) # /app
            base_dir = os.path.dirname(current_dir) # /data-science
            
            model_path = os.path.join(base_dir, "modelo_churn.joblib")
            pipeline_path = os.path.join(base_dir, "preprocessing_pipeline.joblib")

            # 2. Cargar Modelo
            self.model = joblib.load(model_path)
            print(f"✅ Modelo cargado: {model_path}")

            # 3. Cargar Pipeline (Columnas)
            # Esto es vital para saber el orden exacto que espera el modelo
            pipeline_data = joblib.load(pipeline_path)
            self.feature_columns = pipeline_data['feature_columns']
            print(f"✅ Pipeline cargado. Columnas esperadas: {len(self.feature_columns)}")
            
        except FileNotFoundError as e:
            print(f"❌ ERROR ARCHIVO NO ENCONTRADO: {e}")
        except Exception as e:
            print(f"❌ Error fatal cargando artefactos: {e}")

    def predict(self, data: ChurnInput) -> dict:
        if not self.model or not self.feature_columns:
            raise Exception("El modelo o el pipeline no están cargados.")

        # 1. Crear DataFrame con datos crudos
        raw_data = {
            "antiguedad": [data.antiguedad],
            "contrato": [data.contrato],           # Ej: "Month-to-month"
            "cargos_mensuales": [data.cargos_mensuales],
            "soporte_tecnico": [data.soporte_tecnico],
            "servicio_internet": [data.servicio_internet],
            "metodo_pago": [data.metodo_pago]
        }
        input_df = pd.DataFrame(raw_data)

        # 2. PREPROCESAMIENTO (One-Hot Encoding)
        # Convertimos texto a variables dummy (0s y 1s)
        input_encoded = pd.get_dummies(input_df)

        # 3. ALINEACIÓN (Reindex)
        # El modelo espera columnas específicas (ej: contrato_Two year).
        # Si el input actual no tiene esa columna (porque el cliente es Month-to-month),
        # reindex la crea y pone 0. Si sobra alguna que el modelo no conoce, la quita.
        input_final = input_encoded.reindex(columns=self.feature_columns, fill_value=0)

        # 4. Predicción
        prediction = self.model.predict(input_final)[0]
        probability = self.model.predict_proba(input_final)[0][1]

        return {
            "prevision": "Va a cancelar" if prediction == 1 else "Se queda",
            "probabilidad": round(float(probability), 2),
            "alerta": "ALTA" if probability > 0.7 else "BAJA"
        }

# Instancia global para no recargar el modelo en cada petición
churn_service = ChurnModelService()