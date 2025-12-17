# Backend - Churn Insight API

API REST en Spring Boot para integración con el modelo de predicción de churn.

## Requisitos

- Java 17+
- Maven 3.6.3+

## Instalación

```bash
cd backend
mvn clean install
```

## Desarrollo

```bash
mvn spring-boot:run
```

API disponible en http://localhost:8080

## Endpoints

### POST /api/predict
Realiza la predicción de churn para un cliente.

**Request:**
```json
{
  "customerId": "C123",
  "contractLengthMonths": 12,
  "monthlyCharges": 65.50,
  "totalCharges": 786.0,
  "internetService": "Fiber optic",
  "onlineSecurity": "Yes",
  "paymentMethod": "Electronic check",
  "tenureMonths": 2,
  "monthlyLoginFrequency": 8
}
```

**Response:**
```json
{
  "prediction": "Va a cancelar",
  "probability": 0.76,
  "confidence": 0.95,
  "topFeatures": ["tenure_months", "payment_method", "monthly_charges"]
}
```

### GET /api/stats
Obtiene estadísticas de las predicciones realizadas.

**Response:**
```json
{
  "total_evaluados": 500,
  "tasa_churn": 0.23,
  "accuracy": 0.82,
  "precision": 0.79,
  "recall": 0.85,
  "f1_score": 0.82
}
```

### GET /api/health
Verifica el estado de la API.

## Base de Datos

Por defecto usa H2 en memoria. Para usar PostgreSQL, modifica `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/churn_db
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=churn_user
spring.datasource.password=churn_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/churninsight/
│   │   ├── config/          # Configuraciones
│   │   ├── controller/      # Controladores REST
│   │   ├── service/         # Lógica de negocio
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # Entidades JPA
│   │   └── repository/      # Acceso a datos
│   └── resources/
│       └── application.properties
└── test/                    # Tests
```

## Validación

Se valida automáticamente:
- Campos obligatorios
- Tipos de datos
- Valores positivos donde sea necesario

## CORS

Configurado para aceptar solicitudes desde:
- http://localhost:5173 (Vue.js)
- http://localhost:3000 (Desarrollo alternativo)
