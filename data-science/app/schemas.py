from pydantic import BaseModel
from typing import List, Literal


class ChurnRequest(BaseModel):
    antiguedad: int
    contrato: Literal["Month-to-month", "One year", "Two year"]
    cargos_mensuales: float
    soporte_tecnico: Literal["Yes", "No", "No internet service"]
    servicio_internet: Literal["DSL", "Fiber optic","No"]
    metodo_pago: Literal["Electronic check", "Mailed check", "Bank transfer"]

class ChurnResponse(BaseModel):
    prevision: str
    probabilidad: float
    alerta: str