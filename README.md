# PlateUp

Red social de recetas de cocina. Permite a los usuarios publicar recetas, seguir a otros cocineros, guardar favoritos en colecciones, obtener logros y participar en retos culinarios.

## Stack

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 3.x · Java 17 · JPA/Hibernate · JWT |
| Base de datos | MySQL 8 |
| Frontend web | Vue 3 · Vite · Pinia · Vue Router |
| Móvil | Capacitor (Android) |
| Despliegue | Railway (backend + frontend) · Docker |

## Estructura del proyecto

```
PlateUp/
├── plateup/                  # Backend Spring Boot
│   ├── src/main/java/        # Código fuente Java
│   └── Dockerfile
├── Vue/plateup-frontend/     # Frontend Vue 3
│   ├── src/                  # Componentes y lógica
│   ├── android/              # App Android (Capacitor)
│   └── Dockerfile
├── db/                       # Scripts SQL
├── docs/                     # Documentación y capturas
├── assets/                   # Logos e imágenes
└── docker-compose.yml        # Entorno local completo
```

## Ejecución local con Docker

```bash
# 1. Copiar y configurar variables de entorno
cp .env.example .env
# Editar .env con tus valores

# 2. Levantar todos los servicios
docker compose up --build
```

- Backend: http://localhost:8080
- Frontend: http://localhost:80
- API docs (Swagger): http://localhost:8080/swagger-ui.html

## Ejecución sin Docker

**Backend:**
```bash
cd plateup
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd Vue/plateup-frontend
npm install
npm run dev
```

## Base de datos

Los scripts SQL están en `db/`. El archivo `Pruebas.sql` se usa como inicialización en Docker Compose e incluye datos de ejemplo.

## Despliegue

El proyecto está desplegado en Railway. Cada directorio (`plateup/` y `Vue/plateup-frontend/`) contiene su propio `railway.toml` con la configuración de build.
