FROM python:3.13-slim

WORKDIR /app

# Instalar dependencias del sistema si fueran necesarias
# RUN apt-get update && apt-get install -y gcc

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

# Ejecutar FastAPI con recarga automática (--reload)
CMD ["fastapi", "run", "app/main.py", "--port", "8000", "--host", "0.0.0.0"]