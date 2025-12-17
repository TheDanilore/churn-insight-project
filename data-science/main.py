from fastapi import FastAPI
import pandas as pd

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "Microservicio de Data Science activo (Python 3.13)"}

@app.post("/predict")
def predict(data: dict):
    # Simulamos recibir datos
    return {
        "prediction": "Va a cancelar", 
        "probability": 0.85, 
        "status": "Simulacion Exitosa"
    }