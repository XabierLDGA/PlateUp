<template>
  <div
    class="min-h-screen bg-[var(--plateup-bg)]"
    :class="showBottomNav ? 'pb-[calc(4.5rem+env(safe-area-inset-bottom,0px))]' : 'pb-0'"
  >
    <router-view />
    <BottomNav v-if="showBottomNav" />
  </div>
</template>

<script setup>
// Componente raíz de la aplicación: gestiona la estructura principal y la navegación inferior
import { computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { App as CapacitorApp } from '@capacitor/app'
import BottomNav from './components/layout/BottomNav.vue'
import { useAuthStore } from './stores/authStore'

const route = useRoute()
const authStore = useAuthStore()

// La barra de navegación inferior solo aparece si el usuario está autenticado
// y no está en las páginas de login o modo cocina
const showBottomNav = computed(() => {
  return authStore.isAuthenticated && route.name !== 'login' && route.name !== 'cooking'
})

let backButtonListener = null

// Al montar la app, registramos el listener del botón físico de atrás (para Android)
onMounted(async () => {
  backButtonListener = await CapacitorApp.addListener('backButton', () => {
    // Si el usuario está en una ruta raíz, cerrar la aplicación directamente
    const rootPaths = ['/', '/login', '/home', '/explore', '/create', '/profile']

    if (rootPaths.includes(route.path)) {
      CapacitorApp.exitApp()
      return
    }

    // En cualquier otra ruta, navegar hacia atrás en el historial
    window.history.back()
  })
})

// Limpiamos el listener cuando el componente se desmonta para evitar fugas de memoria
onUnmounted(() => {
  backButtonListener?.remove()
})
</script>