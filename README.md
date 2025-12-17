# 🔮 ChurnInsight - Predicción de Cancelación de Clientes

> **Hackathon Project:** Solución integral de IA para predecir la deserción de clientes (Churn) en servicios de suscripción.

## 🏗️ Arquitectura del Sistema
El proyecto sigue una arquitectura de microservicios en un **Monorepo**:

* **Frontend:** Vue.js 3 (Interfaz de usuario).
* **Backend:** Java 21 (Spring Boot 3) + PostgreSQL (API REST y Lógica).
* **Data Science:** Python 3.13 + FastAPI + Scikit-Learn (Modelo de Machine Learning).
* **Infraestructura:** Docker Compose (Orquestación completa).

---

## 🚀 Guía de Inicio Rápido (Para Desarrolladores)

### Prerrequisitos
* **Docker Desktop** (Corriendo).
* **Java 21 JDK** (Para Backend).
* **Python 3.13** (Para Data Science).
* **Node.js 20+** (Para Frontend).

### Opción A: "Modo Dios" (Recomendado para Desarrollo)
*Combina la velocidad de tu PC con la facilidad de Docker para la Base de Datos.*

1.  **Levanta la Base de Datos y la IA:**
    ```bash
    docker-compose up -d db ai-service
    ```

2.  **Inicia el Backend (Java):**
    * Abre una terminal en `/backend`.
    * Ejecuta: `.\mvnw spring-boot:run`
    * *Nota: Se conectará automáticamente a la BD en Docker.*

3.  **Inicia el Frontend (Vue):**
    * Abre una terminal en `/frontend`.
    * Ejecuta: `pnpm install` y luego `pnpm dev`.

---

### Opción B: "Modo Demo" (Todo en Docker)
*Ideal para presentar el proyecto final sin instalar nada.*

1.  En la raíz del proyecto, ejecuta:
    ```bash
    docker-compose up --build
    ```
2.  Espera a que todos los servicios arranquen.

---

## 📡 Endpoints Principales

| Servicio | URL Local | Descripción |
| :--- | :--- | :--- |
| **Frontend** | `http://localhost:5173` | Formulario de predicción. |
| **Backend API** | `http://localhost:8080/api/health` | Verificar estado del sistema. |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` | Documentación automática (Próximamente). |
| **IA Docs** | `http://localhost:8000/docs` | Probador del modelo de Python. |

## 📂 Estructura del Proyecto

```text
/churn-insight
│
├── /backend          # Spring Boot (Java)
├── /frontend         # Vue.js (JavaScript)
├── /data-science     # FastAPI (Python)
└── docker-compose.yml