import { createPinia } from 'pinia'

// Instancia de Pinia compartida entre la app y el router para poder usarla fuera de componentes
export const pinia = createPinia()