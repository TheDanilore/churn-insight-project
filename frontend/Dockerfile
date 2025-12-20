# --- ETAPA 1: Construcción (Build) ---
FROM node:20-alpine as build-stage

WORKDIR /app

# Copiamos dependencias primero para aprovechar caché de Docker
COPY package*.json ./

# Instalamos dependencias
RUN npm install

# Copiamos el código fuente
COPY . .

# Construimos la app (Crea la carpeta /dist)
RUN npm run build

# --- ETAPA 2: Producción (Nginx) ---
FROM nginx:alpine as production-stage

# Copiamos SOLO los archivos construidos (HTML/CSS/JS) de la etapa anterior
COPY --from=build-stage /app/dist /usr/share/nginx/html

# (Opcional) Copiar configuración personalizada de Nginx si usas Vue Router
# COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]