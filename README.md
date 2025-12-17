# Churn Insight - Proyecto Integrado (Vue.js + Spring Boot + FastAPI)

Aplicación para predicción de churn de clientes usando un modelo de Machine Learning con arquitectura de microservicios.

## Estructura del Proyecto

```
/churn-insight-project
│
├── /backend          (API REST - Java Spring Boot)
├── /frontend         (UI - Vue.js 3)
├── /data-science     (Modelo ML - Python FastAPI + Notebooks)
│
├── .gitignore        (Archivos a ignorar en Git)
├── README.md         (Este archivo)
└── docker-compose.yml (Orquestación de contenedores)
```

## Flujo de Comunicación

```
Vue.js (Front) 
    ↓ POST /predict (datos cliente)
Spring Boot (Back) 
    ↓ Valida entrada → Invoca FastAPI
FastAPI (Python) 
    ↓ Carga modelo + calcula predicción
Spring Boot (Back) 
    ↓ Retorna resultado final
Vue.js (Front) 
    ↓ Muestra al usuario
```

## Requisitos Previos

- Node.js ^20.19.0 o >=22.12.0
- pnpm (última versión)
- Java 17+
- Python 3.9+
- Docker y Docker Compose (opcional, para contenerización)
- PostgreSQL 15+ (opcional, para persistencia)

## Instalación y Configuración

### 1. Frontend (Vue.js)

```bash
cd frontend
pnpm install
pnpm run dev
```

Accede a http://localhost:5173

### 2. Backend (Spring Boot)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

API disponible en http://localhost:8080

### 3. Data Science (FastAPI)

```bash
cd data-science
python -m venv venv
source venv/bin/activate  # En Windows: venv\Scripts\activate
pip install -r requirements.txt
fastapi dev main.py
```

API disponible en http://localhost:8000

## Endpoints Principales

### Backend Spring Boot

- `POST /api/predict` - Predice churn de un cliente
- `GET /api/stats` - Estadísticas de predicciones
- `POST /api/batch-predict` - Predicción en lote (CSV)

### FastAPI Python

- `POST /predict` - Carga modelo y calcula predicción
- `GET /health` - Verificar estado del servicio

## Ejemplos de Uso

### Predicción Individual

```json
POST /api/predict

{
  "customer_id": "C123",
  "contract_length_months": 12,
  "monthly_charges": 65.50,
  "total_charges": 786.0,
  "internet_service": "Fiber optic",
  "online_security": "Yes",
  "payment_method": "Electronic check",
  "tenure_months": 2,
  "monthly_login_frequency": 8
}
```

Respuesta:

```json
{
  "prediction": "Va a cancelar",
  "probability": 0.76,
  "confidence": 0.95,
  "top_features": [
    "tenure_months",
    "payment_method",
    "monthly_charges"
  ]
}
```

## Dependencias y Versiones

### Frontend
- Vue.js 3.x
- Vite (build tool)
- TypeScript
- ESLint, Prettier

### Backend
- Spring Boot 3.3.x
- Spring Web (REST)
- Spring Data JPA
- Maven 3.6.3+

### Data Science
- Python 3.9+
- FastAPI
- Uvicorn
- scikit-learn
- pandas
- numpy
- joblib

## Documentación Adicional

- [Frontend Setup](./frontend/README.md)
- [Backend Setup](./backend/README.md)
- [Data Science Setup](./data-science/README.md)

## Desarrollo Local con Docker

```bash
docker-compose up -d
```

Todos los servicios se levantarán automáticamente.

## Equipo

- **Data Science**: Análisis, modelado y serialización
- **Backend**: API REST, validación e integración
- **Frontend**: UI responsiva y consumo de APIs

---

**Hackathon No Country** - Diciembre 2025
