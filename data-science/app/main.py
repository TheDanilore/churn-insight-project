import pandas as pd
from fastapi import FastAPI, HTTPException
from typing import List
from app.schemas import ChurnRequest, ChurnResponse
from app.services import churn_service

app = FastAPI(title="ChurnInsight AI API", version="1.0.0")

model = load_model()

def _predict_dataframe(df:pd.DataFrame) -> List[ChurnResponse]:
    """
        Función interna reutilizable para predecir uno o varios clientes.
    """
    predictions = model.predict(df)
    probabilities = model.predict_proba(df)

    results = []
    for pred, prob in zip(predictions, probabilities):
        prob_churn=prob[1]

        label = "Va a Cancelar" if pred == 1 else "Se queda"
        alerta = "Alta" if prob_churn > 0.7 else "Baja"

        results.append(
         ChurnResponse(
            prevision=label,
            probabilidad=round(prob_churn, 4),
            alerta=alerta,
        ))

       return results

       @app.post("/predict/batch", response_model=List[ChurnResponse])
       def predict_batch(clients: List[ChurnRequest]):
           if not clients:
               raise HTTPException(
               status code=400,
               detail="La lista no puede estar vacia"
               )
       try:
            # 1️ Pydantic → dict → DataFrame
           data = [client.model_dump() for client in clients]
           df = pd.DataFrame(data)

           # 2️ Renombrar columnas
                   df.rename(columns={
                       "antiguedad": "tenure",
                       "cargos_mensuales": "MonthlyCharges",
                       "contrato": "Contract",
                       "soporte_tecnico": "TechSupport",
                       "servicio_internet": "InternetService",
                       "metodo_pago": "PaymentMethod"
                   }, inplace=True)

           # 3️ Predicción
           return _predict_dataframe(df)

       except Exception as e:
           # Si falla algo en la matriz (ej: columna faltante), devolvemos error 500

           raise HTTPException(
           status_code=500,
           detail=f"Error procesando el lote: {str(e)}"
           )


@app.get("/health")
def health_check():
    return {
        "status": "ok",
        "model_loaded": churn_service.model is not None,
        "pipeline_loaded": churn_service.feature_columns is not None
    }