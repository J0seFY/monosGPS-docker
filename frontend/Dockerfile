# Etapa 1: Construcción de la aplicación Angular
FROM node:18 AS builder

# Definimos la carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de dependencias de Node.js
COPY frontend/package*.json ./

# Instalamos las dependencias de Angular
RUN npm install

# Copiamos el resto del código fuente de Angular
COPY frontend/src ./src

# Construimos la aplicación Angular (esto generará la carpeta dist/)
RUN npm run build --prod --output-path=dist

# Etapa 2: Servir la aplicación con Nginx
FROM nginx:alpine

# Copiamos los archivos generados en la etapa de construcción a Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Exponemos el puerto 80 para que Nginx pueda servir la aplicación
EXPOSE 80
