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
* **Modelo IA:** Asegúrate de tener el archivo `modelo_churn.joblib` en la carpeta `data-science/`.
* *(Opcional para desarrollo local)*: Java 21 JDK, Python 3.13, Node.js 20+.

### ⚙️ 0. Configuración Inicial
Antes de arrancar, crea el archivo de variables de entorno en la raíz:

1. Crea un archivo llamado `.env` en la raíz del proyecto (`/churn-insight/.env`).
2. Copia el contenido del ejemplo `.env.example`.

Lo mismo en la carpeta `frontend/`.

### 🛠️ Opción A: Desarrollo Híbrido (Recomendado)
*Usamos Docker solo para la Base de Datos y la IA, mientras corres Java y Vue en tu PC para mayor velocidad.*

1.  **Levanta la Infraestructura (BD + IA):**
    ```bash
    docker-compose up -d db ai-service
    ```

2.  **Inicia el Backend (Java):**
    * Entra a la carpeta: `cd backend`
    * Ejecuta (Windows) (Opcional - para limpiar, compilar, probar): `.\mvnw clean install` o `.\mvnw clean install -DskipTests`
    * Ejecuta (Windows): `.\mvnw spring-boot:run`
    * Ejecuta (Mac/Linux): `./mvnw spring-boot:run`
    * 🔗*Swagger/API:* http://localhost:8080/api/health
    * 🔗*Doc Api:* http://localhost:8080/swagger-ui/index.html

3.  **Inicia el Frontend (Vue):**
    * Entra a la carpeta: `cd frontend`
    * Instala: `pnpm install` (o npm install)
    * Ejecuta: `pnpm dev`
    * 🔗*Web:* http://localhost:5173


    <img width="1919" height="905" alt="Captura de pantalla 2025-12-26 181545" src="https://github.com/user-attachments/assets/23ed5ad1-7a80-4489-a42b-47549bc5420a" />

    <img width="1919" height="908" alt="Captura de pantalla 2025-12-26 181641" src="https://github.com/user-attachments/assets/90e99641-d68b-42ac-a54b-ae5225ad3617" />


4.  **🐍 Desarrollo en DataScience (FastAPI) - Opcional:** (Solo realiza este paso si necesitas modificar el código de Python/IA. Si no, usa el contenedor del paso 1).
    * Entra a la carpeta: `cd data-science`
    * **Activa el entorno virtual:**
        * Windows: `.\venv\Scripts\Activate`
        * Mac/Linux: `source venv/bin/activate`

    * **Desactivar (Al final)**
        * `deactivate`

    * Instala las librerías (Solo si no lo has hecho antes): `pip install -r requirements.txt`
    * 🛑 IMPORTANTE: Detén el contenedor de IA para liberar el puerto 8000:
        
        ```bash
        docker stop churn_ai_service
        ```

    * Levanta el servidor localmente:

        ```bash
        uvicorn app.main:app --reload
        ```

        * app.main: Busca el archivo main.py dentro de la carpeta app.

        * :app: Es el nombre de la variable app = FastAPI(...).

        * --reload: Reinicio automático al guardar cambios.

    * *Web:* http://127.0.0.1:8000/docs

---

### 🐳 Opción B: Todo en Docker (Modo Demo)
*Ideal para validar que todo funciona integrado sin instalar nada en tu PC.*

1.  En la raíz del proyecto, ejecuta:
    ```bash
    docker-compose up --build -d
    ```
2.  Espera a que termine el build.

3. Accede a la web en: http://localhost:5173

---

### ☁️ Despliegue en Producción (Servidor)

*Usa esta opción en un servidor/nube. Utiliza Nginx como servidor web en el puerto 80.*

```bash
    docker-compose -f docker-compose.prod.yml up -d --build
```

Web: http://dominio.com (o la IP del servidor)

Si lo ejecutas en tu computadora http://localhost/


---

## 📂 Estructura del Proyecto

```text
/churn-insight
│
├── .env (.env.example)      # 🔑 Variables de Entorno (Puertos, BD, Claves)
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
│               │   └── /v1        # Versionado físico
│               │       └── PredictionController.java
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
│       ├── __init__.py      #  Archivo vacío (necesario para paquetes Python)
│       ├── main.py          # 🚪 Controlador: Define rutas (@app.post("/predict"))
│       ├── schemas.py       # 📝 Contrato: Valida el JSON de entrada con Pydantic
│       └── services.py      # 🧠 Servicio: Carga el .joblib y ejecuta la predicción
│
└── /frontend                # 🎨 Vue.js 3 + Vite (Interfaz de Usuario)
    ├── .env                 # Variables para modo local (VITE_API_URL)
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

```

---

## 👥 Equipo de Desarrollo

### Grupo 05 - Hackathon NoCountry 2025

| Rol | Área | Responsabilidades |
| :--- | :--- | :--- |
| **Frontend Developer** | Vue.js 3 + Vite | Interfaz de usuario, componentes, temas y responsive design |
| **Backend Developer** | Java Spring Boot | API REST, lógica de negocio, integración con servicios |
| **Data Scientist** | Python + FastAPI - Google Colab | Modelo ML, pipeline de datos, predicciones, .joblib |
| **DevOps** | Docker & Deployment | Orquestación de contenedores, CI/CD, configuración |

---

## 📜 Licencia

Este proyecto fue desarrollado como parte del **Hackathon NoCountry**.

---

**Hecho con ❤️ por Grupo 05 - Hackathon NoCountry Diciembre 2025 - Enero 2026**