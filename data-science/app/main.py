from fastapi import FastAPI, HTTPException
from app.schemas import ChurnInput, ChurnOutput
from app.services import churn_service

app = FastAPI(title="ChurnInsight AI API", version="1.0.0")

@app.post("/predict", response_model=ChurnOutput)
def predict_churn(client_data: ChurnInput):
    try:
        # Delegamos la lógica al servicio
        result = churn_service.predict(client_data)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
def health_check():
    return {"status": "ok", "model_loaded": churn_service.model is not None}