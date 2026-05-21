# PlateUp — Frontend

Frontend de PlateUp desarrollado con **Vue 3 + Vite**. Incluye versión web y móvil (Android) mediante Capacitor.

## Stack

- Vue 3 (Composition API)
- Vite
- Pinia (estado global)
- Vue Router
- Capacitor (build Android)
- Nginx (producción)

## Desarrollo local

```bash
npm install
npm run dev
```

La app conecta con el backend en `http://localhost:8080/api` por defecto (configurable en `.env`).

## Build producción

```bash
npm run build
```

## Build Android

```bash
npx cap sync android
npx cap open android
```

## Variables de entorno

| Variable | Descripción |
|---|---|
| `VITE_API_URL` | URL base de la API del backend |
