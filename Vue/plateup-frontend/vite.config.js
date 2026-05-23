// Configuración de Vite para el frontend de PlateUp
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  // Plugins activos: soporte para Vue 3 y Tailwind CSS con integración nativa en Vite
  plugins: [vue(), tailwindcss()],
})