<template>
  <div class="container-app space-y-6 py-6">
    <header>
      <p class="text-sm font-medium text-gray-500">Panel de control</p>
      <h1 class="text-3xl font-bold tracking-tight text-gray-900">Dashboard</h1>
    </header>

    <div v-if="loading" class="rounded-3xl bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5">
      Cargando datos...
    </div>

    <template v-else>
      <!-- KPI Cards -->
      <section class="grid grid-cols-2 gap-3">
        <div class="rounded-3xl bg-white p-4 shadow-sm ring-1 ring-black/5">
          <p class="text-xs font-medium text-gray-500">Usuarios totales</p>
          <p class="mt-1 text-3xl font-bold text-gray-900">{{ stats.totalUsers ?? 0 }}</p>
        </div>
        <div class="rounded-3xl bg-white p-4 shadow-sm ring-1 ring-black/5">
          <p class="text-xs font-medium text-gray-500">Recetas totales</p>
          <p class="mt-1 text-3xl font-bold text-gray-900">{{ stats.totalRecipes ?? 0 }}</p>
        </div>
        <div class="rounded-3xl bg-white p-4 shadow-sm ring-1 ring-black/5">
          <p class="text-xs font-medium text-gray-500">Likes totales</p>
          <p class="mt-1 text-3xl font-bold text-[#f45b3f]">{{ stats.totalLikes ?? 0 }}</p>
        </div>
        <div class="rounded-3xl bg-white p-4 shadow-sm ring-1 ring-black/5">
          <p class="text-xs font-medium text-gray-500">Recetas cocinadas totales</p>
          <p class="mt-1 text-3xl font-bold text-gray-900">{{ stats.totalCooked ?? 0 }}</p>
        </div>
      </section>

      <!-- Recetas por categoría -->
      <section class="rounded-3xl bg-white p-5 shadow-sm ring-1 ring-black/5">
        <h2 class="mb-4 text-base font-bold text-gray-900">Recetas por categoría</h2>
        <Doughnut :data="categoryChartData" :options="doughnutOptions" />
      </section>

      <!-- Nuevos usuarios por mes -->
      <section class="rounded-3xl bg-white p-5 shadow-sm ring-1 ring-black/5">
        <h2 class="mb-4 text-base font-bold text-gray-900">Nuevos usuarios (últimos 6 meses)</h2>
        <Bar :data="usersMonthChartData" :options="barOptions" />
      </section>

      <!-- Top usuarios -->
      <section class="rounded-3xl bg-white p-5 shadow-sm ring-1 ring-black/5">
        <h2 class="mb-4 text-base font-bold text-gray-900">Top usuarios por recetas</h2>
        <Bar :data="topUsersChartData" :options="barOptions" />
      </section>

      <!-- Top recetas -->
      <section class="rounded-3xl bg-white p-5 shadow-sm ring-1 ring-black/5">
        <h2 class="mb-4 text-base font-bold text-gray-900">Recetas más populares</h2>
        <div class="space-y-3">
          <div
            v-for="(recipe, index) in topRecipes"
            :key="recipe.recipeId"
            class="flex items-center gap-3"
          >
            <span class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-[#f45b3f]/10 text-xs font-bold text-[#f45b3f]">
              {{ index + 1 }}
            </span>
            <img
              v-if="recipe.imageUrl"
              :src="recipe.imageUrl"
              class="h-10 w-10 rounded-xl object-cover"
              alt=""
            />
            <div class="min-w-0 flex-1">
              <p class="truncate text-sm font-semibold text-gray-900">{{ recipe.title }}</p>
              <p class="text-xs text-gray-500">{{ recipe.category }}</p>
            </div>
            <span class="shrink-0 text-sm font-bold text-[#f45b3f]">{{ recipe.likeCount }} ♥</span>
          </div>
        </div>
      </section>

      <!-- Gestión de roles -->
      <section class="rounded-3xl bg-white p-5 shadow-sm ring-1 ring-black/5">
        <h2 class="mb-3 text-base font-bold text-gray-900">Gestión de usuarios</h2>
        <input
          v-model="userSearch"
          type="text"
          placeholder="Buscar usuario..."
          class="mb-4 w-full rounded-2xl bg-gray-50 px-4 py-2 text-sm ring-1 ring-black/5 focus:outline-none focus:ring-2 focus:ring-[#f45b3f]"
        />
        <div class="space-y-3">
          <div
            v-for="user in visibleUsers"
            :key="user.id"
            class="flex items-center gap-3"
          >
            <img
              :src="user.avatarUrl || 'https://ui-avatars.com/api/?name=' + user.username"
              class="h-9 w-9 rounded-full object-cover"
              alt=""
            />
            <div class="min-w-0 flex-1">
              <p class="truncate text-sm font-semibold text-gray-900">{{ user.displayName || user.username }}</p>
              <p class="text-xs text-gray-400">@{{ user.username }}</p>
            </div>
            <button
              @click="toggleRole(user)"
              :disabled="isSelf(user)"
              :class="user.role === 'ADMIN'
                ? 'bg-[#f45b3f] text-white'
                : 'bg-gray-100 text-gray-600'"
              class="shrink-0 rounded-full px-3 py-1 text-xs font-semibold transition disabled:opacity-40 disabled:cursor-not-allowed"
            >
              {{ user.role === 'ADMIN' ? 'Admin' : 'User' }}
            </button>
          </div>
          <p v-if="visibleUsers.length === 0" class="text-center text-sm text-gray-400">
            No se encontraron usuarios.
          </p>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Bar, Doughnut } from 'vue-chartjs'
