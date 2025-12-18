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
├── .gitignore               # 🛡️ Global: Ignora basura (node_modules, venv, target, .idea)
├── README.md                # 📄 Documentación oficial para desarrolladores y jueces
├── docker-compose.yml       # 🐙 Orquestador: Levanta BD + Backend + Frontend + AI juntos
│
├── /backend                 # ☕ Spring Boot (Lógica de Negocio & API Principal)
│   ├── .dockerignore        # Ignora target/ y mvnw para builds rápidos
│   ├── Dockerfile           # Imagen: Eclipse Temurin (Java 21)
│   ├── mvnw & mvnw.cmd      # Maven Wrapper (para compilar sin instalar Maven)
│   ├── pom.xml              # Dependencias: Spring Web, JPA, Postgres, DevTools
│   └── src
│       └── main
│           ├── resources
│           │   └── application.properties # ⚙️ Config: URL de Base de Datos y Credenciales
│           └── java/com/churninsight/backend
│               ├── BackendApplication.java # Punto de inicio (Main)
│               ├── controller     # 🕹️ API REST: Recibe peticiones HTTP del Frontend
│               │   └── HealthController.java
│               ├── model          # 📦 Entidades: Tablas de Base de Datos (User, Prediction)
│               ├── repository     # 🗄️ Repositorios: Consultas SQL automáticas (JPA)
│               ├── service        # 🧠 Lógica: Conecta con /data-science y guarda en BD
│               └── dto            # 📨 DTOs: JSONs de entrada y salida (Contrato)
│
├── /data-science            # 🐍 Python FastAPI (Microservicio de IA)
│   ├── .dockerignore        # Ignora venv/ y __pycache__ (Vital para Docker)
│   ├── Dockerfile           # Imagen: Python 3.13 Slim
│   ├── requirements.txt     # Librerías: fastapi, uvicorn, scikit-learn, pandas, joblib
│   ├── modelo_churn.joblib  # 🧠 EL CEREBRO: Archivo del modelo entrenado (ej: RandomForest)
│   │                        # (Este archivo se descarga de Colab y se pega aquí)
│   └── /app                 # 📂 Código Modular (Arquitectura Limpia)
│       ├── __init__.py      # Archivo vacío (necesario para paquetes Python)
│       ├── main.py          # 🚪 Controlador: Define rutas (@app.post("/predict"))
│       ├── schemas.py       # 📝 Contrato: Valida el JSON de entrada con Pydantic
│       └── services.py      # 🧠 Servicio: Carga el .joblib y ejecuta la predicción
│
└── /frontend                # 🎨 Vue.js 3 + Vite (Interfaz de Usuario)
    ├── .dockerignore        # Ignora node_modules/
    ├── Dockerfile           # Imagen: Node 20 Alpine
    ├── package.json         # Dependencias: Vue, Axios
    ├── vite.config.js       # Configuración del servidor de desarrollo
    ├── index.html           # HTML base
    └── src
        ├── main.js          # Punto de entrada JS
        ├── App.vue          # Componente Padre
        ├── services         # 🌐 API Client: Configuración de Axios para llamar al Backend
        ├── components       # 🧩 Piezas: Botones, Inputs, Alertas, Spinner
        └── views            # 📺 Pantallas: HomeView (Formulario), DashboardView