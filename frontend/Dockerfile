FROM node:20-alpine

WORKDIR /app

# 1. Instalamos pnpm globalmente
RUN npm install -g pnpm

# 2. Copiamos los archivos de dependencias (OJO: Necesitas tener el pnpm-lock.yaml)
COPY package.json pnpm-lock.yaml ./

# 3. Instalamos dependencias usando pnpm
# --frozen-lockfile asegura que se instalen las versiones exactas del lockfile
RUN pnpm install --frozen-lockfile

COPY . .

EXPOSE 5173

# Comando por defecto (se sobreescribe en el docker-compose)
CMD ["pnpm", "run", "dev", "--host"]