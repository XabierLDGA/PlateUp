<template>
  <nav
    class="fixed bottom-4 left-1/2 z-50 flex w-[calc(100%-2rem)] max-w-md -translate-x-1/2 items-center justify-around rounded-full bg-white/95 px-4 py-3 shadow-xl ring-1 ring-black/5 backdrop-blur"
  >
    <RouterLink
      v-for="item in navItems"
      :key="item.name"
      :to="item.to"
      class="flex flex-col items-center gap-1 rounded-full px-3 py-2 text-xs font-medium transition"
      :class="route.path === item.to ? 'text-[#f45b3f]' : 'text-gray-400'"
    >
      <component :is="item.icon" class="h-5 w-5" />
      <span>{{ item.label }}</span>
    </RouterLink>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { House, Search, SquarePen, User } from 'lucide-vue-next'

const route = useRoute()

const navItems = computed(() => [
  { name: 'home', label: 'Home', to: '/home', icon: House },
  { name: 'explore', label: 'Explore', to: '/explore', icon: Search },
  { name: 'create', label: 'Create', to: '/create', icon: SquarePen },
  { name: 'profile', label: 'Profile', to: '/profile', icon: User },
])
</script>