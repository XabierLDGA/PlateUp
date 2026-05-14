<template>
  <div class="container-app space-y-6 py-6">
    <header class="flex items-center justify-between">
      <div>
        <p class="text-sm font-medium text-gray-500">Welcome back</p>
        <h1 class="text-3xl font-bold tracking-tight text-gray-900">
          PlateUp
        </h1>
      </div>

      <button
        type="button"
        class="h-12 w-12 overflow-hidden rounded-full bg-white shadow-sm ring-1 ring-black/5 transition hover:scale-105"
        @click="goToCurrentUserProfile"
      >
        <img
          :src="currentUserAvatar"
          alt="User avatar"
          class="h-full w-full object-cover"
        />
      </button>
    </header>

    <section class="flex gap-3 overflow-x-auto pb-3">
      <button
        v-for="item in filters"
        :key="item"
        @click="changeFilter(item)"
        class="shrink-0 rounded-full px-4 py-2 text-sm font-semibold transition"
        :class="
          activeFilter === item
            ? 'bg-[#f45b3f] text-white shadow'
            : 'bg-white text-gray-600 ring-1 ring-black/5'
        "
      >
        {{ item }}
      </button>
    </section>

    <section class="rounded-[32px] bg-gradient-to-br from-[#f45b3f] to-[#ff8a66] p-5 text-white shadow-lg">
      <div class="mb-4 flex items-center justify-between">
        <div>
          <p class="text-sm text-white/80">Your Cooking Journey</p>
          <h2 class="text-2xl font-bold">Keep your streak alive</h2>
        </div>
        <div class="rounded-full bg-white/20 px-4 py-2 text-sm font-semibold backdrop-blur">
          {{ streakDays }} day streak
        </div>
      </div>

      <div class="grid grid-cols-3 gap-3">
        <div class="rounded-3xl bg-white/15 p-4 backdrop-blur">
          <p class="text-xs text-white/80">Recipes</p>
          <p class="mt-2 text-2xl font-bold">{{ userRecipeCount }}</p>
        </div>
        <div class="rounded-3xl bg-white/15 p-4 backdrop-blur">
          <p class="text-xs text-white/80">Medals</p>
          <p class="mt-2 text-2xl font-bold">{{ achievementsCount }}</p>
        </div>
        <div class="rounded-3xl bg-white/15 p-4 backdrop-blur">
          <p class="text-xs text-white/80">Points</p>
          <p class="mt-2 text-2xl font-bold">{{ totalAchievementPoints }}</p>
        </div>
      </div>
    </section>

    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold text-gray-900">Fresh from the kitchen</h2>
        <span class="text-sm font-medium text-[#f45b3f]">{{ totalFeedCount }} recipes</span>
      </div>

      <div
        v-if="loading"
        class="rounded-3xl bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
      >
        Loading recipes...
      </div>

      <div
        v-else-if="errorMessage"
        class="rounded-3xl bg-red-50 p-6 text-center text-red-600 shadow-sm ring-1 ring-red-100"
      >
        {{ errorMessage }}
      </div>

      <div
        v-else-if="feedRecipes.length === 0"
        class="rounded-3xl bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
      >
        Follow more people to see their recipes here.
      </div>

      <div v-else class="space-y-5">
        <RecipeCard
          v-for="recipe in feedRecipes"
          :key="recipe.id"
          :recipe="recipe"
          :author="getAuthorByUserId(recipe.userId)"
          :likes-count="getLikesCount(recipe.id)"
          :is-liked="isRecipeLikedByCurrentUser(recipe.id)"
          :like-loading="likeLoadingRecipeId === Number(recipe.id)"
          @toggle-like="toggleRecipeLike"
        />

        <div ref="scrollSentinel" class="h-4" />

        <div
          v-if="loadingMore"
          class="rounded-3xl bg-white p-4 text-center text-sm text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          Loading more...
        </div>

        <div
          v-else-if="!hasMoreRecipes && feedRecipes.length > 0"
          class="rounded-3xl bg-white p-4 text-center text-sm text-gray-400 shadow-sm ring-1 ring-black/5"
        >
          You've seen all the recipes!
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import RecipeCard from '../components/recipes/RecipeCard.vue'
import recipeService from '../services/recipeService'
import userService from '../services/userService'
import likeService from '../services/likeService'
import achievementService from '../services/achievementService'
import userAchievementService from '../services/userAchievementService'
import { getUserAvatar } from '../utils/userAvatar'
import { RECIPE_CATEGORIES } from '../utils/recipeCategories'

const PAGE_SIZE = 10

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const loadingMore = ref(false)
const errorMessage = ref('')
const feedRecipes = ref([])
const users = ref([])
const likes = ref([])
const likeCounts = ref({})
const achievements = ref([])
const userAchievements = ref([])
const likeLoadingRecipeId = ref(null)
const currentPage = ref(0)
const hasMoreRecipes = ref(true)
const totalFeedCount = ref(0)
const scrollSentinel = ref(null)
let observer = null

const filters = ['All', ...RECIPE_CATEGORIES]
const activeFilter = ref('All')

const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))
const currentUserAvatar = computed(() => getUserAvatar(authStore.currentUser))

const userRecipeCountRef = ref(0)

const userRecipeCount = computed(() => userRecipeCountRef.value)

const achievementsCount = computed(() => {
  if (!currentUserId.value) return 0
  return userAchievements.value.filter(
    (item) => Number(item.user_id) === currentUserId.value
  ).length
})