import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js'
import adminService from '../services/adminService'
import userService from '../services/userService'
import { useAuthStore } from '../stores/authStore'

ChartJS.register(ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend)

const authStore = useAuthStore()

// Estado general de carga y datos del panel
const loading = ref(true)
const stats = ref({})
const recipesByCategory = ref({})
const topUsers = ref([])
const topRecipes = ref([])
const usersByMonth = ref({})
const allUsers = ref([])

// Texto de búsqueda para filtrar usuarios en la sección de gestión
const userSearch = ref('')

// Devuelve los usuarios que coinciden con la búsqueda, o los primeros 5 si no hay filtro
const visibleUsers = computed(() => {
  const q = userSearch.value.trim().toLowerCase()
  const filtered = q
    ? allUsers.value.filter(
        u =>
          u.username.toLowerCase().includes(q) ||
          (u.displayName || '').toLowerCase().includes(q)
      )
    : allUsers.value.slice(0, 5)
  return filtered
})

// Comprueba si el usuario de la lista es el mismo que está logueado
function isSelf(user) {
  return Number(user.id) === Number(authStore.currentUser?.id)
}

// Paleta de colores usada en las gráficas
const COLORS = [
  '#f45b3f', '#ff8a66', '#ffd580', '#4bc0c0',
  '#36a2eb', '#9966ff', '#ff6384', '#c9cbcf',
]

// Datos formateados para la gráfica de recetas por categoría
const categoryChartData = computed(() => ({
  labels: Object.keys(recipesByCategory.value),
  datasets: [{
    data: Object.values(recipesByCategory.value),
    backgroundColor: COLORS,
    borderWidth: 0,
  }],
}))

// Datos formateados para la gráfica de nuevos usuarios por mes
const usersMonthChartData = computed(() => ({
  labels: Object.keys(usersByMonth.value),
  datasets: [{
    label: 'Nuevos usuarios',
    data: Object.values(usersByMonth.value),
    backgroundColor: '#f45b3f',
    borderRadius: 8,
  }],
}))

// Datos formateados para la gráfica de los usuarios con más recetas
const topUsersChartData = computed(() => ({
  labels: topUsers.value.map(u => u.displayName || u.username),
  datasets: [{
    label: 'Recetas',
    data: topUsers.value.map(u => u.recipeCount),
    backgroundColor: '#ff8a66',
    borderRadius: 8,
  }],
}))

// Opciones de configuración para las gráficas de tipo dónut y barras
const doughnutOptions = {
  responsive: true,
  plugins: { legend: { position: 'bottom' } },
}

const barOptions = {
  responsive: true,
  plugins: { legend: { display: false } },
  scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } },
}

// Cambia el rol de un usuario entre ADMIN y USER
async function toggleRole(user) {
  const newRole = user.role === 'ADMIN' ? 'USER' : 'ADMIN'
  try {
    const response = await adminService.updateUserRole(user.id, newRole)
    user.role = response.data.role
  } catch (error) {
    console.error('Error updating role:', error)
  }
}

// Carga todos los datos del panel al montar la vista
onMounted(async () => {
  try {
    const [statsRes, categoryRes, topUsersRes, topRecipesRes, monthRes, usersRes] =
      await Promise.all([
        adminService.getStats(),
        adminService.getRecipesByCategory(),
        adminService.getTopUsers(),
        adminService.getTopRecipes(),
        adminService.getUsersByMonth(),
        userService.getAll(),
      ])

    stats.value = statsRes.data || {}
    recipesByCategory.value = categoryRes.data || {}
    topUsers.value = topUsersRes.data || []
    topRecipes.value = topRecipesRes.data || []
    usersByMonth.value = monthRes.data || {}
    allUsers.value = usersRes.data || []
  } catch (error) {
    console.error('Error loading admin data:', error)
  } finally {
    loading.value = false
  }
})
</script>
