# Etapa de construcción: compilamos el frontend con Node y Vite
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# La URL de la API se pasa como argumento en tiempo de build para incrustarla en el bundle
ARG VITE_API_URL
RUN VITE_API_URL=${VITE_API_URL} npm run build

# Etapa de producción: servimos los archivos estáticos con Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Usamos nuestra propia configuración de Nginx para gestionar el routing de Vue Router
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