const totalAchievementPoints = computed(() => {
  if (!currentUserId.value) return 0
  return userAchievements.value
    .filter((item) => Number(item.user_id) === currentUserId.value)
    .reduce((sum, item) => {
      const achievement = achievements.value.find(
        (a) => Number(a.id) === Number(item.achievement_id)
      )
      return sum + (achievement?.points || 0)
    }, 0)
})

const streakDays = computed(() => {
  return Number(authStore.currentUser?.streakCount || 0)
})

function getAuthorByUserId(userId) {
  return users.value.find((user) => Number(user.id) === Number(userId)) || null
}

function getLikesCount(recipeId) {
  return Number(likeCounts.value[Number(recipeId)] ?? 0)
}

function isRecipeLikedByCurrentUser(recipeId) {
  if (!currentUserId.value) return false
  return likes.value.some(
    (like) =>
      Number(like.userId) === currentUserId.value &&
      Number(like.recipeId) === Number(recipeId)
  )
}

async function toggleRecipeLike(recipe) {
  const recipeId = Number(recipe?.id)
  if (!currentUserId.value || !recipeId || likeLoadingRecipeId.value === recipeId) return

  likeLoadingRecipeId.value = recipeId

  try {
    if (isRecipeLikedByCurrentUser(recipeId)) {
      await likeService.remove(currentUserId.value, recipeId)
      likes.value = likes.value.filter(
        (like) =>
          !(Number(like.userId) === currentUserId.value && Number(like.recipeId) === recipeId)
      )
      likeCounts.value[recipeId] = Math.max(0, (Number(likeCounts.value[recipeId]) || 0) - 1)
    } else {
      const payload = { userId: currentUserId.value, recipeId }
      const response = await likeService.create(payload)
      likes.value.push(response.data || payload)
      likeCounts.value[recipeId] = (Number(likeCounts.value[recipeId]) || 0) + 1

      const refreshedAchievements = await userAchievementService.getByUserId(currentUserId.value)
      userAchievements.value = refreshedAchievements.data || []
    }
  } catch (error) {
    console.error('Error toggling like in HomeView:', error)
  } finally {
    likeLoadingRecipeId.value = null
  }
}

function goToCurrentUserProfile() {
  router.push({ name: 'profile' })
}

async function loadFeedPage(reset = false) {
  if (!currentUserId.value) return

  if (reset) {
    currentPage.value = 0
    hasMoreRecipes.value = true
    feedRecipes.value = []
    totalFeedCount.value = 0
    likeCounts.value = {}
  }

  if (!hasMoreRecipes.value) return

  const isFirstPage = currentPage.value === 0
  if (isFirstPage) {
    loading.value = true
  } else {
    loadingMore.value = true
  }

  try {
    const response = await recipeService.getFeed({
      page: currentPage.value,
      size: PAGE_SIZE,
      category: activeFilter.value !== 'All' ? activeFilter.value : undefined,
    })

    const page = response.data
    const newRecipes = page.content || []

    feedRecipes.value = reset ? newRecipes : [...feedRecipes.value, ...newRecipes]
    hasMoreRecipes.value = !page.last
    totalFeedCount.value = page.totalElements ?? totalFeedCount.value

    const newIds = newRecipes.map((r) => Number(r.id)).filter(Boolean)
    if (newIds.length > 0) {
      const countsResponse = await likeService.getCounts(newIds)
      Object.assign(likeCounts.value, countsResponse.data || {})
    }
    currentPage.value += 1
  } catch (error) {
    console.error('Error loading feed:', error)
    if (currentPage.value === 0) {
      errorMessage.value = 'Could not load recipes right now.'
    }
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

function loadMoreRecipes() {
  if (!loadingMore.value && hasMoreRecipes.value && !loading.value) {
    loadFeedPage()
  }
}

function changeFilter(item) {
  activeFilter.value = item
}

watch(activeFilter, () => {
  loadFeedPage(true)
})

function setupObserver() {
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        loadMoreRecipes()
      }
    },
    { rootMargin: '200px' }
  )

  if (scrollSentinel.value) {
    observer.observe(scrollSentinel.value)
  }
}

async function loadData() {
  loading.value = true
  errorMessage.value = ''

  try {
    await authStore.initialize()

    const [usersResponse, likesResponse, achievementsResponse] = await Promise.all([
      userService.getAll(),
      currentUserId.value ? likeService.getByUserId(currentUserId.value) : Promise.resolve({ data: [] }),
      achievementService.getAll(),
    ])

    users.value = usersResponse.data || []
    likes.value = likesResponse.data || []
    achievements.value = achievementsResponse.data || []

    if (currentUserId.value) {
      const userAchievementsResponse = await userAchievementService.getByUserId(currentUserId.value)
      userAchievements.value = userAchievementsResponse.data || []
    }

    await loadFeedPage(true)
  } catch (error) {
    console.error('Error loading home data:', error)
    errorMessage.value = 'Could not load recipes right now.'
    loading.value = false
  }
}

async function loadUserRecipeCount() {
  if (!currentUserId.value) return
  try {
    const response = await recipeService.countByUser(currentUserId.value)
    userRecipeCountRef.value = Number(response.data) || 0
  } catch {
    userRecipeCountRef.value = 0
  }
}

onMounted(async () => {
  await loadData()
  await loadUserRecipeCount()
  setupObserver()
})

onUnmounted(() => {
  if (observer) observer.disconnect()
})
</script>
