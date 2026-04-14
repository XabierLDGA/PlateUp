<template>
  <nav
    class="fixed bottom-[calc(env(safe-area-inset-bottom,0px)+0.5rem)] left-1/2 z-50 flex w-[calc(100%-1.25rem)] max-w-md -translate-x-1/2 items-center justify-around rounded-full bg-white/95 px-2 py-2 shadow-lg ring-1 ring-black/5 backdrop-blur"
  >
    <RouterLink
      v-for="item in navItems"
      :key="item.name"
      :to="item.to"
      class="flex min-w-0 flex-col items-center gap-0.5 rounded-full px-2 py-1 text-[11px] font-medium leading-none transition"
      :class="route.path === item.to ? 'text-[#f45b3f]' : 'text-gray-400'"
    >
      <component :is="item.icon" class="h-4.5 w-4.5" />
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