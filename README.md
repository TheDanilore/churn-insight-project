# 🔮 ChurnInsight - Predicción de Churn

> **Hackathon Project:** Sistema integral para predecir la cancelación de clientes mediante Machine Learning.

## 🏗️ Arquitectura del Monorepo

Este proyecto integra tres tecnologías principales orquestadas con Docker:

| Módulo | Tecnología | Puerto Local | Descripción |
| :--- | :--- | :--- | :--- |
| **Frontend** | Vue.js 3 + Vite | `5173` | Panel de control para usuarios. |
| **Backend** | Java 21 (Spring Boot) | `8080` | API REST y lógica de negocio. |
| **Data Science** | Python 3.13 (FastAPI) | `8000` | Microservicio de Machine Learning. |
| **Database** | PostgreSQL 15 | `5432` | Persistencia de datos. |

---

## 🚀 Guía de Inicio para Desarrolladores

### Prerrequisitos
* **Docker Desktop** (Debe estar corriendo).
* **Git**.
* *(Opcional para desarrollo local)*: Java 21 JDK, Python 3.13, Node.js 20+.

### 🛠️ Opción A: Desarrollo Híbrido (Recomendado)
*Usamos Docker solo para la Base de Datos y la IA, mientras corres Java y Vue en tu PC para mayor velocidad.*

1.  **Levanta la Infraestructura (BD + IA):**
    ```bash
    docker-compose up -d db ai-service
    ```

2.  **Inicia el Backend (Java):**
    * Entra a la carpeta: `cd backend`
    * Ejecuta: `.\mvnw spring-boot:run`
    * *Swagger/API:* http://localhost:8080/api/health

3.  **Inicia el Frontend (Vue):**
    * Entra a la carpeta: `cd frontend`
    * Instala: `pnpm install` (o npm install)
    * Ejecuta: `pnpm dev`
    * *Web:* http://localhost:5173

---

### 🐳 Opción B: Todo en Docker (Modo Demo)
*Ideal para validar que todo funciona junto sin instalar nada extra.*

1.  En la raíz del proyecto, ejecuta:
    ```bash
    docker-compose up --build
    ```
2.  Espera a que los logs se estabilicen.

---

## 📂 Estructura del Proyecto

```text
/churn-insight
│
├── /backend          # Spring Boot (Lógica de Negocio)
│   ├── src/main/java # Código Java
│   └── pom.xml       # Dependencias Maven
│
├── /frontend         # Vue.js (Interfaz de Usuario)
│   └── src/          # Componentes y Vistas
│
├── /data-science     # Python (Inteligencia Artificial)
│   ├── main.py       # Endpoint FastAPI
│   └── requirements.txt
│
├── docker-compose.yml # Orquestador de servicios
└── README.md          # Esta documentación