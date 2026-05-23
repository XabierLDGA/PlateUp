<template>
  <div class="container-app space-y-6 py-6">

    <!-- ── User card ─────────────────────────────────────────── -->
    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <div class="flex items-start gap-4">
        <button
          type="button"
          class="rounded-full transition hover:scale-105"
          @click="goToCurrentUserProfile"
        >
          <img
            :src="userAvatar"
            :alt="currentUser?.displayName"
            class="h-20 w-20 rounded-full object-cover ring-4 ring-orange-50"
          />
        </button>

        <div class="flex-1">
          <h1 class="text-2xl font-bold text-gray-900">
            {{ currentUser?.displayName || 'PlateUp User' }}
          </h1>
          <p class="text-sm text-gray-500">@{{ currentUser?.username || 'plateup' }}</p>
          <p class="mt-2 text-sm leading-6 text-gray-600">
            {{ currentUser?.bio || 'No bio available yet.' }}
          </p>
        </div>
      </div>

      <div class="mt-5 grid grid-cols-3 gap-3">
        <div class="rounded-3xl bg-orange-50 p-4 text-center">
          <p class="text-xs text-gray-500">Recipes</p>
          <p class="mt-1 text-2xl font-bold text-[#f45b3f]">{{ myRecipes.length }}</p>
        </div>

        <button
          type="button"
          class="rounded-3xl bg-gray-50 p-4 text-center transition hover:bg-gray-100"
          @click="openConnections('followers')"
        >
          <p class="text-xs text-gray-500">Followers</p>
          <p class="mt-1 text-2xl font-bold text-gray-800">{{ followersCount }}</p>
        </button>

        <button
          type="button"
          class="rounded-3xl bg-gray-50 p-4 text-center transition hover:bg-gray-100"
          @click="openConnections('following')"
        >
          <p class="text-xs text-gray-500">Following</p>
          <p class="mt-1 text-2xl font-bold text-gray-800">{{ followingCount }}</p>
        </button>
      </div>
    </section>

    <!-- ── Tabs ──────────────────────────────────────────────── -->
    <section class="flex gap-3">
      <button
        @click="activeTab = 'recipes'"
        class="flex-1 rounded-full px-4 py-3 text-sm font-semibold transition"
        :class="activeTab === 'recipes' ? 'bg-[#f45b3f] text-white' : 'bg-white text-gray-600 ring-1 ring-black/5'"
      >
        My Recipe Book
      </button>

      <button
        @click="activeTab = 'achievements'"
        class="flex-1 rounded-full px-4 py-3 text-sm font-semibold transition"
        :class="activeTab === 'achievements' ? 'bg-[#f45b3f] text-white' : 'bg-white text-gray-600 ring-1 ring-black/5'"
      >
        Achievements
      </button>
    </section>

    <!-- ── Recipe Book tab ───────────────────────────────────── -->
    <section v-if="activeTab === 'recipes'" class="space-y-8">

      <!-- My created recipes -->
      <div class="space-y-4">
        <div class="flex items-center justify-between gap-3">
          <h2 class="text-xl font-bold text-gray-900">My Recipe Book</h2>
          <p v-if="deleteErrorMessage" class="text-sm font-medium text-red-600">
            {{ deleteErrorMessage }}
          </p>
        </div>

        <div
          v-if="myRecipes.length === 0"
          class="rounded-[28px] bg-white p-6 text-center text-sm text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          You still haven't published any recipes.
        </div>

        <div v-else class="grid grid-cols-2 gap-4">
          <article
            v-for="recipe in myRecipes"
            :key="recipe.id"
            class="overflow-hidden rounded-[28px] bg-white shadow-sm ring-1 ring-black/5"
          >
            <RouterLink
              :to="`/recipes/${recipe.id}`"
              class="block"
            >
              <img
                :src="resolveRecipeImage(recipe)"
                :alt="recipe.title"
                class="h-36 w-full object-cover"
              />
            </RouterLink>

            <div class="space-y-3 p-3">
              <RouterLink :to="`/recipes/${recipe.id}`" class="block">
                <h3 class="line-clamp-1 text-sm font-bold text-gray-900">{{ recipe.title }}</h3>
                <p class="text-xs text-gray-500">{{ recipe.totalMinutes }} min · {{ recipe.difficulty }}</p>
              </RouterLink>

              <button
                type="button"
                class="flex w-full items-center justify-center gap-2 rounded-2xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-600 transition hover:bg-red-100 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="deletingRecipeId === Number(recipe.id)"
                @click="openDeleteModal(recipe)"
              >
                <Trash2 class="h-4 w-4" />
                <span>
                  {{ deletingRecipeId === Number(recipe.id) ? 'Deleting...' : 'Delete recipe' }}
                </span>
              </button>
            </div>
          </article>
        </div>
      </div>

      <!-- Cooked recipes -->
      <div class="space-y-4">
        <div class="flex items-center gap-3">
          <h2 class="text-xl font-bold text-gray-900">Cooked Recipes</h2>
          <span
            v-if="cookedRecipeDetails.length"
            class="flex h-6 min-w-[1.5rem] items-center justify-center rounded-full bg-[#f45b3f] px-2 text-xs font-bold text-white"
          >
            {{ cookedRecipeDetails.length }}
          </span>
        </div>

        <div
          v-if="cookedRecipeDetails.length === 0"
          class="rounded-[28px] bg-white p-6 text-center text-sm text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          You haven't cooked any recipes yet. Hit <strong>Start Cooking</strong> on any recipe!
        </div>

        <div v-else class="grid grid-cols-2 gap-4">
          <article
            v-for="item in cookedRecipeDetails"
            :key="item.recipe.id"
            class="relative overflow-hidden rounded-[28px] bg-white shadow-sm ring-1 ring-black/5"
          >
            <RouterLink :to="`/recipes/${item.recipe.id}`" class="block">
              <img
                :src="resolveRecipeImage(item.recipe)"
                :alt="item.recipe.title"
                class="h-36 w-full object-cover"
              />
            </RouterLink>

            <div class="space-y-2 p-3">
              <RouterLink :to="`/recipes/${item.recipe.id}`" class="block">
                <h3 class="line-clamp-1 text-sm font-bold text-gray-900">{{ item.recipe.title }}</h3>
                <p class="text-xs text-gray-500">
                  Cooked in {{ formatElapsed(item.cooked.elapsedSeconds) }}
                </p>
              </RouterLink>

              <button
                type="button"
                class="flex w-full items-center justify-center gap-1 rounded-2xl bg-orange-50 px-3 py-2 text-xs font-semibold text-[#f45b3f] transition hover:bg-orange-100"
                @click="cookAgain(item.recipe.id)"
              >
                <ChefHat class="h-4 w-4" />
                <span>Cook again</span>
              </button>
            </div>
          </article>
        </div>
      </div>

    </section>

    <!-- ── Achievements tab ──────────────────────────────────── -->
    <section v-else class="space-y-4">
      <h2 class="text-xl font-bold text-gray-900">Achievements</h2>

      <div
        v-if="earnedAchievements.length === 0"
        class="rounded-[28px] bg-white p-6 text-center text-sm text-gray-500 shadow-sm ring-1 ring-black/5"
      >
        You still have no achievements unlocked.
      </div>

      <div v-else class="space-y-3">
        <article
          v-for="achievement in earnedAchievements"
          :key="achievement.id"
          class="rounded-[28px] bg-white p-4 shadow-sm ring-1 ring-black/5"
        >
          <div class="flex items-start gap-4">
            <div class="flex h-14 w-14 items-center justify-center rounded-2xl bg-orange-50 text-2xl">
              🏅
            </div>

            <div class="flex-1">
              <div class="flex items-center justify-between gap-3">
                <h3 class="text-base font-bold text-gray-900">{{ achievement.name }}</h3>
                <span class="rounded-full bg-orange-50 px-3 py-1 text-xs font-semibold text-[#f45b3f]">
                  {{ achievement.points }} pts
                </span>
              </div>

              <p class="mt-1 text-sm text-gray-600">{{ achievement.description }}</p>
              <p class="mt-2 text-xs text-gray-400">{{ achievement.category }}</p>
            </div>
          </div>
        </article>
      </div>
    </section>

    <!-- ── Settings ──────────────────────────────────────────── -->
    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <h2 class="text-lg font-bold text-gray-900">Settings</h2>

      <div class="mt-4 space-y-3">
        <button
          type="button"
          class="flex w-full items-center justify-between rounded-2xl bg-gray-50 px-4 py-4 text-sm font-semibold text-gray-700 transition hover:bg-gray-100"
          @click="goToEditProfile"
        >
          <span>Edit profile</span>
          <span>›</span>
        </button>

        <button
          @click="logout"
          class="flex w-full items-center justify-between rounded-2xl bg-red-50 px-4 py-4 text-sm font-semibold text-red-600"
        >
          <span>Log out</span>
          <span>›</span>
        </button>
      </div>
    </section>

    <!-- ── Delete modal ──────────────────────────────────────── -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 z-[200] flex items-center justify-center bg-black/50 px-4"
    >
      <div class="w-full max-w-sm rounded-[28px] bg-white p-6 shadow-2xl">
        <div class="flex items-center gap-3">
          <div class="flex h-12 w-12 items-center justify-center rounded-full bg-red-50">
            <Trash2 class="h-6 w-6 text-red-600" />
          </div>

          <div>
            <h3 class="text-lg font-bold text-gray-900">Delete recipe?</h3>
            <p class="text-sm text-gray-500">This action cannot be undone.</p>
          </div>
        </div>

        <div class="mt-4 rounded-2xl bg-gray-50 px-4 py-3">
          <p class="text-sm font-semibold text-gray-800">
            {{ recipeToDelete?.title || 'Selected recipe' }}
          </p>
        </div>

        <div class="mt-6 flex gap-3">
          <button
            type="button"
            class="flex-1 rounded-2xl bg-gray-100 px-4 py-3 text-sm font-semibold text-gray-700 transition hover:bg-gray-200"
            :disabled="deletingRecipeId !== null"
            @click="closeDeleteModal"
          >
            Cancel
          </button>

          <button
            type="button"
            class="flex-1 rounded-2xl bg-[#f45b3f] px-4 py-3 text-sm font-semibold text-white transition hover:bg-[#e14f35] disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="deletingRecipeId !== null"
            @click="deleteSelectedRecipe"
          >
            {{ deletingRecipeId !== null ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Trash2, ChefHat } from 'lucide-vue-next'
import { useAuthStore } from '../stores/authStore'
import recipeService from '../services/recipeService'
import followService from '../services/followService'
import achievementService from '../services/achievementService'
import userAchievementService from '../services/userAchievementService'
import cookedRecipeService from '../services/cookedRecipeService'
import { getRecipeImage } from '../utils/recipeImage'
import { getUserAvatar } from '../utils/userAvatar'

const router = useRouter()
const authStore = useAuthStore()

// Pestaña activa en la vista del perfil: 'recipes' o 'achievements'
const activeTab = ref('recipes')

// Recetas propias creadas por el usuario
const myRecipes = ref([])
// Registros de recetas cocinadas y todas las recetas necesarias para resolverlas
const cookedEntries = ref([])
const allRecipes = ref([])

// Contadores de seguidores y seguidos del usuario
const followersCount = ref(0)
const followingCount = ref(0)

// Datos de logros
const achievements = ref([])
const userAchievements = ref([])

// Estado del modal de eliminación de receta
const deletingRecipeId = ref(null)
const deleteErrorMessage = ref('')
const showDeleteModal = ref(false)
const recipeToDelete = ref(null)

// Usuario actual y su avatar
const currentUser = computed(() => authStore.currentUser)
const userAvatar = computed(() => getUserAvatar(currentUser.value))

// Lista de logros conseguidos por el usuario, cruzando sus IDs con los datos completos
const earnedAchievements = computed(() => {
  if (!currentUser.value?.id) return []

  const mine = userAchievements.value.filter(
    (item) => Number(item.user_id) === Number(currentUser.value.id)
  )

  return mine
    .map((userAchievement) =>
      achievements.value.find(
        (achievement) => Number(achievement.id) === Number(userAchievement.achievement_id)
      )
    )
    .filter(Boolean)
})

// Recetas cocinadas ajenas (excluye las propias para no duplicarlas)
const cookedRecipeDetails = computed(() => {
  const myIds = new Set(myRecipes.value.map((r) => Number(r.id)))

  return cookedEntries.value
    .map((cooked) => {
      const recipe = allRecipes.value.find((r) => Number(r.id) === Number(cooked.recipeId))
      return recipe ? { cooked, recipe } : null
    })
    .filter((item) => item && !myIds.has(Number(item.recipe.id)))
})

function resolveRecipeImage(recipe) {
  return getRecipeImage(recipe)
}

// Convierte segundos en un formato legible de tiempo tipo "5m 30s" o "1h 05m"
function formatElapsed(seconds) {
  if (!seconds) return '—'
  const h = Math.floor(seconds / 3600)
  const m = Math.floor((seconds % 3600) / 60)
  const s = seconds % 60
  const pad = (n) => String(n).padStart(2, '0')
  return h > 0 ? `${pad(h)}h ${pad(m)}m` : `${pad(m)}m ${pad(s)}s`
}

// Navega al propio perfil del usuario
function goToCurrentUserProfile() {
  router.push({ name: 'profile' })
}

// Navega a la pantalla de edición de perfil
function goToEditProfile() {
  router.push({ name: 'edit-profile' })
}

// Abre la vista de conexiones en la pestaña indicada (followers o following)
function openConnections(tab) {
  if (!currentUser.value?.id) return

  router.push({
    name: 'connections',
    params: { id: currentUser.value.id },
    query: { tab },
  })
}

// Cierra sesión y redirige a la pantalla de inicio
function logout() {
  authStore.logout()
  router.push('/')
}

// Redirige a la vista de cocina para cocinar una receta de nuevo
function cookAgain(recipeId) {
  router.push({ name: 'cooking', params: { id: recipeId } })
}

// Abre el modal de confirmación para eliminar una receta
function openDeleteModal(recipe) {
  if (!recipe?.id) return

  deleteErrorMessage.value = ''
  recipeToDelete.value = recipe
  showDeleteModal.value = true
}

// Cierra el modal de eliminar receta si no hay una eliminación en curso
function closeDeleteModal() {
  if (deletingRecipeId.value !== null) return

  showDeleteModal.value = false
  recipeToDelete.value = null
}

// Elimina la receta seleccionada y la quita de la lista local
async function deleteSelectedRecipe() {
  const recipeId = Number(recipeToDelete.value?.id)

  if (!recipeId || deletingRecipeId.value === recipeId) return

  deletingRecipeId.value = recipeId
  deleteErrorMessage.value = ''

  try {
    await recipeService.remove(recipeId)
    myRecipes.value = myRecipes.value.filter((item) => Number(item.id) !== recipeId)
    showDeleteModal.value = false
    recipeToDelete.value = null
  } catch (error) {
    console.error('Error deleting recipe:', error)
    deleteErrorMessage.value = 'The recipe could not be deleted.'
  } finally {
    deletingRecipeId.value = null
  }
}

// Carga todas las recetas, seguidores, logros y recetas cocinadas del usuario
async function loadData() {
  await authStore.initialize()

  const userId = authStore.currentUser?.id
  if (!userId) return

  const [
    recipesResponse,
    followersResponse,
    followingResponse,
    achievementsResponse,
    userAchievementsResponse,
    cookedResponse,
  ] = await Promise.allSettled([
    recipeService.getByUserId(userId),
    followService.getFollowers(userId),
    followService.getFollowing(userId),
    achievementService.getAll(),
    userAchievementService.getByUserId(userId),
    cookedRecipeService.getByUserId(userId),
  ])

  myRecipes.value = recipesResponse.status === 'fulfilled' ? recipesResponse.value.data || [] : []
  followersCount.value = followersResponse.status === 'fulfilled' ? followersResponse.value.data?.length || 0 : 0
  followingCount.value = followingResponse.status === 'fulfilled' ? followingResponse.value.data?.length || 0 : 0
  achievements.value = achievementsResponse.status === 'fulfilled' ? achievementsResponse.value.data || [] : []
  userAchievements.value = userAchievementsResponse.status === 'fulfilled' ? userAchievementsResponse.value.data || [] : []
  cookedEntries.value = cookedResponse.status === 'fulfilled' ? cookedResponse.value.data || [] : []

  // Cargamos los datos de las recetas cocinadas para poder mostrar su información
  const cookedIds = [...new Set(cookedEntries.value.map((c) => Number(c.recipeId)).filter(Boolean))]
  if (cookedIds.length > 0) {
    const allRecipesResponse = await recipeService.getByIds(cookedIds)
    allRecipes.value = allRecipesResponse.data || []
  } else {
    allRecipes.value = []
  }
}

onMounted(loadData)
</script>