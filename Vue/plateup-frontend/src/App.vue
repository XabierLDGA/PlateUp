<template>
  <div
    class="min-h-screen"
    :class="showBottomNav ? 'pb-24' : 'pb-0'"
  >
    <router-view />
    <BottomNav v-if="showBottomNav" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import BottomNav from './components/layout/BottomNav.vue'
import { useAuthStore } from './stores/authStore'

const route = useRoute()
const authStore = useAuthStore()

const showBottomNav = computed(() => {
  return authStore.isAuthenticated && route.name !== 'login' && route.name !== 'cooking'
})
</script>