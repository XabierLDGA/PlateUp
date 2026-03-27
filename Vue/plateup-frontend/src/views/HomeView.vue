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
        @click="activeFilter = item"
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
        <span class="text-sm font-medium text-[#f45b3f]">{{ filteredRecipes.length }} recipes</span>
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
        v-else-if="filteredRecipes.length === 0"
        class="rounded-3xl bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
      >
        Follow more people to see their recipes here.
      </div>

      <div v-else class="space-y-5">
        <RecipeCard
          v-for="recipe in filteredRecipes"
          :key="recipe.id"
          :recipe="recipe"
          :author="getAuthorByUserId(recipe.userId)"
          :likes-count="getLikesCount(recipe.id)"
          :is-liked="isRecipeLikedByCurrentUser(recipe.id)"
          :like-loading="likeLoadingRecipeId === Number(recipe.id)"
          @toggle-like="toggleRecipeLike"
        />
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import RecipeCard from '../components/recipes/RecipeCard.vue'
import recipeService from '../services/recipeService'
import userService from '../services/userService'
import likeService from '../services/likeService'
import followService from '../services/followService'
import achievementService from '../services/achievementService'
import userAchievementService from '../services/userAchievementService'
import { getUserAvatar } from '../utils/userAvatar'
import { RECIPE_CATEGORIES, normalizeRecipeCategory } from '../utils/recipeCategories'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const errorMessage = ref('')
const recipes = ref([])
const users = ref([])
const likes = ref([])
const follows = ref([])
const achievements = ref([])
const userAchievements = ref([])
const likeLoadingRecipeId = ref(null)

const filters = ['All', ...RECIPE_CATEGORIES]
const activeFilter = ref('All')

const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))
const currentUserAvatar = computed(() => getUserAvatar(authStore.currentUser))

const followedUserIds = computed(() => {
  if (!currentUserId.value) return []

  return follows.value
    .filter((follow) => Number(follow.followerId) === currentUserId.value)
    .map((follow) => Number(follow.followedId))
})

function getRecipeTimestamp(recipe) {
  const created = recipe?.createdAt ? new Date(recipe.createdAt).getTime() : NaN
  if (!Number.isNaN(created)) return created

  const updated = recipe?.updatedAt ? new Date(recipe.updatedAt).getTime() : NaN
  if (!Number.isNaN(updated)) return updated

  return 0
}

const visibleFeedRecipes = computed(() => {
  if (!currentUserId.value) return []

  return [...recipes.value]
    .filter((recipe) => {
      const authorId = Number(recipe.userId)
      return authorId === currentUserId.value || followedUserIds.value.includes(authorId)
    })
    .sort((a, b) => {
      const timeA = getRecipeTimestamp(a)
      const timeB = getRecipeTimestamp(b)

      if (timeA !== timeB) {
        return timeB - timeA
      }

      return Number(b?.id || 0) - Number(a?.id || 0)
    })
})

const userRecipeCount = computed(() => {
  if (!currentUserId.value) return 0

  return recipes.value.filter(
    (recipe) => Number(recipe.userId) === currentUserId.value
  ).length
})

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
        (achievementItem) => Number(achievementItem.id) === Number(item.achievement_id)
      )

      return sum + (achievement?.points || 0)
    }, 0)
})

const streakDays = computed(() => {
  const count = achievementsCount.value
  return count === 0 ? 1 : count + 2
})

const filteredRecipes = computed(() => {
  if (activeFilter.value === 'All') return visibleFeedRecipes.value

  return visibleFeedRecipes.value.filter((recipe) => {
    return normalizeRecipeCategory(recipe.category) === activeFilter.value
  })
})

function getAuthorByUserId(userId) {
  return users.value.find((user) => Number(user.id) === Number(userId)) || null
}

function getLikesCount(recipeId) {
  return likes.value.filter((like) => Number(like.recipeId) === Number(recipeId)).length
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

  if (!currentUserId.value || !recipeId || likeLoadingRecipeId.value === recipeId) {
    return
  }

  likeLoadingRecipeId.value = recipeId

  try {
    if (isRecipeLikedByCurrentUser(recipeId)) {
      await likeService.remove(currentUserId.value, recipeId)

      likes.value = likes.value.filter(
        (like) =>
          !(
            Number(like.userId) === currentUserId.value &&
            Number(like.recipeId) === recipeId
          )
      )
    } else {
      const payload = {
        userId: currentUserId.value,
        recipeId,
      }

      const response = await likeService.create(payload)

      likes.value.push(response.data || payload)
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

async function loadData() {
  loading.value = true
  errorMessage.value = ''

  try {
    await authStore.initialize()

    const [recipesResponse, usersResponse, likesResponse, followsResponse, achievementsResponse, userAchievementsResponse] =
      await Promise.all([
        recipeService.getAll(),
        userService.getAll(),
        likeService.getAll(),
        followService.getAll(),
        achievementService.getAll(),
        userAchievementService.getAll(),
      ])

    recipes.value = recipesResponse.data || []
    users.value = usersResponse.data || []
    likes.value = likesResponse.data || []
    follows.value = followsResponse.data || []
    achievements.value = achievementsResponse.data || []
    userAchievements.value = userAchievementsResponse.data || []
  } catch (error) {
    console.error('Error loading home data:', error)
    errorMessage.value = 'Could not load recipes right now.'
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>