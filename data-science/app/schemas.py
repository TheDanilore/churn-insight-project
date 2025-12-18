from pydantic import BaseModel

# Input (Lo que manda Java)
class ChurnInput(BaseModel):
    antiguedad: int
    contrato: str
    cargos_mensuales: float
    soporte_tecnico: str
    servicio_internet: str
    metodo_pago: str

# Output (Lo que devolvemos)
class ChurnOutput(BaseModel):
    prevision: str
    probabilidad: float
    alerta: str